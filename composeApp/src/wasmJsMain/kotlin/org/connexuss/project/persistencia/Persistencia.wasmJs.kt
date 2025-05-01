package org.connexuss.project.persistencia

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings
import com.russhwolf.settings.observable.makeObservable
import kotlinx.browser.window

//val settings: Settings = Settings() // localStorage

@OptIn(ExperimentalSettingsApi::class)
actual fun provideObservableSettings(): ObservableSettings =
    settings.makeObservable()