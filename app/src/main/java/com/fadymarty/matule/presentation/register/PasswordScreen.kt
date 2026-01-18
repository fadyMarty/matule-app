package com.fadymarty.matule.presentation.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fadymarty.matule.presentation.components.LoadingScreen
import com.fadymarty.matule.common.util.ObserveAsEvents
import com.fadymarty.ui_kit.common.theme.MatuleTheme
import com.fadymarty.ui_kit.presentation.components.buttons.BigButton
import com.fadymarty.ui_kit.presentation.components.input.PasswordInput

@Composable
fun PasswordRoot(
    onNavigateToCreatePin: () -> Unit,
    viewModel: RegisterViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is RegisterEvent.NavigateToCreatePin -> {
                onNavigateToCreatePin()
            }

            else -> Unit
        }
    }

    PasswordScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun PasswordScreen(
    state: RegisterState,
    onEvent: (RegisterEvent) -> Unit,
) {
    var isPasswordVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var isConfirmPasswordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold { innerPadding ->
        if (state.isLoading) {
            LoadingScreen(
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(59.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "✋",
                        fontSize = 32.sp
                    )
                    Text(
                        text = "Создание пароля",
                        style = MatuleTheme.typography.title1ExtraBold
                    )
                }
                Spacer(Modifier.height(23.dp))
                Text(
                    text = "Введите новый пароль",
                    style = MatuleTheme.typography.textRegular
                )
                Spacer(Modifier.height(90.dp))
                PasswordInput(
                    value = state.password,
                    onValueChange = {
                        onEvent(RegisterEvent.PasswordChanged(it))
                    },
                    label = "Новый Пароль",
                    isVisible = isPasswordVisible,
                    onTrailingIconClick = {
                        isPasswordVisible = !isPasswordVisible
                    },
                    error = if (!state.isPasswordValid) {
                        "Введите надежный пароль"
                    } else null
                )
                Spacer(Modifier.height(12.dp))
                PasswordInput(
                    value = state.passwordConfirm,
                    onValueChange = {
                        onEvent(RegisterEvent.ConfirmPasswordChanged(it))
                    },
                    label = "Повторите пароль",
                    isVisible = isConfirmPasswordVisible,
                    onTrailingIconClick = {
                        isConfirmPasswordVisible = !isConfirmPasswordVisible
                    },
                    error = if (!state.isPasswordValid) {
                        "Пароли не совпадают"
                    } else null
                )
                Spacer(Modifier.height(12.dp))
                BigButton(
                    label = "Сохранить",
                    onClick = {
                        onEvent(RegisterEvent.Register)
                    },
                    enabled = state.password.isNotBlank() && state.passwordConfirm.isNotBlank()
                )
            }
        }
    }
}