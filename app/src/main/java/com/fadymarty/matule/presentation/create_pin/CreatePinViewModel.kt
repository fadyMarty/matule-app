package com.fadymarty.matule.presentation.create_pin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.network.domain.use_case.user.SavePinUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreatePinViewModel(
    private val savePinUseCase: SavePinUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CreatePinState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<CreatePinEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onEvent(event: CreatePinEvent) {
        when (event) {
            is CreatePinEvent.NumberClicked -> {
                _state.update {
                    it.copy(
                        pin = it.pin + event.number,
                        selectedNumber = event.number
                    )
                }
                if (_state.value.pin.length == 4) {
                    viewModelScope.launch {
                        savePinUseCase(_state.value.pin)
                        eventChannel.send(CreatePinEvent.NavigateToMainGraph)
                    }
                }
            }

            CreatePinEvent.RemoveLastNumber -> {
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