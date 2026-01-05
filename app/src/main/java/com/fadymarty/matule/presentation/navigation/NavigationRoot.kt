package com.fadymarty.matule.presentation.navigation

import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.fadymarty.matule.presentation.cart.CartRoot
import com.fadymarty.matule.presentation.login.LoginRoot
import com.fadymarty.matule.presentation.main.MainScreen
import com.fadymarty.matule.presentation.pin.create_pin.CreatePinRoot
import com.fadymarty.matule.presentation.pin.enter_pin.EnterPinRoot
import com.fadymarty.matule.presentation.register.PasswordRoot
import com.fadymarty.matule.presentation.register.RegisterRoot
import com.fadymarty.matule.presentation.splash.SplashRoot
import org.koin.compose.viewmodel.sharedKoinViewModel

@Composable
fun NavigationRoot() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Splash
    ) {
        composable<Route.Splash>(
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = EaseOut
                    )
                )
            }
        ) {
            SplashRoot(
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Route.Splash) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<Route.Login> {
            LoginRoot(
                onNavigateToRegister = {
                    navController.navigate(Route.Register)
                },
                onNavigateToCreatePin = {
                    navController.navigate(Route.CreatePin) {
                        popUpTo(Route.Login) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        navigation<Route.RegisterGraph>(
            startDestination = Route.Register
        ) {
            composable<Route.Register> {
                RegisterRoot(
                    onNavigateToPassword = {
                        navController.navigate(Route.Password)
                    },
                    viewModel = it.sharedKoinViewModel(
                        navController = navController,
                        navGraphRoute = Route.RegisterGraph
                    )
                )
            }
            composable<Route.Password> {
                PasswordRoot(
                    onNavigateToCreatePin = {
                        navController.navigate(Route.CreatePin) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                    },
                    viewModel = it.sharedKoinViewModel(
                        navController = navController,
                        navGraphRoute = Route.RegisterGraph
                    )
                )
            }
        }
        composable<Route.CreatePin> {
            CreatePinRoot(
                onNavigateToMainGraph = {
                    navController.navigate(Route.MainGraph) {
                        popUpTo(Route.CreatePin) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<Route.EnterPin> {
            EnterPinRoot(
                onNavigateToMainGraph = {
                    navController.navigate(Route.MainGraph) {
                        popUpTo(Route.CreatePin) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        navigation<Route.MainGraph>(
            startDestination = Route.Main
        ) {
            composable<Route.Main> {
                MainScreen(
                    rootNavController = navController
                )
            }
        }
        composable<Route.Cart> {
            CartRoot(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToMainGraph = {
                    navController.navigate(Route.MainGraph) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
            )
        }
    }
}