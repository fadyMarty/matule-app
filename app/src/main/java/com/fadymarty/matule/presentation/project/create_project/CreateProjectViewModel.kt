package com.fadymarty.matule.presentation.project.create_project

import androidx.lifecycle.ViewModel
import com.fadymarty.matule_network.domain.use_case.projects.CreateProjectUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateProjectViewModel(
    private val createProjectUseCase: CreateProjectUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(CreateProjectState())
    val state = _state.asStateFlow()
}