package com.fadymarty.matule.presentation.project

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.fadymarty.matule.presentation.navigation.Route
import com.fadymarty.matule.presentation.util.Constants
import com.fadymarty.matule.presentation.util.MainSnackbarController
import com.fadymarty.matule.presentation.util.SnackbarEvent
import com.fadymarty.matule_network.domain.use_case.projects.CreateProjectUseCase
import com.fadymarty.matule_network.domain.use_case.projects.GetProjectByIdUseCase
import com.fadymarty.matule_network.domain.use_case.user.GetUsersUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProjectViewModel(
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val getUsersUseCase: GetUsersUseCase,
    private val createProjectUseCase: CreateProjectUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val project: Route.Project = savedStateHandle.toRoute()

    private val _state = MutableStateFlow(ProjectState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<ProjectEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            project.id?.let { id ->
                getProjectByIdUseCase(id)
                    .onSuccess { project ->
                        _state.update { it.copy(project = project) }
                    }
                    .onFailure {
                        MainSnackbarController.sendEvent(
                            event = SnackbarEvent(Constants.ERROR_MESSAGE)
                        )
                    }
            }
            getUsersUseCase()
                .onSuccess { users ->
                    _state.update {
                        it.copy(
                            users = users,
                            isLoading = false
                        )
                    }
                }
                .onFailure {
                    MainSnackbarController.sendEvent(
                        event = SnackbarEvent(Constants.ERROR_MESSAGE)
                    )
                }
        }
    }

    fun onEvent(event: ProjectEvent) {
        when (event) {
            is ProjectEvent.TypeSelected -> {
                if (project.id != null) return
                _state.update {
                    it.copy(
                        project = it.project.copy(
                            typeProject = event.type
                        )
                    )
                }
            }

            is ProjectEvent.TitleChanged -> {
                if (project.id != null) return
                _state.update {
                    it.copy(
                        project = it.project.copy(
                            title = event.title
                        )
                    )
                }
            }

            is ProjectEvent.DateStartChanged -> {
                if (project.id != null) return
                _state.update {
                    it.copy(
                        project = it.project.copy(
                            dateStart = event.dateStart
                        )
                    )
                }
            }

            is ProjectEvent.DateEndChanged -> {
                if (project.id != null) return
                _state.update {
                    it.copy(
                        project = it.project.copy(
                            dateEnd = event.dateEnd
                        )
                    )
                }
            }

            is ProjectEvent.UserSelected -> {
                if (project.id != null) return
                val user = _state.value.users.firstOrNull { user ->
                    user.email == event.email
                }
                _state.update {
                    it.copy(
                        user = user,
                        project = it.project.copy(
                            userId = user?.id
                        )
                    )
                }
            }

            is ProjectEvent.DescriptionSourceChanged -> {
                if (project.id != null) return
                _state.update {
                    it.copy(
                        project = it.project.copy(
                            descriptionSource = event.descriptionSource
                        )
                    )
                }
            }

            is ProjectEvent.CategorySelected -> {
                if (project.id != null) return
                _state.update {
                    it.copy(
                        project = it.project.copy(
                            category = event.category
                        )
                    )
                }
            }

            is ProjectEvent.ImageSelected -> {
                if (project.id != null) return
                _state.update {
                    it.copy(
                        project = it.project.copy(
                            image = event.uri,
                        ),
                        showGallery = false,
                        showCamera = false
                    )
                }
            }

            ProjectEvent.ShowImagePicker -> {
                _state.update { it.copy(showImagePicker = true) }
            }

            ProjectEvent.HideImagePicker -> {
                _state.update { it.copy(showImagePicker = false) }
            }

            ProjectEvent.ShowGallery -> {
                _state.update {
                    it.copy(
                        showGallery = true,
                        showImagePicker = false
                    )
                }
            }

            ProjectEvent.HideGalleryPicker -> {
                _state.update { it.copy(showGallery = false) }
            }

            ProjectEvent.ShowCameraPicker -> {
                _state.update {
                    it.copy(
                        showCamera = true,
                        showImagePicker = false
                    )
                }
            }

            ProjectEvent.HideCameraPicker -> {
                _state.update { it.copy(showCamera = false) }
            }

            ProjectEvent.CreateProject -> {
                createProject()
            }

            else -> Unit
        }
    }

    private fun createProject() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            createProjectUseCase(_state.value.project)
                .onSuccess {
                    eventChannel.send(ProjectEvent.NavigateBack)
                }
                .onFailure {
                    MainSnackbarController.sendEvent(
                        event = SnackbarEvent(Constants.ERROR_MESSAGE)
                    )
                }
            _state.update { it.copy(isLoading = false) }
        }
    }
}