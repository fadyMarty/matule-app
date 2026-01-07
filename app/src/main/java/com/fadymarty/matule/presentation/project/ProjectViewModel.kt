package com.fadymarty.matule.presentation.project

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.fadymarty.matule.presentation.navigation.Route
import com.fadymarty.matule_network.domain.use_case.projects.CreateProjectUseCase
import com.fadymarty.matule_network.domain.use_case.projects.GetProjectByIdUseCase
import com.fadymarty.matule_network.domain.use_case.user.GetUserIdUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProjectViewModel(
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val createProjectUseCase: CreateProjectUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val project = savedStateHandle.toRoute<Route.Project>()

    private val _state = MutableStateFlow(ProjectState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<ProjectEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        project.id?.let { id ->
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                getProjectByIdUseCase(id)
                    .onSuccess { project ->
                        _state.update {
                            it.copy(
                                project = project,
                                isLoading = false
                            )
                        }
                    }
                    .onFailure {
                        eventChannel.send(ProjectEvent.ShowErrorSnackBar)
                    }
            }
        }
    }

    fun onEvent(event: ProjectEvent) {
        when (event) {
            is ProjectEvent.TitleChanged -> {
                _state.update {
                    it.copy(
                        project = it.project.copy(
                            title = event.title
                        )
                    )
                }
            }

            is ProjectEvent.DateStartChanged -> {
                _state.update {
                    it.copy(
                        project = it.project.copy(
                            dateStart = event.dateStart
                        )
                    )
                }
            }

            is ProjectEvent.DateEndChanged -> {
                _state.update {
                    it.copy(
                        project = it.project.copy(
                            dateEnd = event.dateEnd
                        )
                    )
                }
            }

            is ProjectEvent.DescriptionSourceChanged -> {
                _state.update {
                    it.copy(
                        project = it.project.copy(
                            descriptionSource = event.descriptionSource
                        )
                    )
                }
            }

            ProjectEvent.ShowImagePickerModal -> {
                _state.update { it.copy(showImagePickerModal = true) }
            }

            ProjectEvent.HideImagePickerModal -> {
                _state.update { it.copy(showImagePickerModal = false) }
            }

            ProjectEvent.ShowGallery -> {
                _state.update {
                    it.copy(
                        showGallery = true,
                        showImagePickerModal = false
                    )
                }
            }

            ProjectEvent.HideGallery -> {
                _state.update { it.copy(showGallery = false) }
            }

            ProjectEvent.ShowCamera -> {
                _state.update {
                    it.copy(
                        showCamera = true,
                        showImagePickerModal = false
                    )
                }
            }

            ProjectEvent.HideCamera -> {
                _state.update { it.copy(showCamera = false) }
            }

            is ProjectEvent.ImageSelected -> {
                _state.update {
                    it.copy(
                        showGallery = false,
                        showCamera = false,
                        project = it.project.copy(image = event.uri)
                    )
                }
            }

            ProjectEvent.CreateProject -> {
                createProject()
            }

            else -> Unit
        }
    }

    private fun createProject() {
        getUserIdUseCase().onEach { userId ->
            userId?.let {
                _state.update { it.copy(isLoading = true) }
                val project = _state.value.project.copy(userId = userId)
                createProjectUseCase(project)
                    .onSuccess {
                        eventChannel.send(ProjectEvent.NavigateToProjects)
                    }
                    .onFailure {
                        _state.update { it.copy(isLoading = false) }
                        eventChannel.send(ProjectEvent.ShowErrorSnackBar)
                    }
            }
        }.launchIn(viewModelScope)
    }
}