package com.fadymarty.matule.presentation.pin.create_pin

sealed interface CreatePinEvent {
    data class NumberClicked(val number: Int) : CreatePinEvent
    data object RemoveLastNumber : CreatePinEvent
    data object NavigateToMainGraph : CreatePinEvent
}