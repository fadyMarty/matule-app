package com.fadymarty.matule.presentation.project.projects

sealed interface ProjectsEvent {
    data object NavigateToCreateProject : ProjectsEvent
    data class NavigateToProject(val id: String) : ProjectsEvent
    data object ShowErrorSnackBar : ProjectsEvent
}