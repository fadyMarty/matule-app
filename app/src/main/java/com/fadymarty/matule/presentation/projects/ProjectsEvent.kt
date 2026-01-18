package com.fadymarty.matule.presentation.projects

sealed interface ProjectsEvent {
    data class NavigateToProject(val id: String? = null) : ProjectsEvent
}