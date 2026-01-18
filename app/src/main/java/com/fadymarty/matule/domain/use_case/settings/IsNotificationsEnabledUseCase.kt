package com.fadymarty.matule.domain.use_case.settings

import com.fadymarty.matule.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class IsNotificationsEnabledUseCase(
    private val settingsRepository: SettingsRepository,
) {
    operator fun invoke(): Flow<Boolean> {
        return settingsRepository.isNotificationsEnabled()
    }
}