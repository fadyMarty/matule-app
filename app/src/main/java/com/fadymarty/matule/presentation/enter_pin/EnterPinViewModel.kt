package com.fadymarty.matule.presentation.enter_pin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.network.domain.use_case.user.GetPinUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EnterPinViewModel(
    private val getPinUseCase: GetPinUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(EnterPinState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<EnterPinEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        getPinUseCase().onEach { pin ->
            _state.update { it.copy(correctPin = pin) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: EnterPinEvent) {
        when (event) {
            is EnterPinEvent.NumberClicked -> {
                _state.update {
                    it.copy(
                        pin = it.pin + event.number,
                        selectedNumber = event.number
                    )
                }
                if (_state.value.pin.length == 4) {
                    if (_state.value.pin == _state.value.correctPin) {
                        viewModelScope.launch {
                            eventChannel.send(EnterPinEvent.NavigateToMainGraph)
                        }
                    } else {
                        _state.update {
                            it.copy(
                                pin = "",
                                selectedNumber = null
                            )
                        }
                    }
                }
            }

            EnterPinEvent.RemoveLastNumber -> {
                _state.update {
                    it.copy(
                        pin = it.pin.dropLast(1),
                        selectedNumber = null
                    )
                }
            }

            else -> Unit
        }
    }
}