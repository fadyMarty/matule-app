package com.fadymarty.matule.presentation.project.create_project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fadymarty.matule.R
import com.fadymarty.matule_ui_kit.common.theme.MatuleTheme
import com.fadymarty.matule_ui_kit.presentation.components.buttons.BigButton
import com.fadymarty.matule_ui_kit.presentation.components.header.SmallHeader
import com.fadymarty.matule_ui_kit.presentation.components.input.Input
import com.fadymarty.matule_ui_kit.presentation.components.select.Select
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateProjectRoot(
    viewsModel: CreateProjectViewModel = koinViewModel(),
) {
    val state by viewsModel.state.collectAsStateWithLifecycle()

    CreateProjectScreen(
        state = state
    )
}

@Composable
private fun CreateProjectScreen(
    state: CreateProjectState,
) {
    Scaffold(
        topBar = {
            SmallHeader(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = 28.dp),
                title = "Создать проект"
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding()
                ),
            contentPadding = PaddingValues(
                start = 20.dp,
                top = 13.dp,
                end = 20.dp,
                bottom = 15.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Select(
                    items = listOf(),
                    selectedItem = state.type,
                    onItemClick = {},
                    hint = "Выберите тип",
                    label = "Тип"
                )
            }
            item {
                Input(
                    value = state.title,
                    onValueChange = {},
                    hint = "Введите имя",
                    label = "Название проекта"
                )
            }
            item {
                Input(
                    value = state.dateStart,
                    onValueChange = {},
                    hint = "--.--.----",
                    label = "Дата начала"
                )
            }
            item {
                Input(
                    value = state.dateEnd,
                    onValueChange = {},
                    hint = "--.--.----",
                    label = "Дата Окончания"
                )
            }
            item {
                Select(
                    items = listOf(),
                    selectedItem = state.userId,
                    onItemClick = {},
                    hint = "Выберите кому",
                    label = "Кому"
                )
            }
            item {
                Input(
                    value = state.descriptionSource,
                    onValueChange = {},
                    label = "Источник описания",
                    hint = "example.com"
                )
            }
            item {
                Select(
                    items = listOf(),
                    selectedItem = state.category,
                    onItemClick = {},
                    hint = "Выберите  категорию",
                    label = "Категория"
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .padding(top = 21.dp, bottom = 16.dp)
                        .size(202.dp, 192.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MatuleTheme.colorScheme.inputBg)
                        .clickable {

                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(80.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_select),
                        contentDescription = null,
                        tint = MatuleTheme.colorScheme.description
                    )
                }
            }
            item {
                BigButton(
                    label = "Подтвердить",
                    onClick = {}
                )
            }
        }
    }
}