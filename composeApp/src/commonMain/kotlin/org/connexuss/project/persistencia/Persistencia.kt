package org.connexuss.project.persistencia

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.connexuss.project.interfaces.DefaultTopBar
import org.connexuss.project.interfaces.LimitaTamanioAncho


expect val settings: Settings

class FlowSettingsProvider(
    settings: Settings,
    dispatcher: CoroutineDispatcher // ← ahora recibe dispatcher
) {
    private val observable: ObservableSettings = settings as ObservableSettings

    @OptIn(ExperimentalSettingsApi::class)
    val flowSettings: FlowSettings =
        observable.toFlowSettings(dispatcher) // usa el dispatcher aquí
}

class SettingsState @OptIn(ExperimentalSettingsApi::class) constructor(
    private val flowSettings: FlowSettings
) {
    // Leer flujos de preferencia
    @OptIn(ExperimentalSettingsApi::class)
    val isDarkModeFlow: Flow<Boolean> = flowSettings.getBooleanFlow("dark_mode", defaultValue = false)
    @OptIn(ExperimentalSettingsApi::class)
    val onboardingShownFlow: Flow<Boolean> = flowSettings.getBooleanFlow("onboarding_shown", defaultValue = false)
    @OptIn(ExperimentalSettingsApi::class)
    val counterFlow: Flow<Int> = flowSettings.getIntFlow("counter", defaultValue = 0)
    @OptIn(ExperimentalSettingsApi::class)
    val userNameFlow: Flow<String> = flowSettings.getStringFlow("user_name", defaultValue = "")
    @OptIn(ExperimentalSettingsApi::class)
    val ratingFlow: Flow<Float> = flowSettings.getFloatFlow("rating", defaultValue = 0f)
    @OptIn(ExperimentalSettingsApi::class)
    val thresholdFlow: Flow<Double> = flowSettings.getDoubleFlow("threshold", defaultValue = 0.0)
    @OptIn(ExperimentalSettingsApi::class)
    val launchCountFlow: Flow<Long> = flowSettings.getLongFlow("launch_count", defaultValue = 0L)

    // Funciones suspend para escritura
    @OptIn(ExperimentalSettingsApi::class)
    suspend fun setDarkMode(value: Boolean) = flowSettings.putBoolean("dark_mode", value)
    @OptIn(ExperimentalSettingsApi::class)
    suspend fun toggleOnboarding(shown: Boolean) = flowSettings.putBoolean("onboarding_shown", shown)
    @OptIn(ExperimentalSettingsApi::class)
    suspend fun incrementCounter() = flowSettings.putInt("counter", flowSettings.getInt("counter", 0) + 1)
    @OptIn(ExperimentalSettingsApi::class)
    suspend fun updateUserName(name: String) = flowSettings.putString("user_name", name)
    @OptIn(ExperimentalSettingsApi::class)
    suspend fun setRating(value: Float) = flowSettings.putFloat("rating", value)
    @OptIn(ExperimentalSettingsApi::class)
    suspend fun setThreshold(value: Double) = flowSettings.putDouble("threshold", value)
    @OptIn(ExperimentalSettingsApi::class)
    suspend fun incrementLaunchCount() = flowSettings.putLong("launch_count", flowSettings.getLong("launch_count", 0L) + 1)
}

@OptIn(ExperimentalSettingsApi::class)
@Composable
fun PantallaPruebasPersistencia(flowSettingsProvider: FlowSettingsProvider, navController: NavHostController) {
    // Proveemos SettingsState con FlowSettings y un scope
    val scope = rememberCoroutineScope()
    val settingsState = remember { SettingsState(flowSettingsProvider.flowSettings) }

    // Convertimos Flow<T> en State<T> para Compose
    val isDarkMode by settingsState.isDarkModeFlow.collectAsState(initial = false)     // :contentReference[oaicite:7]{index=7}
    val onboardingShown by settingsState.onboardingShownFlow.collectAsState(initial = false)
    val counter by settingsState.counterFlow.collectAsState(initial = 0)
    val userName by settingsState.userNameFlow.collectAsState(initial = "")
    val rating by settingsState.ratingFlow.collectAsState(initial = 0f)
    val threshold by settingsState.thresholdFlow.collectAsState(initial = 0.0)
    val launchCount by settingsState.launchCountFlow.collectAsState(initial = 0L)

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Persistencia",
                navController = navController,
                showBackButton = true,
                muestraEngranaje = true,
                irParaAtras = true
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LimitaTamanioAncho { modifier ->

                Column(
                    modifier.fillMaxSize().padding(padding),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("💾 Persistencia con FlowSettings", style = MaterialTheme.typography.h6)

                    // Switch Dark Mode
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("Dark Mode:", Modifier.weight(1f))
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { scope.launch { settingsState.setDarkMode(it) } }
                        )
                    }

                    // Onboarding
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("Onboarding mostrado:", Modifier.weight(1f))
                        Text(onboardingShown.toString())
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = { scope.launch { settingsState.toggleOnboarding(!onboardingShown) } }) {
                            Text("Toggle")
                        }
                    }

                    // Contador
                    Text("Contador: $counter")
                    Button(onClick = { scope.launch { settingsState.incrementCounter() } }) { Text("+") }

                    // TextField
                    OutlinedTextField(
                        value = userName,
                        onValueChange = { scope.launch { settingsState.updateUserName(it) } },
                        label = { Text("Usuario") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Slider Rating
                    Text("Rating: $rating")
                    Slider(
                        value = rating,
                        onValueChange = { scope.launch { settingsState.setRating(it) } },
                        valueRange = 0f..5f, steps = 4
                    )

                    // Slider Threshold
                    Text("Threshold: $threshold")
                    Slider(
                        value = threshold.toFloat(),
                        onValueChange = { scope.launch { settingsState.setThreshold(it.toDouble()) } },
                        valueRange = 0f..100f
                    )

                    // Launch Count
                    Text("Launch Count: $launchCount")
                    Button(onClick = { scope.launch { settingsState.incrementLaunchCount() } }) {
                        Text("Launch")
                    }
                }
            }
        }
    }
}