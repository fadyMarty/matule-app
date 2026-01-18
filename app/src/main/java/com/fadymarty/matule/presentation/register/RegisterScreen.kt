package com.fadymarty.matule.presentation.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fadymarty.matule.common.util.ObserveAsEvents
import com.fadymarty.ui_kit.common.theme.MatuleTheme
import com.fadymarty.ui_kit.presentation.components.buttons.BigButton
import com.fadymarty.ui_kit.presentation.components.input.Input
import com.fadymarty.ui_kit.presentation.components.select.Select
import com.fadymarty.ui_kit.presentation.components.select.SelectItem

@Composable
fun RegisterRoot(
    onNavigateToPassword: () -> Unit,
    viewModel: RegisterViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is RegisterEvent.NavigateToPassword -> onNavigateToPassword()
            else -> Unit
        }
    }

    RegisterScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun RegisterScreen(
    state: RegisterState,
    onEvent: (RegisterEvent) -> Unit,
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(32.dp))
            Text(
                text = "Создание Профиля",
                style = MatuleTheme.typography.title1ExtraBold
            )
            Spacer(Modifier.height(44.dp))
            Text(
                text = "Без профиля вы не сможете создавать проекты.",
                style = MatuleTheme.typography.captionRegular,
                color = MatuleTheme.colorScheme.placeholder
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "В профиле будут храниться результаты проектов и ваши описания.",
                style = MatuleTheme.typography.captionRegular,
                color = MatuleTheme.colorScheme.placeholder
            )
            Spacer(Modifier.height(32.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Input(
                    value = state.firstName,
                    onValueChange = {
                        onEvent(RegisterEvent.FirstNameChanged(it))
                    },
                    hint = "Имя"
                )
                Input(
                    value = state.secondName,
                    onValueChange = {
                        onEvent(RegisterEvent.SecondNameChanged(it))
                    },
                    hint = "Отчество"
                )
                Input(
                    value = state.lastName,
                    onValueChange = {
                        onEvent(RegisterEvent.LastNameChanged(it))
                    },
                    hint = "Фамилия"
                )
                Input(
                    value = state.dateBirthday,
                    onValueChange = {
                        onEvent(RegisterEvent.DateBirthDayChanged(it))
                    },
                    hint = "Дата рождения"
                )
                Select(
                    items = listOf(
                        SelectItem("Мужской"),
                        SelectItem("Женский")
                    ),
                    selectedItemLabel = state.gender,
                    onItemClick = {
                        onEvent(RegisterEvent.GenderSelected(it.label))
                    },
                    hint = "Пол"
                )
                Input(
                    value = state.email,
                    onValueChange = {
                        onEvent(RegisterEvent.EmailChanged(it))
                    },
                    hint = "Почта",
                    error = if (!state.isEmailValid) {
                        "Введите корректный email"
                    } else null
                )
            }
            Spacer(Modifier.weight(1f))
            BigButton(
                label = "Далее",
                onClick = {
                    onEvent(RegisterEvent.Submit)
                },
                enabled = state.firstName.isNotBlank() &&
                        state.secondName.isNotBlank() &&
                        state.lastName.isNotBlank() &&
                        state.dateBirthday.isNotBlank() &&
                        state.gender != null &&
                        state.email.isNotBlank()
            )
            Spacer(Modifier.height(32.dp))
        }
    }
}