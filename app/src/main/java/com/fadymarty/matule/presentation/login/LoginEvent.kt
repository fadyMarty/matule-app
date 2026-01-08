package com.fadymarty.matule.presentation.login

sealed interface LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent
    data class PasswordChanged(val password: String) : LoginEvent
    data object Login : LoginEvent
    data object NavigateToRegister : LoginEvent
    data object NavigateToCreatePin : LoginEvent
}