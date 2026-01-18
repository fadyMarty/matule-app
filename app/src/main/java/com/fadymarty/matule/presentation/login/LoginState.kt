package com.fadymarty.matule.presentation.login

data class LoginState(
    val isLoading: Boolean = false,
    val email: String = "",
    val isEmailValid: Boolean = true,
    val password: String = "",
)