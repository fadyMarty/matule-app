package com.fadymarty.matule.presentation.project

sealed interface ProjectEvent {
    data class TitleChanged(val title: String) : ProjectEvent
    data class DateStartChanged(val dateStart: String) : ProjectEvent
    data class DateEndChanged(val dateEnd: String) : ProjectEvent
    data class DescriptionSourceChanged(val descriptionSource: String) : ProjectEvent
    data class CategorySelected(val category: String) : ProjectEvent
    data object ShowImagePickerModal : ProjectEvent
    data object HideImagePickerModal : ProjectEvent
    data object ShowGallery : ProjectEvent
    data object HideGallery : ProjectEvent
    data object ShowCamera : ProjectEvent
    data object HideCamera : ProjectEvent
    data class ImageSelected(val uri: String) : ProjectEvent
    data object CreateProject : ProjectEvent
    data object NavigateToProjects : ProjectEvent
    data object ShowErrorSnackBar : ProjectEvent
}