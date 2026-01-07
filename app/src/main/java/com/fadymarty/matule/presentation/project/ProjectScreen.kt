package com.fadymarty.matule.presentation.project

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.fadymarty.matule_ui_kit.common.theme.MatuleTheme
import com.fadymarty.matule_ui_kit.presentation.components.buttons.BigButton
import com.fadymarty.matule_ui_kit.presentation.components.header.SmallHeader
import com.fadymarty.matule_ui_kit.presentation.components.input.Input
import com.fadymarty.matule_ui_kit.presentation.components.modal.Modal
import com.fadymarty.matule_ui_kit.presentation.components.select.Select
import com.fadymarty.matule_ui_kit.presentation.components.select.SelectItem
import com.fadymarty.matule_ui_kit.presentation.components.snack_bar.SnackBar
import io.github.ismoy.imagepickerkmp.domain.config.CameraCaptureConfig
import io.github.ismoy.imagepickerkmp.domain.config.ImagePickerConfig
import io.github.ismoy.imagepickerkmp.domain.config.PermissionAndConfirmationConfig
import io.github.ismoy.imagepickerkmp.presentation.ui.components.GalleryPickerLauncher
import io.github.ismoy.imagepickerkmp.presentation.ui.components.ImagePickerLauncher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun ProjectRoot(
    onNavigateToProjects: () -> Unit,
    viewModel: ProjectViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ProjectEvent.NavigateToProjects -> {
                    onNavigateToProjects()
                }

                ProjectEvent.ShowErrorSnackBar -> {
                    val job = launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.error_message),
                            duration = SnackbarDuration.Indefinite
                        )
                    }
                    delay(5000)
                    job.cancel()
                }

                else -> Unit
            }
        }
    }

    ProjectScreen(
        state = state,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState,
    )
}

@Composable
fun ProjectScreen(
    state: ProjectState,
    onEvent: (ProjectEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val context = LocalContext.current

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            ) {
                SnackBar(
                    modifier = Modifier.padding(start = 20.dp, end = 8.dp),
                    message = it.visuals.message,
                    onDismiss = {
                        it.dismiss()
                    }
                )
            }
        },
        topBar = {
            SmallHeader(
                modifier = Modifier
                    .statusBarsPadding()
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
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding()),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    top = 13.dp,
                    end = 20.dp,
                    bottom = 15.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Select(
                        items = emptyList(),
                        selectedItemLabel = null,
                        onItemClick = {},
                        label = "Тип",
                        hint = "Выберите  тип"
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
                        hint = "Введите имя"
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
                        hint = "--.--.----"
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
                        hint = "--.--.----"
                    )
                    Spacer(Modifier.height(10.dp))
                }
                item {
                    Select(
                        items = emptyList(),
                        selectedItemLabel = null,
                        onItemClick = {},
                        label = "Кому",
                        hint = "Выберите  кому"
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
                        hint = "example.com"
                    )
                    Spacer(Modifier.height(17.dp))
                }
                item {
                    Select(
                        items = listOf(
                            SelectItem("Категория 1"),
                            SelectItem("Категория 2"),
                            SelectItem("Категория 3")
                        ),
                        selectedItemLabel = null,
                        onItemClick = {
                            onEvent(ProjectEvent.CategorySelected(it.label))
                        },
                        label = "Категория",
                        hint = "Выберите  категорию"
                    )
                    Spacer(Modifier.height(37.dp))
                }
                item {
                    SubcomposeAsyncImage(
                        modifier = Modifier
                            .size(202.dp, 192.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                onEvent(ProjectEvent.ShowImagePickerModal)
                            },
                        model = ImageRequest.Builder(context)
                            .data(state.project.image)
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
                    Spacer(Modifier.height(32.dp))
                    BigButton(
                        label = "Подтвердить",
                        onClick = {
                            onEvent(ProjectEvent.CreateProject)
                        },
                        enabled = state.project.title.isNotEmpty()
                                && state.project.dateStart.isNotEmpty()
                                && state.project.dateEnd.isNotEmpty()
                                && state.project.descriptionSource.isNotEmpty()
                                && state.project.category.isNotEmpty()
                                && state.project.image.isNotEmpty()
                    )
                }
            }
        }
    }

    if (state.showImagePickerModal) {
        Modal(
            onDismissRequest = {
                onEvent(ProjectEvent.HideImagePickerModal)
            },
            title = "Загрузка фото"
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onEvent(ProjectEvent.ShowGallery)
                        }
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    text = "Галерея",
                    style = MatuleTheme.typography.headlineRegular
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onEvent(ProjectEvent.ShowCamera)
                        }
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    text = "Камера",
                    style = MatuleTheme.typography.headlineRegular
                )
            }
        }
    }

    if (state.showGallery) {
        GalleryPickerLauncher(
            onPhotosSelected = { photos ->
                onEvent(ProjectEvent.ImageSelected(photos.first().uri))
            },
            onError = {
                onEvent(ProjectEvent.HideGallery)
            },
            onDismiss = {
                onEvent(ProjectEvent.HideGallery)
            }
        )
    }

    if (state.showCamera) {
        ImagePickerLauncher(
            config = ImagePickerConfig(
                onPhotoCaptured = { photo ->
                    onEvent(ProjectEvent.ImageSelected(photo.uri))
                },
                onError = {
                    onEvent(ProjectEvent.HideCamera)
                },
                onDismiss = {
                    onEvent(ProjectEvent.HideCamera)
                },
                cameraCaptureConfig = CameraCaptureConfig(
                    permissionAndConfirmationConfig = PermissionAndConfirmationConfig(
                        skipConfirmation = true
                    )
                )
            )
        )
    }
}