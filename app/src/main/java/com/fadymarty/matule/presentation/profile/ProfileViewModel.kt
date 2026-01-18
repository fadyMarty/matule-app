package com.fadymarty.matule.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.matule.domain.use_case.settings.IsNotificationsEnabledUseCase
import com.fadymarty.matule.domain.use_case.settings.SetNotificationsEnabledUseCase
import com.fadymarty.matule.common.util.Constants
import com.fadymarty.matule.common.util.MainSnackbarController
import com.fadymarty.matule.common.util.SnackbarEvent
import com.fadymarty.network.domain.use_case.user.ClearSessionUseCase
import com.fadymarty.network.domain.use_case.user.GetCurrentUserUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val clearSessionUseCase: ClearSessionUseCase,
    private val setNotificationsEnabledUseCase: SetNotificationsEnabledUseCase,
    private val isNotificationsEnabledUseCase: IsNotificationsEnabledUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<ProfileEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        isNotificationsEnabled()
        getProfile()
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.ToggleNotificationsEnabled -> {
                _state.update {
                    it.copy(isNotificationsEnabled = !it.isNotificationsEnabled)
                }
                viewModelScope.launch {
                    setNotificationsEnabledUseCase(_state.value.isNotificationsEnabled)
                }
            }

            is ProfileEvent.Logout -> {
                viewModelScope.launch {
                    clearSessionUseCase()
                    eventChannel.send(ProfileEvent.NavigateToLogin)
                }
            }

            else -> Unit
        }
    }

    private fun getProfile() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getCurrentUserUseCase()
                .onSuccess { user ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            user = user
                        )
                    }
                }
                .onFailure {
                    MainSnackbarController.sendEvent(
                        event = SnackbarEvent(Constants.ERROR_MESSAGE)
                    )
                }
        }
    }

    private fun isNotificationsEnabled() {
        isNotificationsEnabledUseCase().onEach { enabled ->
            _state.update { it.copy(isNotificationsEnabled = enabled) }
        }.launchIn(viewModelScope)
    }
}