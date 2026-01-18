package com.fadymarty.matule.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fadymarty.matule.R
import com.fadymarty.matule.presentation.components.LoadingScreen
import com.fadymarty.matule.common.util.ObserveAsEvents
import com.fadymarty.ui_kit.common.theme.MatuleTheme
import com.fadymarty.ui_kit.presentation.components.buttons.BigButton
import com.fadymarty.ui_kit.presentation.components.buttons.LoginButton
import com.fadymarty.ui_kit.presentation.components.input.Input
import com.fadymarty.ui_kit.presentation.components.input.PasswordInput
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginRoot(
    onNavigateToRegister: () -> Unit,
    onNavigateToCreatePin: () -> Unit,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            LoginEvent.NavigateToRegister -> onNavigateToRegister()
            LoginEvent.NavigateToCreatePin -> onNavigateToCreatePin()
            else -> Unit
        }
    }

    LoginScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun LoginScreen(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
) {
    var isPasswordVisible by rememberSaveable {
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
                        text = "Добро пожаловать!",
                        style = MatuleTheme.typography.title1Semibold
                    )
                }
                Spacer(Modifier.height(23.dp))
                Text(
                    text = "Войдите, чтобы пользоваться функциями приложения",
                    style = MatuleTheme.typography.textRegular
                )
                Spacer(Modifier.height(64.dp))
                Input(
                    value = state.email,
                    onValueChange = {
                        onEvent(LoginEvent.EmailChanged(it))
                    },
                    label = "Вход по E-mail",
                    hint = "example@mail.com",
                    error = if (!state.isEmailValid) {
                        "Введите корректный email"
                    } else null
                )
                Spacer(Modifier.height(14.dp))
                PasswordInput(
                    value = state.password,
                    onValueChange = {
                        onEvent(LoginEvent.PasswordChanged(it))
                    },
                    label = "Пароль",
                    isVisible = isPasswordVisible,
                    onTrailingIconClick = {
                        isPasswordVisible = !isPasswordVisible
                    }
                )
                Spacer(Modifier.height(14.dp))
                BigButton(
                    label = "Далее",
                    onClick = {
                        onEvent(LoginEvent.Login)
                    },
                    enabled = state.email.isNotBlank() && state.password.isNotBlank()
                )
                Spacer(Modifier.height(15.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = null,
                            indication = null
                        ) {
                            onEvent(LoginEvent.NavigateToRegister)
                        },
                    text = "Зарегистрироваться",
                    style = MatuleTheme.typography.textRegular,
                    color = MatuleTheme.colorScheme.accent,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.weight(1f))
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Или войдите с помощью",
                        style = MatuleTheme.typography.textRegular,
                        color = MatuleTheme.colorScheme.placeholder,
                        textAlign = TextAlign.Center
                    )
                    LoginButton(
                        label = "Войти с VK",
                        icon = R.drawable.ic_vk,
                        onClick = {}
                    )
                    LoginButton(
                        label = "Войти с Yandex",
                        icon = R.drawable.ic_yandex,
                        onClick = {}
                    )
                    Spacer(Modifier.height(56.dp))
                }
            }
        }
    }
}