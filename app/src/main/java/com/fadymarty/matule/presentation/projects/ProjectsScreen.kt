package com.fadymarty.matule.presentation.projects

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fadymarty.matule.presentation.components.LoadingScreen
import com.fadymarty.matule.common.util.ObserveAsEvents
import com.fadymarty.ui_kit.common.theme.MatuleTheme
import com.fadymarty.ui_kit.presentation.components.cards.ProjectCard
import com.fadymarty.ui_kit.presentation.components.header.SmallHeader
import com.fadymarty.ui_kit.presentation.components.icons.MatuleIcons
import org.koin.compose.viewmodel.koinViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

@Composable
fun ProjectsRoot(
    onNavigateToProject: (String?) -> Unit,
    viewModel: ProjectsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ProjectsEvent.NavigateToProject -> {
                onNavigateToProject(event.id)
            }
        }
    }

    ProjectsScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun ProjectsScreen(
    state: ProjectsState,
    onEvent: (ProjectsEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            SmallHeader(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp)
                    .padding(top = 28.dp),
                title = "Проекты",
                trailingIcon = {
                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .clickable(
                                interactionSource = null,
                                indication = null
                            ) {
                                onEvent(ProjectsEvent.NavigateToProject())
                            },
                        imageVector = MatuleIcons.Plus,
                        contentDescription = null,
                        tint = MatuleTheme.colorScheme.inputIcon
                    )
                }
            )
        }
    ) { innerPadding ->
        if (state.isLoading) {
            LoadingScreen(
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding()
                )
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = innerPadding.calculateTopPadding()
                    ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(
                    horizontal = 20.dp,
                    vertical = 18.dp
                )
            ) {
                items(state.projects) { project ->
                    val normalizedDate = project.created?.replace(" ", "T")
                    val date = Instant.parse(normalizedDate)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    val today = LocalDate.now()

                    val days = ChronoUnit.DAYS.between(date, today)

                    val timeAgo = when {
                        days == 0L -> "Сегодня"
                        days % 100 in 11..14 -> "Прошло $days дней"
                        days % 10 == 1L -> "Прошел $days день"
                        days % 10 in 2..4 -> "Прошло $days дня"
                        else -> "Прошло $days дней"
                    }

                    ProjectCard(
                        title = project.title,
                        date = timeAgo,
                        onOpenClick = {
                            onEvent(ProjectsEvent.NavigateToProject(project.id))
                        }
                    )
                }
            }
        }
    }
}