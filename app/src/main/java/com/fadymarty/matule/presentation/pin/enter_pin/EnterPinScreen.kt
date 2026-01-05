package com.fadymarty.matule.presentation.pin.enter_pin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fadymarty.matule.presentation.pin.components.PinScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EnterPinRoot(
    onNavigateToMainGraph: () -> Unit,
    viewModel: EnterPinViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                EnterPinEvent.NavigateToMainGraph -> {
                    onNavigateToMainGraph()
                }

                else -> Unit
            }
        }
    }

    EnterPinScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun EnterPinScreen(
    state: EnterPinState,
    onEvent: (EnterPinEvent) -> Unit
) {
    PinScreen(
        pin = state.pin,
        onNumberClick = {
            onEvent(EnterPinEvent.NumberClicked(it))
        },
        onRemoveLastClick = {
            onEvent(EnterPinEvent.RemoveLastNumber)
        },
        selectedNumber = state.selectedNumber,
        title = "Вход"
    )
}