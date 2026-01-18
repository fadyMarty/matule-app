package com.fadymarty.matule.presentation.create_pin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fadymarty.matule.presentation.components.PinScreen
import com.fadymarty.matule.common.util.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreatePinRoot(
    onNavigateToMainGraph: () -> Unit,
    viewModel: CreatePinViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            CreatePinEvent.NavigateToMainGraph -> {
                onNavigateToMainGraph()
            }

            else -> Unit
        }
    }

    CreatePinScreen(
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@Composable
private fun CreatePinScreen(
    state: CreatePinState,
    onEvent: (CreatePinEvent) -> Unit,
) {
    PinScreen(
        pin = state.pin,
        onNumberClick = { number ->
            onEvent(CreatePinEvent.NumberClicked(number))
        },
        onRemoveLastClick = {
            onEvent(CreatePinEvent.RemoveLastNumber)
        },
        selectedNumber = state.selectedNumber,
        title = "Создайте пароль",
        subtitle = "Для защиты ваших персональных данных"
    )
}