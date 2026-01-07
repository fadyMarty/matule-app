package com.fadymarty.matule.presentation.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.matule_network.domain.use_case.projects.GetProjectsUseCase
import com.fadymarty.matule_network.domain.use_case.projects.ObserveProjectsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProjectsViewModel(
    private val getProjectsUseCase: GetProjectsUseCase,
    private val observeProjectsUseCase: ObserveProjectsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ProjectsState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<ProjectsEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getProjectsUseCase()
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                }
                .onFailure {
                    eventChannel.send(ProjectsEvent.ShowErrorSnackBar)
                }
        }
        observeProjectsUseCase().onEach { projects ->
            _state.update { it.copy(projects = projects) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: ProjectsEvent) {
        when (event) {
            is ProjectsEvent.NavigateToProject -> {
                viewModelScope.launch {
                    eventChannel.send(ProjectsEvent.NavigateToProject(event.id))
                }
            }

            else -> Unit
        }
    }
}