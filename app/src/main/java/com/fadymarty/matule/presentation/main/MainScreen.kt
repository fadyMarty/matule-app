package com.fadymarty.matule.presentation.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fadymarty.matule.R
import com.fadymarty.matule.presentation.catalog.CatalogRoot
import com.fadymarty.matule.presentation.home.HomeRoot
import com.fadymarty.matule.presentation.navigation.Route
import com.fadymarty.matule.presentation.profile.ProfileRoot
import com.fadymarty.matule.presentation.project.ProjectRoot
import com.fadymarty.matule.presentation.projects.ProjectsRoot
import com.fadymarty.matule.common.util.MainSnackbarController
import com.fadymarty.matule.common.util.ObserveAsEvents
import com.fadymarty.ui_kit.presentation.components.snack_bar.SnackBar
import com.fadymarty.ui_kit.presentation.components.tab_bar.TabBar
import com.fadymarty.ui_kit.presentation.components.tab_bar.TabBarItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    rootNavController: NavHostController,
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    ObserveAsEvents(
        flow = MainSnackbarController.events,
        key1 = snackbarHostState
    ) { event ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()

            val job = launch {
                snackbarHostState.showSnackbar(
                    message = event.message,
                    duration = SnackbarDuration.Indefinite
                )
            }
            delay(5000)
            job.cancel()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            ) {
                SnackBar(
                    modifier = Modifier.padding(start = 20.dp, end = 8.dp),
                    message = it.visuals.message,
                    onDismiss = {
                        it.dismiss()
                    },
                )
            }
        },
        bottomBar = {
            TabBar(
                items = listOf(
                    TabBarItem(
                        icon = R.drawable.ic_home,
                        label = "Главная",
                        route = Route.Home
                    ),
                    TabBarItem(
                        icon = R.drawable.ic_catalog,
                        label = "Каталог",
                        route = Route.Catalog
                    ),
                    TabBarItem(
                        icon = R.drawable.ic_projects,
                        label = "Проекты",
                        iconSize = 24.dp,
                        iconPadding = PaddingValues(top = 5.dp, bottom = 3.dp),
                        route = Route.Projects
                    ),
                    TabBarItem(
                        icon = R.drawable.ic_profile,
                        label = "Профиль",
                        route = Route.Profile
                    )
                ),
                currentRoute = currentRoute,
                onItemClick = { item ->
                    navigateToTab(navController, item.route)

                }
            )
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(
                bottom = innerPadding.calculateBottomPadding()
            ),
            navController = navController,
            startDestination = Route.Home
        ) {
            composable<Route.Home> {
                HomeRoot()
            }
            composable<Route.Catalog> {
                CatalogRoot(
                    onNavigateToProfile = {
                        navigateToTab(navController, Route.Profile)
                    },
                    onNavigateToCart = {
                        rootNavController.navigate(Route.Cart)
                    }
                )
            }
            composable<Route.Projects> {
                ProjectsRoot(
                    onNavigateToProject = {
                        navController.navigate(Route.Project(it))
                    }
                )
            }
            composable<Route.Project> {
                ProjectRoot(
                    onNavigateBack = {
                        navController.navigateUp()
                    }
                )
            }
            composable<Route.Profile> {
                ProfileRoot(
                    onNavigateToLogin = {
                        rootNavController.navigate(Route.Login) {
                            popUpTo(Route.Main) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}

private fun navigateToTab(navController: NavController, route: Any) {
    navController.navigate(route) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}