package com.albertorusso.randomuser.presentation.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.albertorusso.randomuser.domain.model.User
import org.koin.androidx.compose.get

@Composable
fun UserDetailScreen(email: String) {
    val userDetailViewModel: UserDetailViewModel = get()

    val user = userDetailViewModel.user.collectAsState().value
    val isLoading = userDetailViewModel.loadingState.collectAsState().value
    val errorMessage = userDetailViewModel.errorState.collectAsState().value

    LaunchedEffect(email) {
        userDetailViewModel.getUser(email)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            errorMessage != null -> {
                Text(
                    text = errorMessage ?: "",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            user != null -> {
                UserDetailContent(user = user)
            }
        }
    }
}

@Composable
fun UserDetailContent(user: User) {
    // Main layout for the user details
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = user.pictureBig),
            contentDescription = "User Profile Picture",
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .border(5.dp, Color.Gray, CircleShape)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Displaying user's name and surname
        Text(
            text = "Hi, My name is\n${user.firstName} ${user.lastName}",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Displaying user's gender
        Text(
            text = "Gender: ${user.gender}",
            style = MaterialTheme.typography.bodyLarge
        )

        // Displaying user's location (Street, City, State)
        Text(
            text = "Location: ${user.locationStreet}, ${user.locationCity}, ${user.locationState}",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Displaying user's email
        Text(
            text = "Email: ${user.email}",
            style = MaterialTheme.typography.bodyLarge
        )

        // Displaying user's registered date
        Text(
            text = "Registered on: ${user.registeredDate}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}