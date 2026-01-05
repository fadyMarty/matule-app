package com.fadymarty.matule.presentation.project.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.matule_network.domain.use_case.projects.GetProjectsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProjectsViewModel(
    private val getProjectsUseCase: GetProjectsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProjectsState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<ProjectsEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getProjectsUseCase()
                .onSuccess { projects ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            projects = projects
                        )
                    }
                }
                .onFailure {
                    eventChannel.send(ProjectsEvent.ShowErrorSnackBar)
                }
        }
    }

    fun onEvent(event: ProjectsEvent) {
        when (event) {
            ProjectsEvent.NavigateToCreateProject -> {
                viewModelScope.launch {
                    eventChannel.send(ProjectsEvent.NavigateToCreateProject)
                }
            }

            is ProjectsEvent.NavigateToProject -> {
                viewModelScope.launch {
                    eventChannel.send(ProjectsEvent.NavigateToProject(event.id))
                }
            }

            else -> Unit
        }
    }
}