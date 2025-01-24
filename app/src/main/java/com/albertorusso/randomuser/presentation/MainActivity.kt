package com.albertorusso.randomuser.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.albertorusso.randomuser.presentation.details.UserDetailScreen
import com.albertorusso.randomuser.presentation.list.UserListScreen
import com.albertorusso.randomuser.presentation.navigation.NavRoutes

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = NavRoutes.USER_LIST
            ) {
                composable(NavRoutes.USER_LIST) {
                    UserListScreen(
                        onUserClick = { user ->
                            navController.navigate("userDetail/${user.email}")
                        }
                    )
                }
                composable(
                    route = NavRoutes.USER_DETAIL,
                    arguments = listOf(navArgument(NavRoutes.EMAIL) { type = NavType.StringType })
                ) { backStackEntry ->
                    val email = backStackEntry.arguments?.getString(NavRoutes.EMAIL) ?: return@composable
                    UserDetailScreen(email = email)
                }
            }
        }
    }
}