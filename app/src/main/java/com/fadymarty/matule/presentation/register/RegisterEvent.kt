package com.fadymarty.matule.presentation.register

sealed interface RegisterEvent {
    data class FirstNameChanged(val firstName: String) : RegisterEvent
    data class SecondNameChanged(val secondName: String) : RegisterEvent
    data class LastNameChanged(val lastName: String) : RegisterEvent
    data class DateBirthDayChanged(val dateBirthday: String) : RegisterEvent
    data class GenderSelected(val gender: String) : RegisterEvent
    data class EmailChanged(val email: String) : RegisterEvent
    data class PasswordChanged(val password: String) : RegisterEvent
    data class ConfirmPasswordChanged(val confirmPassword: String) : RegisterEvent
    data object Submit : RegisterEvent
    data object Register : RegisterEvent
    data object NavigateToPassword : RegisterEvent
    data object NavigateToCreatePin : RegisterEvent
}