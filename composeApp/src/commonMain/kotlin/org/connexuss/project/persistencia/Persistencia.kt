package org.connexuss.project.persistencia

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.boolean
import com.russhwolf.settings.coroutines.getBooleanStateFlow
import com.russhwolf.settings.double
import com.russhwolf.settings.float
import com.russhwolf.settings.int
import com.russhwolf.settings.long
import com.russhwolf.settings.string
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


expect val settings: Settings

//class SettingsState(
//    settings: Settings,
//    private val scope: CoroutineScope = MainScope() // o tu propio scope
//) {
//    // Delegate primitivo
//    var isDarkMode: Boolean by settings.boolean("dark_mode", defaultValue = false)
//
//    // Observación reactiva con StateFlow
//    private val observable: ObservableSettings = settings as ObservableSettings
//
//    @OptIn(ExperimentalSettingsApi::class)
//    val onboardingShownFlow: StateFlow<Boolean> =
//        observable.getBooleanStateFlow(
//            scope,
//            key = "onboarding_shown",
//            defaultValue = false,
//            sharingStarted = SharingStarted.Lazily
//        )
//}

class SettingsState(
    settings: Settings,
    scope: CoroutineScope = MainScope()
) {
    // Delegados para tipos primitivos
    var isDarkMode: Boolean by settings.boolean("dark_mode", defaultValue = false)
    var onboardingShown: Boolean by settings.boolean("onboarding_shown", defaultValue = false)  // ← Aquí :contentReference[oaicite:2]{index=2}
    var counter: Int by settings.int("counter", defaultValue = 0)
    var launchCount: Long by settings.long("launch_count", defaultValue = 0L)
    var rating: Float by settings.float("rating", defaultValue = 0f)
    var threshold: Double by settings.double("threshold", defaultValue = 0.0)
    var userName: String by settings.string("user_name", defaultValue = "")

    // Para el flujo reactivo: StateFlow directamente, sin 'by' como delegado
    private val observable = settings as ObservableSettings
    @OptIn(ExperimentalSettingsApi::class)
    val onboardingShownFlow: StateFlow<Boolean> =
        observable.getBooleanStateFlow(
            coroutineScope = scope,
            key = "onboarding_shown",
            defaultValue = false,
            sharingStarted = SharingStarted.Lazily
        )  // Extensión en multiplatform-settings-coroutines :contentReference[oaicite:3]{index=3}
}

@Composable
fun SettingsDemoScreen(settingsState: SettingsState) {
    // Collect Flow-based state
    val onboardingShown by settingsState.onboardingShownFlow.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "💾 Demo de Persistencia Multiplatform",
            style = MaterialTheme.typography.h6
        )

        // Boolean: Dark Mode
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Dark Mode:", modifier = Modifier.weight(1f))
            Switch(
                checked = settingsState.isDarkMode,
                onCheckedChange = { settingsState.isDarkMode = it }
            )
        }

        // Boolean (Flow): Onboarding showed
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Onboarding mostrado:", modifier = Modifier.weight(1f))
            Text(text = onboardingShown.toString())
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                // Toggle value
                coroutineScope.launch {
                    settingsState.onboardingShown = !onboardingShown
                }
            }) {
                Text("Toggle")
            }
        }

        // Int: Contador
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Contador:", modifier = Modifier.weight(1f))
            Text(text = settingsState.counter.toString())
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { settingsState.counter += 1 }) { Text("+") }
            Spacer(modifier = Modifier.width(4.dp))
            Button(onClick = { settingsState.counter = settingsState.counter.coerceAtLeast(1) - 1 }) { Text("-") }
        }

        // String: Nombre de usuario
        OutlinedTextField(
            value = settingsState.userName,
            onValueChange = { settingsState.userName = it },
            label = { Text("Nombre de usuario") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Float: Rating
        Column {
            Text(text = "Rating: ${settingsState.rating}")
            Slider(
                value = settingsState.rating,
                onValueChange = { settingsState.rating = it },
                valueRange = 0f..5f,
                steps = 4
            )
        }

        // Double: Threshold
        Column {
            Text(text = "Threshold: ${settingsState.threshold}")
            Slider(
                value = settingsState.threshold.toFloat(),
                onValueChange = { settingsState.threshold = it.toDouble() },
                valueRange = 0f..100f
            )
        }

        // Long: Launch Count
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Launch Count:", modifier = Modifier.weight(1f))
            Text(text = settingsState.launchCount.toString())
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { settingsState.launchCount += 1L }) { Text("+") }
        }
    }
}
