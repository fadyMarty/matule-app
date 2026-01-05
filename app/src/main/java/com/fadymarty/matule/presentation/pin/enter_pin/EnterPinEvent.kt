package com.fadymarty.matule.presentation.pin.enter_pin

sealed interface EnterPinEvent {
    data class NumberClicked(val number: Int) : EnterPinEvent
    data object RemoveLastNumber : EnterPinEvent
    data object NavigateToMainGraph : EnterPinEvent
}