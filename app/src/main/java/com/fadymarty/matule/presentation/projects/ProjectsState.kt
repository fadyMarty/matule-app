package com.fadymarty.matule.presentation.projects

import com.fadymarty.matule_network.domain.model.Project

data class ProjectsState(
    val isLoading: Boolean = false,
    val projects: List<Project> = emptyList(),
)
