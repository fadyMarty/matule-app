package com.fadymarty.matule.presentation.enter_pin

data class EnterPinState(
    val correctPin: String? = null,
    val pin: String = "",
    val selectedNumber: Int? = null,
)
