package com.albertorusso.randomuser.presentation.list

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.albertorusso.randomuser.domain.model.User
import org.koin.androidx.compose.get
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.albertorusso.randomuser.R

@Composable
fun UserListScreen(onUserClick: (User) -> Unit) {

    val userListViewModel: UserListViewModel = get()
    val userList = userListViewModel.userList.collectAsState().value
    val isLoading = userListViewModel.loadingState.collectAsState().value
    val errorMessage = userListViewModel.errorState.collectAsState().value

    var searchTerm by remember { mutableStateOf("") }
    LaunchedEffect(searchTerm) {
        kotlinx.coroutines.delay(500)
        userListViewModel.searchUser(searchTerm) // Trigger search
    }

    val listState = rememberLazyListState()
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleItemIndex ->
                val totalItemsCount = listState.layoutInfo.totalItemsCount
                if (searchTerm.isEmpty() && lastVisibleItemIndex == totalItemsCount - 1) {
                    userListViewModel.getUsers(true) // Trigger loading more users
                }
            }
    }

    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackBarHostState.showSnackbar(it) //display snackbar
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            // Search box at the top
            TextField(
                value = searchTerm,
                onValueChange = { searchTerm = it },
                label = { Text("Search Users") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true
            )

            // User list
            if (userList.isNotEmpty()) {
                UserList(userList = userList,
                    listState = listState,
                    onUserClick = onUserClick,
                    onSwipeDelete = { user ->
                        userListViewModel.deleteUser(user.email)
                    })
            } else {
                Text(
                    text = stringResource(id = R.string.no_users_found),
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 24.sp)
                )
            }
        }

        // Show loading spinner on top of the content
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f))
                    .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator(modifier = Modifier.size(50.dp))
            }
        }
    }

    // Snackbar for error message
    SnackbarHost(
        hostState = snackBarHostState,
        modifier = Modifier
            .fillMaxSize()
    )

}

@Composable
fun UserList(userList: List<User>, listState: LazyListState, onUserClick: (User) -> Unit, onSwipeDelete: (User) -> Unit) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
         items(userList, key = { it.email }) { user ->
            UserItem(
                user = user,
                onClick = { onUserClick(user) },
                onSwipeDelete = { onSwipeDelete(user) }
            )
        }
    }
}

@Composable
fun UserItem(user: User, onClick: () -> Unit, onSwipeDelete: (String) -> Unit) {
    val dismissState = rememberSwipeToDismissBoxState()

    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onSwipeDelete(user.email)
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromEndToStart = true,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val scale by animateFloatAsState(
                if (dismissState.targetValue == SwipeToDismissBoxValue.Settled) 0.75f else 1f
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete icon",
                    modifier = Modifier.scale(scale)
                )
            }
        }
    ) {
        UserItemContent(user = user, onClick = onClick)
    }
}

@Composable
fun UserItemContent(user: User, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = user.pictureSmall),
                contentDescription = "User Profile Picture",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${user.firstName} ${user.lastName}",
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = user.phone,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}