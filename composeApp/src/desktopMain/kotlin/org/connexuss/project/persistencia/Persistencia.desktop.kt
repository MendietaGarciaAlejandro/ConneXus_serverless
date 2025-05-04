package org.connexuss.project.persistencia

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.observable.makeObservable

//val settings: Settings = Settings() // Preferences.userRoot()

@OptIn(ExperimentalSettingsApi::class)
actual fun provideObservableSettings(): ObservableSettings =
    settings.makeObservable() // SharedPreferencesSettings es Observable