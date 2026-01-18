package com.fadymarty.matule.presentation.project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.fadymarty.matule.R
import com.fadymarty.matule.presentation.components.LoadingScreen
import com.fadymarty.matule.common.util.ObserveAsEvents
import com.fadymarty.network.data.remote.MatuleApi
import com.fadymarty.network.domain.model.User
import com.fadymarty.ui_kit.common.theme.MatuleTheme
import com.fadymarty.ui_kit.presentation.components.buttons.BigButton
import com.fadymarty.ui_kit.presentation.components.header.SmallHeader
import com.fadymarty.ui_kit.presentation.components.input.Input
import com.fadymarty.ui_kit.presentation.components.modal.Modal
import com.fadymarty.ui_kit.presentation.components.select.Select
import com.fadymarty.ui_kit.presentation.components.select.SelectItem
import io.appmetrica.analytics.AppMetrica
import io.github.ismoy.imagepickerkmp.domain.config.CameraCaptureConfig
import io.github.ismoy.imagepickerkmp.domain.config.ImagePickerConfig
import io.github.ismoy.imagepickerkmp.domain.config.PermissionAndConfirmationConfig
import io.github.ismoy.imagepickerkmp.domain.extensions.loadBytes
import io.github.ismoy.imagepickerkmp.presentation.ui.components.GalleryPickerLauncher
import io.github.ismoy.imagepickerkmp.presentation.ui.components.ImagePickerLauncher
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProjectRoot(
    onNavigateBack: () -> Unit,
    viewModel: ProjectViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ProjectEvent.NavigateBack -> {
                onNavigateBack()
            }

            else -> Unit
        }
    }

    ProjectScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun ProjectScreen(
    state: ProjectState,
    onEvent: (ProjectEvent) -> Unit,
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            SmallHeader(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp)
                    .padding(top = 28.dp),
                title = "Создать проект"
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
                    .padding(
                        top = innerPadding.calculateTopPadding()
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(
                    start = 20.dp,
                    top = 13.dp,
                    end = 20.dp,
                    bottom = 15.dp
                )
            ) {
                item {
                    Select(
                        items = listOf(
                            SelectItem("Тип 1"),
                            SelectItem("Тип 2")
                        ),
                        selectedItemLabel = state.project.typeProject,
                        onItemClick = {
                            onEvent(ProjectEvent.TypeSelected(it.label))
                        },
                        label = "Тип"
                    )
                    Spacer(Modifier.height(16.dp))
                }
                item {
                    Input(
                        value = state.project.title,
                        onValueChange = {
                            onEvent(ProjectEvent.TitleChanged(it))
                        },
                        label = "Название проекта",
                        hint = "Введите имя",
                        keyboardActions = KeyboardActions(
                            onDone = {
                                AppMetrica.reportEvent("Title entered")
                            }
                        )
                    )
                    Spacer(Modifier.height(16.dp))
                }
                item {
                    Input(
                        value = state.project.dateStart,
                        onValueChange = {
                            onEvent(ProjectEvent.DateStartChanged(it))
                        },
                        label = "Дата начала",
                        hint = "--.--.----",
                        keyboardActions = KeyboardActions(
                            onDone = {
                                AppMetrica.reportEvent("Date start entered")
                            }
                        )
                    )
                    Spacer(Modifier.height(22.dp))
                }
                item {
                    Input(
                        value = state.project.dateEnd,
                        onValueChange = {
                            onEvent(ProjectEvent.DateEndChanged(it))
                        },
                        label = "Дата Окончания",
                        hint = "--.--.----",
                        keyboardActions = KeyboardActions(
                            onDone = {
                                AppMetrica.reportEvent("Date end entered")
                            }
                        )
                    )
                    Spacer(Modifier.height(10.dp))
                }
                item {
                    Select(
                        items = state.users.map {
                            SelectItem(getFullName(it))
                        },
                        selectedItemLabel = if (state.user != null) {
                            getFullName(state.user)
                        } else "Выберите  кому",
                        onItemClick = {
                            onEvent(ProjectEvent.UserSelected(it.label))
                        },
                        label = "Кому"
                    )
                    Spacer(Modifier.height(16.dp))
                }
                item {
                    Input(
                        value = state.project.descriptionSource,
                        onValueChange = {
                            onEvent(ProjectEvent.DescriptionSourceChanged(it))
                        },
                        label = "Источник описания",
                        hint = "example.com",
                        keyboardActions = KeyboardActions(
                            onDone = {
                                AppMetrica.reportEvent("Description source entered")
                            }
                        )
                    )
                    Spacer(Modifier.height(17.dp))
                }
                item {
                    Select(
                        items = listOf(
                            SelectItem("Категория 1"),
                            SelectItem("Категория 2")
                        ),
                        selectedItemLabel = state.project.category,
                        onItemClick = {
                            onEvent(ProjectEvent.CategorySelected(it.label))
                        },
                        label = "Категория"
                    )
                    Spacer(Modifier.height(37.dp))
                }
                item {
                    SubcomposeAsyncImage(
                        modifier = Modifier
                            .size(202.dp, 192.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable(state.project.id == null) {
                                onEvent(ProjectEvent.ShowImagePicker)
                            },
                        model = ImageRequest.Builder(context)
                            .data(
                                data = if (state.project.image != null) {
                                    "${MatuleApi.BASE_URL}/files/${state.project.collectionId}/${state.project.id}/${state.project.image}"
                                } else state.imageBytes
                            )
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        loading = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MatuleTheme.colorScheme.inputBg),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        },
                        error = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MatuleTheme.colorScheme.inputBg),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    modifier = Modifier.size(80.dp),
                                    painter = painterResource(R.drawable.ic_select),
                                    contentDescription = null,
                                    tint = MatuleTheme.colorScheme.description
                                )
                            }
                        }
                    )
                }
                if (state.project.id == null) {
                    item {
                        Spacer(Modifier.height(32.dp))
                        BigButton(
                            label = "Подтвердить",
                            onClick = {
                                onEvent(ProjectEvent.CreateProject)
                            },
                            enabled = state.project.typeProject != "Выберите тип"
                                    && state.project.title.isNotBlank()
                                    && state.project.dateStart.isNotBlank()
                                    && state.project.dateEnd.isNotBlank()
                                    && state.project.userId != null
                                    && state.project.descriptionSource.isNotBlank()
                                    && state.project.category != "Выберите  категорию"
                                    && state.imageBytes != null
                        )
                    }
                }
            }
        }
    }

    if (state.showGallery) {
        GalleryPickerLauncher(
            onPhotosSelected = { photos ->
                onEvent(ProjectEvent.ImageSelected(photos.first().loadBytes()))
            },
            onError = {
                onEvent(ProjectEvent.HideGalleryPicker)
            },
            onDismiss = {
                onEvent(ProjectEvent.HideGalleryPicker)
            }
        )
    }

    if (state.showCamera) {
        ImagePickerLauncher(
            config = ImagePickerConfig(
                onPhotoCaptured = { photo ->
                    onEvent(ProjectEvent.ImageSelected(photo.loadBytes()))
                    AppMetrica.reportEvent("Image selected")
                },
                onError = {
                    onEvent(ProjectEvent.HideCameraPicker)
                },
                onDismiss = {
                    onEvent(ProjectEvent.HideCameraPicker)
                },
                cameraCaptureConfig = CameraCaptureConfig(
                    permissionAndConfirmationConfig = PermissionAndConfirmationConfig(
                        skipConfirmation = true
                    )
                )
            )
        )
    }

    if (state.showImagePicker) {
        Modal(
            onDismissRequest = {
                onEvent(ProjectEvent.HideImagePicker)
            },
            title = "Выбор фотографии"
        ) {
            Spacer(Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(
                    top = 12.dp,
                    bottom = 20.dp
                )
            ) {
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onEvent(ProjectEvent.ShowGallery)
                            }
                            .padding(
                                horizontal = 20.dp,
                                vertical = 14.dp
                            ),
                        text = "Галерея",
                        style = MatuleTheme.typography.headlineRegular
                    )
                }
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onEvent(ProjectEvent.ShowCameraPicker)
                            }
                            .padding(
                                horizontal = 20.dp,
                                vertical = 14.dp
                            ),
                        text = "Камера",
                        style = MatuleTheme.typography.headlineRegular
                    )
                }
            }
        }
    }
}

fun getFullName(user: User): String {
    return "${user.firstName} ${user.lastName.first()}. ${user.secondName.first()}."
}