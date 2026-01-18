package com.fadymarty.matule.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.matule.common.util.Constants
import com.fadymarty.matule.common.util.SnackbarController
import com.fadymarty.matule.common.util.SnackbarEvent
import com.fadymarty.matule.domain.use_case.validation.ValidateEmailUseCase
import com.fadymarty.network.domain.use_case.user.LoginUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _state.update { it.copy(email = event.email) }
            }

            is LoginEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.password) }
            }

            is LoginEvent.NavigateToRegister -> {
                viewModelScope.launch {
                    eventChannel.send(LoginEvent.NavigateToRegister)
                }
            }

            is LoginEvent.Login -> {
                login()
            }

            else -> Unit
        }
    }

    private fun login() {
        val isEmailValid = validateEmailUseCase(_state.value.email)
        _state.update {
            it.copy(isEmailValid = isEmailValid)
        }
        if (!isEmailValid) {
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            loginUseCase(_state.value.email, _state.value.password)
                .onSuccess {
                    eventChannel.send(LoginEvent.NavigateToCreatePin)
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