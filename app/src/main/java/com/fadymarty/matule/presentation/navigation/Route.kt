package com.fadymarty.matule.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Splash : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object RegisterGraph : Route

    @Serializable
    data object Register : Route

    @Serializable
    data object Password : Route

    @Serializable
    data object CreatePin : Route

    @Serializable
    data object EnterPin : Route

    @Serializable
    data object MainGraph : Route

    @Serializable
    data object Main : Route

    @Serializable
    data object Home : Route

    @Serializable
    data object Catalog : Route

    @Serializable
    data object Cart : Route

    @Serializable
    data object Projects : Route

    @Serializable
    data class Project(val id: String?) : Route

    @Serializable
    data object Profile : Route
}