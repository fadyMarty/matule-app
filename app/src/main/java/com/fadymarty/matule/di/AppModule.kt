package com.fadymarty.matule.di

import com.fadymarty.matule.data.repository.SettingsRepositoryImpl
import com.fadymarty.matule.domain.repository.SettingsRepository
import com.fadymarty.matule.domain.use_case.settings.IsNotificationsEnabledUseCase
import com.fadymarty.matule.domain.use_case.settings.SetNotificationsEnabledUseCase
import com.fadymarty.matule.domain.use_case.validation.ValidateEmailUseCase
import com.fadymarty.matule.domain.use_case.validation.ValidatePasswordConfirmUseCase
import com.fadymarty.matule.domain.use_case.validation.ValidatePasswordUseCase
import com.fadymarty.matule.presentation.cart.CartViewModel
import com.fadymarty.matule.presentation.catalog.CatalogViewModel
import com.fadymarty.matule.presentation.create_pin.CreatePinViewModel
import com.fadymarty.matule.presentation.enter_pin.EnterPinViewModel
import com.fadymarty.matule.presentation.home.HomeViewModel
import com.fadymarty.matule.presentation.login.LoginViewModel
import com.fadymarty.matule.presentation.profile.ProfileViewModel
import com.fadymarty.matule.presentation.project.ProjectViewModel
import com.fadymarty.matule.presentation.projects.ProjectsViewModel
import com.fadymarty.matule.presentation.register.RegisterViewModel
import com.fadymarty.matule.presentation.splash.SplashViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    singleOf(::SettingsRepositoryImpl) { bind<SettingsRepository>() }

    singleOf(::ValidateEmailUseCase)
    singleOf(::ValidatePasswordUseCase)
    singleOf(::ValidatePasswordConfirmUseCase)

    singleOf(::SetNotificationsEnabledUseCase)
    singleOf(::IsNotificationsEnabledUseCase)

    viewModelOf(::SplashViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::CreatePinViewModel)
    viewModelOf(::EnterPinViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::CatalogViewModel)
    viewModelOf(::ProjectsViewModel)
    viewModelOf(::ProjectViewModel)
    viewModelOf(::CartViewModel)
}