package com.fadymarty.matule.presentation.project

import com.fadymarty.matule_network.domain.model.Project
import com.fadymarty.matule_network.domain.model.User

data class ProjectState(
    val isLoading: Boolean = false,
    val project: Project = Project(
        title = "",
        typeProject = "Выберите  тип",
        dateStart = "",
        dateEnd = "",
        descriptionSource = "",
        category = "Выберите  категорию",
    ),
    val users: List<User> = emptyList(),
    val user: User? = null,
    val showImagePicker: Boolean = false,
    val showGallery: Boolean = false,
    val showCamera: Boolean = false,
)
