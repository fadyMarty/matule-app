package com.fadymarty.matule.presentation.splash

import com.fadymarty.matule.presentation.navigation.Route

sealed interface SplashEvent {
    data class Navigate(val route: Route) : SplashEvent
}