package com.fadymarty.matule.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun setNotificationEnabled(enabled: Boolean)
    fun isNotificationsEnabled(): Flow<Boolean>
}