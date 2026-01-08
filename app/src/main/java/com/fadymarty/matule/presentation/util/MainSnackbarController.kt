package com.fadymarty.matule.presentation.util

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object MainSnackbarController {

    private val eventChannel = Channel<SnackbarEvent>()
    val events = eventChannel.receiveAsFlow()

    suspend fun sendEvent(event: SnackbarEvent) {
        eventChannel.send(event)
    }
}