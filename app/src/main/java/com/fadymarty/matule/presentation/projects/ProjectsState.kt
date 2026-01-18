package com.fadymarty.matule.presentation.projects

import com.fadymarty.network.domain.model.Project

data class ProjectsState(
    val isLoading: Boolean = false,
    val projects: List<Project> = emptyList(),
)