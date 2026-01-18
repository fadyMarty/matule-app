package com.fadymarty.matule.presentation.project

sealed interface ProjectEvent {
    data class TypeSelected(val type: String) : ProjectEvent
    data class TitleChanged(val title: String) : ProjectEvent
    data class DateStartChanged(val dateStart: String) : ProjectEvent
    data class DateEndChanged(val dateEnd: String) : ProjectEvent
    data class UserSelected(val fullName: String) : ProjectEvent
    data class DescriptionSourceChanged(val descriptionSource: String) : ProjectEvent
    data class CategorySelected(val category: String) : ProjectEvent
    data class ImageSelected(val bytes: ByteArray) : ProjectEvent
    data object ShowImagePicker : ProjectEvent
    data object HideImagePicker : ProjectEvent
    data object ShowGallery : ProjectEvent
    data object HideGalleryPicker : ProjectEvent
    data object ShowCameraPicker : ProjectEvent
    data object HideCameraPicker : ProjectEvent
    data object CreateProject : ProjectEvent
    data object NavigateBack : ProjectEvent
}
