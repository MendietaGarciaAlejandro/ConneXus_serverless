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
import com.russhwolf.settings.observable.makeObservable
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.connexuss.project.interfaces.navegacion.DefaultTopBar
import org.connexuss.project.interfaces.comun.LimitaTamanioAncho
import org.connexuss.project.interfaces.tema.TemaConfig
import org.connexuss.project.usuario.Usuario

val settings: Settings = Settings() // SharedPreferences en Android, Preferences.userRoot() en Desktop, localStorage en JS

@OptIn(ExperimentalSettingsApi::class)
val observable = settings.makeObservable()

@OptIn(ExperimentalSettingsApi::class)
val flowSettings = observable.toFlowSettings(Dispatchers.Default)

const val KEY_IDIOMA = "idioma"

@OptIn(ExperimentalSettingsApi::class)
fun SettingsState.getIdiomaKeyFlow(): Flow<String> =
    flowSettings.getStringFlow(KEY_IDIOMA, defaultValue = "es")

@OptIn(ExperimentalSettingsApi::class)
suspend fun SettingsState.setIdiomaKey(key: String) =
    flowSettings.putString(KEY_IDIOMA, key)

const val KEY_TEMA_CLARO = "temaClaro"
const val KEY_COLOR_TEMA = "colorTemaKey"

@OptIn(ExperimentalSettingsApi::class)
fun SettingsState.getTemaConfigFlow(): Flow<TemaConfig> =
    flowSettings.getBooleanFlow(KEY_TEMA_CLARO, true)
        .combine(
            flowSettings.getStringFlow(KEY_COLOR_TEMA, "claro")
        ) { claro, colorKey ->
            TemaConfig(temaClaro = claro, colorTemaKey = colorKey)
        }

@OptIn(ExperimentalSettingsApi::class)
suspend fun SettingsState.setTemaConfig(config: TemaConfig) {
    flowSettings.putBoolean(KEY_TEMA_CLARO, config.temaClaro)
    flowSettings.putString(KEY_COLOR_TEMA, config.colorTemaKey)
}

const val KEY_FONT = "fontFamily"

// Las claves que usaremos para identificar cada fuente
object FontKeys {
    const val DEFAULT    = "default"
    const val SERIF      = "serif"
    const val MONOSPACE  = "monospace"
    const val CURSIVE    = "cursive"
    const val SANS_SERIF = "sans_serif"
}

@OptIn(ExperimentalSettingsApi::class)
fun SettingsState.getFontKeyFlow(): Flow<String> =
    flowSettings.getStringFlow(KEY_FONT, FontKeys.DEFAULT)

@OptIn(ExperimentalSettingsApi::class)
suspend fun SettingsState.setFontKey(key: String) =
    flowSettings.putString(KEY_FONT, key)

class FlowSettingsProvider(
    settings: Settings,
    dispatcher: CoroutineDispatcher // ← ahora recibe dispatcher
) {
    @OptIn(ExperimentalSettingsApi::class)
    val observable: ObservableSettings = settings.makeObservable()
    @OptIn(ExperimentalSettingsApi::class)
    val flowSettings: FlowSettings =
        observable.toFlowSettings(dispatcher) // usa el dispatcher aquí
}

const val KEY_ACCESS  = "access_token"
const val KEY_REFRESH = "refresh_token"
const val KEY_EXPIRES  = "expires_in"
const val KEY_USER    = "user_data"
const val KEY_USER_JSON = "user_data_json"
const val KEY_REMEMBERED_EMAIL = "remembered_email"
const val KEY_REMEMBER_ME_CHECKED = "remember_me_checked"
const val KEY_REMEMBER_EMAIL_CHECKED = "remember_email_checked"

@OptIn(ExperimentalSettingsApi::class)
suspend fun clearSession() {
    flowSettings.remove(KEY_ACCESS)
    flowSettings.remove(KEY_REFRESH)
    flowSettings.remove(KEY_EXPIRES)
    flowSettings.remove(KEY_USER_JSON)
    flowSettings.remove(KEY_REMEMBERED_EMAIL)
    flowSettings.putBoolean(KEY_REMEMBER_ME_CHECKED, false)
    flowSettings.putBoolean(KEY_REMEMBER_EMAIL_CHECKED, false)
}

@OptIn(ExperimentalSettingsApi::class)
suspend fun saveSession(
    session: UserSession,
    usuario: Usuario
) {
    flowSettings.putString(KEY_ACCESS,  session.accessToken)
    flowSettings.putString(KEY_REFRESH, session.refreshToken)
    flowSettings.putLong(  KEY_EXPIRES, session.expiresIn)
    // Serializa tu Usuario a JSON
    val userJson = Json.encodeToString(Usuario.serializer(), usuario)
    flowSettings.putString(KEY_USER_JSON, userJson)
}

@OptIn(ExperimentalSettingsApi::class)
fun getUserJsonFlow(): Flow<String?> =
    flowSettings.getStringFlow(KEY_USER_JSON, defaultValue = "")
        .map { it.ifBlank { null } }

fun SettingsState.getRestoredSessionFlow(): Flow<Pair<UserSession, Usuario>?> =
    combine(
        getSessionFlow(),             // Flow<UserSession?>
        getUserJsonFlow()             // Flow<String?>
    ) { session, userJson ->
        if (session == null || userJson == null) null
        else {
            val usuario = Json.decodeFromString(Usuario.serializer(), userJson)
            session to usuario
        }
    }


@OptIn(ExperimentalSettingsApi::class)
fun SettingsState.getSessionFlow(): Flow<UserSession?> = combine(
    flowSettings.getStringFlow(KEY_ACCESS, ""),
    flowSettings.getStringFlow(KEY_REFRESH, ""),
    flowSettings.getLongFlow(KEY_EXPIRES, 0L)
) { access, refresh, exp ->
    if (access.isBlank() || refresh.isBlank()) null
    else UserSession(accessToken = access, refreshToken = refresh, expiresIn = exp.toInt().toLong(), tokenType = "Bearer", user = null)
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
    @OptIn(ExperimentalSettingsApi::class)
    val rememberedEmailFlow: Flow<String> = flowSettings.getStringFlow("remembered_email", defaultValue = "")

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

    // Funciones para manejar el email y los estados de los checkboxes
    @OptIn(ExperimentalSettingsApi::class)
    suspend fun saveEmail(email: String) = flowSettings.putString(KEY_REMEMBERED_EMAIL, email)

    @OptIn(ExperimentalSettingsApi::class)
    suspend fun removeEmail() = flowSettings.remove(KEY_REMEMBERED_EMAIL)

    @OptIn(ExperimentalSettingsApi::class)
    suspend fun getEmail(): String = flowSettings.getString(KEY_REMEMBERED_EMAIL, "")

    @OptIn(ExperimentalSettingsApi::class)
    suspend fun saveRememberMeState(checked: Boolean) = flowSettings.putBoolean(KEY_REMEMBER_ME_CHECKED, checked)

    @OptIn(ExperimentalSettingsApi::class)
    suspend fun getRememberMeState(): Boolean = flowSettings.getBoolean(KEY_REMEMBER_ME_CHECKED, false)

    @OptIn(ExperimentalSettingsApi::class)
    suspend fun saveRememberEmailState(checked: Boolean) = flowSettings.putBoolean(KEY_REMEMBER_EMAIL_CHECKED, checked)

    @OptIn(ExperimentalSettingsApi::class)
    suspend fun getRememberEmailState(): Boolean = flowSettings.getBoolean(KEY_REMEMBER_EMAIL_CHECKED, false)

    // Funciones para manejo de sesión
    @OptIn(ExperimentalSettingsApi::class)
    suspend fun clearSession() {
        flowSettings.remove(KEY_ACCESS)
        flowSettings.remove(KEY_REFRESH)
        flowSettings.remove(KEY_EXPIRES)
        flowSettings.remove(KEY_USER_JSON)
        flowSettings.remove(KEY_REMEMBERED_EMAIL)
        flowSettings.putBoolean(KEY_REMEMBER_ME_CHECKED, false)
        flowSettings.putBoolean(KEY_REMEMBER_EMAIL_CHECKED, false)
    }

    @OptIn(ExperimentalSettingsApi::class)
    suspend fun saveSession(
        session: UserSession,
        usuario: Usuario
    ) {
        flowSettings.putString(KEY_ACCESS, session.accessToken)
        flowSettings.putString(KEY_REFRESH, session.refreshToken)
        flowSettings.putLong(KEY_EXPIRES, session.expiresIn.toLong())
        // Serializa tu Usuario a JSON
        val userJson = Json.encodeToString(Usuario.serializer(), usuario)
        flowSettings.putString(KEY_USER_JSON, userJson)
    }
}

expect fun provideObservableSettings(): ObservableSettings

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
