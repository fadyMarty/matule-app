package com.fadymarty.matule.presentation.profile

import com.fadymarty.network.domain.model.User

data class ProfileState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val isNotificationsEnabled: Boolean = true,
)
