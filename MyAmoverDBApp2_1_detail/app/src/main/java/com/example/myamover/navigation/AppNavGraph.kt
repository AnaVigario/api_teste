package com.example.myamover.navigation

//import com.example.myamover.screens.HistoryScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myamover.BottomNavigationBar
import com.example.myamover.TopBar
import com.example.myamover.screens.HistoryScreen
import com.example.myamover.screens.HomeScreen
import com.example.myamover.screens.LoginScreen
import com.example.myamover.screens.MapScreen
import com.example.myamover.screens.TasksScreen

//import com.example.myamover.screens.TasksScreen


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    // rotas que exibem barras (por enquanto só a Home; adicione outras quando criar)
    val routesWithBars = remember {
        setOf(
            NavRoutes.HomeWithArgs,
            NavRoutes.Tasks,
            NavRoutes.History,
            NavRoutes.Map
        )
    }
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val showBars = currentRoute in routesWithBars

    val title = when (currentRoute) {
        NavRoutes.HomeWithArgs -> "Home"
        NavRoutes.Tasks -> "Tasks"
        NavRoutes.History -> "History"
        NavRoutes.Map -> "Map"
        else -> ""
    }

    Scaffold(
        topBar = {
            if (showBars) TopBar(
                navController = navController,
                title = title,
            )
        },
        bottomBar = {
            if (showBars) {
                BottomNavigationBar(
                    navController = navController,
                    items = listOf(
                        NavigationItem.Home,   // add suas outras tabs quando existirem
                        NavigationItem.Tasks,
                        NavigationItem.History,
                        NavigationItem.Map,
                        // NavigationItem.Perfil
                    )
                )
            }
        },
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) { inner ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Login,
            modifier = modifier.padding(inner)
        ) {
            // LOGIN (sem barras)
            composable(NavRoutes.Login) {
                LoginScreen(
                    windowSize = windowSize,
                    navController = navController
                )
            }

            // HOME (com barras) — recebe o email
            composable(
                route = NavRoutes.HomeWithArgs,
                arguments = listOf(navArgument("email") { type = NavType.StringType })
            ) { backStackEntry ->
                val email = backStackEntry.arguments?.getString("email").orEmpty()
                HomeScreen(
                    name = "Amover",
                    email = email,
                    windowSize = windowSize,
                    navController = navController,
                    modifier = modifier,
                    onOpenTasksClick = { navController.navigate(NavRoutes.Tasks) },
                    onOpenHistoryClick = { navController.navigate(NavRoutes.History) },
                    onBackClick = { navController.popBackStack() },

                )
            }
            composable(NavRoutes.Tasks) {
                TasksScreen(
                    windowSize = windowSize,
                    modifier = modifier,
                    onOpenMapRoute = { navController.navigate(NavRoutes.Map) },
                    onViewTaskDetails = { navController.navigate(NavRoutes.ViewTaskDetail) },
                    onBackClick = { navController.popBackStack() },

                )
            }
            composable(NavRoutes.History) {
                HistoryScreen(
                    modifier = modifier,
                    onBack = { navController.popBackStack() },
                    onOpenMapRoute = { navController.popBackStack() },
                    navController = navController,

                    navigationIcon = {},
                )
            }
            composable(NavRoutes.Map) {
                MapScreen(
                    windowSize = windowSize,
                    modifier = modifier,
                )
            }

        }
    }
}

