package com.fadymarty.matule.domain.use_case.settings

import com.fadymarty.matule.domain.repository.SettingsRepository

class SetNotificationsEnabledUseCase(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(enabled: Boolean) {
        settingsRepository.setNotificationEnabled(enabled)
    }
}