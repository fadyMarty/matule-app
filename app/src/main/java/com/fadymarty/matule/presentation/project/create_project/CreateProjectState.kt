package com.fadymarty.matule.presentation.project.create_project

import com.fadymarty.matule_ui_kit.presentation.components.select.SelectItem

data class CreateProjectState(
    val isLoading: Boolean = false,
    val type: SelectItem? = null,
    val title: String = "",
    val dateStart: String = "",
    val dateEnd: String = "",
    val userId: SelectItem? = null,
    val descriptionSource: String = "",
    val category: SelectItem? = null,
    val image: String? = null,
)