package com.fadymarty.matule.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.matule.common.util.Constants
import com.fadymarty.matule.common.util.SnackbarController
import com.fadymarty.matule.common.util.SnackbarEvent
import com.fadymarty.matule.domain.use_case.validation.ValidateEmailUseCase
import com.fadymarty.matule.domain.use_case.validation.ValidatePasswordConfirmUseCase
import com.fadymarty.matule.domain.use_case.validation.ValidatePasswordUseCase
import com.fadymarty.network.domain.model.User
import com.fadymarty.network.domain.use_case.user.RegisterUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validatePasswordConfirmUseCase: ValidatePasswordConfirmUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<RegisterEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.FirstNameChanged -> {
                _state.update { it.copy(firstName = event.firstName) }
            }

            is RegisterEvent.SecondNameChanged -> {
                _state.update { it.copy(secondName = event.secondName) }
            }

            is RegisterEvent.LastNameChanged -> {
                _state.update { it.copy(lastName = event.lastName) }
            }

            is RegisterEvent.DateBirthDayChanged -> {
                _state.update { it.copy(dateBirthday = event.dateBirthday) }
            }

            is RegisterEvent.GenderSelected -> {
                _state.update { it.copy(gender = event.gender) }
            }

            is RegisterEvent.EmailChanged -> {
                _state.update { it.copy(email = event.email) }
            }

            is RegisterEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.password) }
            }

            is RegisterEvent.ConfirmPasswordChanged -> {
                _state.update { it.copy(passwordConfirm = event.confirmPassword) }
            }

            is RegisterEvent.Submit -> {
                submit()
            }

            is RegisterEvent.Register -> {
                register()
            }

            else -> Unit
        }
    }

    private fun submit() {
        val isEmailValid = validateEmailUseCase(_state.value.email)
        _state.update { it.copy(isEmailValid = isEmailValid) }
        if (!isEmailValid) {
            return
        }
        viewModelScope.launch {
            eventChannel.send(RegisterEvent.NavigateToPassword)
        }
    }

    private fun register() {
        val isPasswordValid = validatePasswordUseCase(_state.value.password)
        val isPasswordConfirmValid = validatePasswordConfirmUseCase(
            password = _state.value.password,
            passwordConfirm = _state.value.passwordConfirm
        )
        _state.update {
            it.copy(
                isPasswordValid = isPasswordValid,
                isPasswordConfirmValid = isPasswordConfirmValid
            )
        }
        if (!isPasswordValid || !isPasswordConfirmValid) {
            return
        }
        val user = User(
            firstName = _state.value.firstName,
            lastName = _state.value.lastName,
            secondName = _state.value.secondName,
            dateBirthday = _state.value.dateBirthday,
            gender = _state.value.gender!!,
            email = _state.value.email,
            password = _state.value.password,
            passwordConfirm = _state.value.passwordConfirm
        )

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            registerUseCase(user)
                .onSuccess {
                    eventChannel.send(RegisterEvent.NavigateToCreatePin)
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(Constants.ERROR_MESSAGE)
                    )
                }
        }
    }
}