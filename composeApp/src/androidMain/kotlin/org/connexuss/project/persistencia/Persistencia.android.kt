package org.connexuss.project.persistencia

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings

actual val settings: Settings = Settings() // no-arg: default SharedPreferences
actual fun provideObservableSettings(): ObservableSettings =
    (Settings() as ObservableSettings) // SharedPreferencesSettings es Observable