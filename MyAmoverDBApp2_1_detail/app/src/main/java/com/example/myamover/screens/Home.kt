package com.example.myamover.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.myamover.model.LoginViewModel
import com.example.myamover.navigation.NavRoutes
import com.example.myamover.ui.theme.MyAmoverTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    email: String,
    name: String,
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
    onOpenTasksClick: () -> Unit,
    onBackClick: () -> Unit,
    onOpenHistoryClick: () -> Unit,
    navController: NavController,
) {
    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
        }

        WindowWidthSizeClass.Medium -> {
        }

        WindowWidthSizeClass.Expanded -> {
        }

        else -> {
        }
    }

    val loginViewModel: LoginViewModel = viewModel()


    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp)
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Welcome, Amover",
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier
                .padding(40.dp)
                .align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(60.dp))


        //TASKS
        Button(
            onClick = onOpenTasksClick,
            modifier = modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 40.dp)
                .shadow(10.dp),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),

            ) {
            Text(
                text = "Tasks",
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier.padding(8.dp)
            )

        }

        Spacer(modifier = Modifier.height(40.dp))


        //History
        Button(
            onClick = onOpenHistoryClick,
            modifier = modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 40.dp)
                .shadow(10.dp),  // mais alto que o normal
            shape = MaterialTheme.shapes.large, // usa o shape do seu Shape.kt
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = "History",
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier.padding(8.dp)
            )

        }
        Spacer(modifier = Modifier.height(40.dp))


        //Logouut
        Button(
            onClick = {
                loginViewModel.logout()
                // 2) limpa navegação e volta ao Login
                navController.navigate(NavRoutes.Login) {
                    popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                    launchSingleTop = true
                }
            },

            modifier = modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 40.dp)
                .shadow(10.dp),   // mais alto que o normal
            shape = MaterialTheme.shapes.large, // usa o shape do seu Shape.kt
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = "Logout",
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier.padding(8.dp)
            )

        }
    }
}


@Composable
fun TopBar(email: String) {
    TODO("Not yet implemented")
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyAmoverTheme {
        MyAmoverTheme() {
            HomeScreen(
                windowSize = WindowWidthSizeClass.Compact,
                name = "Amover",
                modifier = Modifier,
                onOpenTasksClick = {},
                email = "email",
                onBackClick = TODO(),
                onOpenHistoryClick = {
                    TODO()
                },
                navController = TODO()
            )

        }
    }
}
