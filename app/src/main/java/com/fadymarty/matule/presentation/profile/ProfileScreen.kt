package com.fadymarty.matule.presentation.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fadymarty.matule.presentation.components.LoadingScreen
import com.fadymarty.matule.presentation.util.ObserveAsEvents
import com.fadymarty.matule_ui_kit.common.theme.MatuleTheme
import com.fadymarty.matule_ui_kit.presentation.components.controls.Toggle
import com.rajat.pdfviewer.PdfViewerActivity
import com.rajat.pdfviewer.util.saveTo
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileRoot(
    onNavigateToLogin: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ProfileEvent.NavigateToLogin -> onNavigateToLogin()
            else -> Unit
        }
    }

    ProfileScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun ProfileScreen(
    state: ProfileState,
    onEvent: (ProfileEvent) -> Unit,
) {
    val context = LocalContext.current

    Scaffold { innerPadding ->
        if (state.isLoading) {
            LoadingScreen(
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding()
                )
            )
        } else {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(
                        top = innerPadding.calculateTopPadding()
                    )
            ) {
                state.user?.let { user ->
                    Spacer(Modifier.height(32.dp))
                    Text(
                        text = user.firstName,
                        style = MatuleTheme.typography.title1ExtraBold
                    )
                    user.email?.let { email ->
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = email,
                            style = MatuleTheme.typography.headlineRegular,
                            color = MatuleTheme.colorScheme.placeholder
                        )
                    }
                    Spacer(Modifier.height(24.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Text(
                            text = "📋",
                            fontSize = 32.sp
                        )
                        Text(
                            text = "Мои заказы",
                            style = MatuleTheme.typography.title3Semibold
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .padding(end = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚙️",
                            fontSize = 32.sp
                        )
                        Spacer(Modifier.width(20.dp))
                        Text(
                            text = "Уведомления",
                            style = MatuleTheme.typography.title3Semibold
                        )
                        Spacer(Modifier.weight(1f))
                        Toggle(
                            checked = state.isNotificationsEnabled,
                            onClick = {
                                onEvent(ProfileEvent.ToggleNotificationsEnabled)
                            }
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            Text(
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = null,
                                        indication = null
                                    ) {
                                        context.startActivity(
                                            PdfViewerActivity.launchPdfFromUrl(
                                                context = context,
                                                pdfUrl = "https://www.back4app.com/terms-of-service.pdf",
                                                pdfTitle = null,
                                                saveTo = saveTo.DOWNLOADS
                                            )
                                        )
                                    },
                                text = "Политика конфиденциальности",
                                style = MatuleTheme.typography.textMedium,
                                color = MatuleTheme.colorScheme.placeholder
                            )
                            Text(
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = null,
                                        indication = null
                                    ) {
                                        context.startActivity(
                                            PdfViewerActivity.launchPdfFromUrl(
                                                context = context,
                                                pdfUrl = "https://www.back4app.com/terms-of-service.pdf",
                                                pdfTitle = null,
                                                saveTo = saveTo.DOWNLOADS
                                            )
                                        )
                                    },
                                text = "Пользовательское соглашение",
                                style = MatuleTheme.typography.textMedium,
                                color = MatuleTheme.colorScheme.placeholder
                            )
                            Text(
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = null,
                                        indication = null
                                    ) {
                                        onEvent(ProfileEvent.Logout)
                                    },
                                text = "Выход",
                                style = MatuleTheme.typography.textMedium,
                                color = MatuleTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}