package com.fadymarty.matule.presentation.project

import com.fadymarty.matule_network.domain.model.Project

data class ProjectState(
    val isLoading: Boolean = false,
    val project: Project = Project(
        title = "",
        dateStart = "",
        dateEnd = "",
        gender = "",
        descriptionSource = "",
        category = "",
        image = "",
        userId = ""
    ),
    val showImagePickerModal: Boolean = false,
    val showGallery: Boolean = false,
    val showCamera: Boolean = false,
)