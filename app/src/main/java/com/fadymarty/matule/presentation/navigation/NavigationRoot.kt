package com.fadymarty.matule.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.fadymarty.matule.presentation.cart.CartRoot
import com.fadymarty.matule.presentation.create_pin.CreatePinRoot
import com.fadymarty.matule.presentation.enter_pin.EnterPinRoot
import com.fadymarty.matule.presentation.login.LoginRoot
import com.fadymarty.matule.presentation.main.MainScreen
import com.fadymarty.matule.presentation.register.PasswordRoot
import com.fadymarty.matule.presentation.register.RegisterRoot
import com.fadymarty.matule.presentation.splash.SplashRoot
import com.fadymarty.matule.common.util.ObserveAsEvents
import com.fadymarty.matule.common.util.SnackbarController
import com.fadymarty.ui_kit.presentation.components.snack_bar.SnackBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.sharedKoinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationRoot() {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val navController = rememberNavController()

    ObserveAsEvents(
        flow = SnackbarController.events,
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
        }
    ) {
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
                            popUpTo(navController.graph.id) {
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
                            popUpTo(navController.graph.id) {
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
}