package com.fadymarty.matule.presentation.pin.enter_pin

data class EnterPinState(
    val correctPin: String? = null,
    val pin: String = "",
    val selectedNumber: Int? = null
)
