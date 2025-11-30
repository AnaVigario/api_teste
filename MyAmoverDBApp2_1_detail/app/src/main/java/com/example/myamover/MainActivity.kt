package com.example.myamover

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myamover.navigation.AppNavGraph
import com.example.myamover.navigation.NavRoutes
import com.example.myamover.navigation.NavigationItem
import com.example.myamover.ui.theme.MyAmoverTheme


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAmoverTheme {
                AppNavGraph(windowSize = calculateWindowSizeClass(this).widthSizeClass)
                //MainScreen()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavHostController,
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentBase = navBackStackEntry?.destination?.route
        ?.substringBefore("?")?.substringBefore("/{")
    val isHome = currentBase == NavRoutes.Home
    val canPop = navController.previousBackStackEntry != null
    val isHistory = currentBase == NavRoutes.History

    if (isHistory) {
        return
    }
    TopAppBar(
        modifier = modifier
            .padding(0.dp)
            .verticalScroll(rememberScrollState()),
        actions = actions,
        scrollBehavior = scrollBehavior,
        windowInsets = windowInsets,
        title = {
            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        navigationIcon = {
            when {
                isHome -> { // botão de logout na Home
                    IconButton(onClick = {
                        navController.navigate(NavRoutes.HomeWithArgs) {
//                            popUpTo(navController.graph.findStartDestination().id) {
//                                inclusive = true
//                            }
//                            launchSingleTop = true
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Login"
                        )
                    }
                }

                canPop -> { // seta de voltar nas outras telas
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }

                else -> { /* sem ícone */
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    )
}

private fun NavDestination?.baseRoute(): String? =
    this?.hierarchy?.firstNotNullOfOrNull { d ->
        d.route?.substringBefore("?")?.substringBefore("/{")
    }

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar(
        navController = rememberNavController(),
        title = "Title",
        subtitle = "Subtitle"
    )
}


@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    items: List<NavigationItem>
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            //val selected = currentDest?.hierarchy?.any { it.route == item.route } == true
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(text = item.title) },
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        topBar = { TopBar(
            navController = rememberNavController(),
            title = "Title",
            subtitle = "Subtitle"
        ) },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                items = listOf(
                    NavigationItem.Home,
                    NavigationItem.Tasks,
                    NavigationItem.Map,
                    NavigationItem.Perfil
                )
            )
        },
        content = { padding -> // We have to pass the scaffold inner padding to our content. That's why we use Box.
            Box(modifier = Modifier.padding(padding)) {
                /* Add code later */
            }
        },
        modifier = Modifier.background(MaterialTheme.colorScheme.background), // Set background color to avoid the white flashing when you switch between screens
    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}

