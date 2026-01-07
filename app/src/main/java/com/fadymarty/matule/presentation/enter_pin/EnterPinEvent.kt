package com.fadymarty.matule.presentation.enter_pin

sealed interface EnterPinEvent {
    data class NumberClicked(val number: Int) : EnterPinEvent
    data object RemoveLastNumber : EnterPinEvent
    data object NavigateToMainGraph : EnterPinEvent
}