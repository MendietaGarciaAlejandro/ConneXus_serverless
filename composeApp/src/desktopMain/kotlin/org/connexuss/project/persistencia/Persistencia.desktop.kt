package org.connexuss.project.persistencia

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings

actual val settings: Settings = Settings() // Preferences.userRoot()
actual fun provideObservableSettings(): ObservableSettings =
    (Settings() as ObservableSettings) // SharedPreferencesSettings es Observable