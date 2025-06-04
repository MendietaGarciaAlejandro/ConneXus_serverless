package org.connexuss.project.interfaces.comun

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.coroutines.launch
import org.connexuss.project.interfaces.navegacion.DefaultTopBar
import org.connexuss.project.persistencia.SettingsState
import org.connexuss.project.persistencia.getIdiomaKeyFlow
import org.connexuss.project.persistencia.setIdiomaKey

@Composable
fun PantallaIdiomas(
    navController: NavHostController,
    settingsState: SettingsState
) {
    val scope = rememberCoroutineScope()
    val idiomas = listOf(
        LanguageOption("es", "espanol", espannol),
        LanguageOption("en", "ingles",  ingles),
        LanguageOption("pt", "portugues", portugues),
        LanguageOption("fr", "frances",   frances),
        LanguageOption("de", "aleman",    aleman),
        LanguageOption("it", "italiano",  italiano)
    )

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = traducir("cambiar_idioma"),
                navController = navController,
                showBackButton = true,
                irParaAtras = true,
                muestraEngranaje = false
            )
        }
    ) { padding ->
        LimitaTamanioAncho { modifier ->
            Column(
                modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                idiomas.forEach { option ->
                    Button(
                        onClick = {
                            // Guardas el código en Settings
                            scope.launch { settingsState.setIdiomaKey(option.code) }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Mostramos la traducción de “espanol”, “ingles”, …
                        Text(text = traducir(option.labelKey))
                    }
                }
            }
        }
    }
}

data class Idioma(val palabras: Map<String, String>)

data class LanguageOption(
    val code: String,
    val labelKey: String,
    val idioma: Idioma
)

val IdiomaSaver: Saver<Idioma, Map<String, String>> = object : Saver<Idioma, Map<String, String>> {
    override fun restore(value: Map<String, String>): Idioma {
        return Idioma(value)  // Reconstruimos el objeto a partir del mapa de palabras
    }

    override fun SaverScope.save(value: Idioma): Map<String, String> {
        return value.palabras  // Guardamos solo el mapa de palabras
    }
}

val LocalIdiomaState = staticCompositionLocalOf<MutableState<Idioma>> {
    error("No se ha proporcionado un estado de idioma")
}

@OptIn(ExperimentalSettingsApi::class)
@Composable
fun ProveedorDeIdioma(
    settingsState: SettingsState,
    content: @Composable () -> Unit
) {
    // 1) Leemos la clave guardada
    val idiomaKey by settingsState.getIdiomaKeyFlow().collectAsState(initial = "es")

    // 2) La mapeamos a un objeto Idioma
    val idioma = remember(idiomaKey) {
        when (idiomaKey) {
            "es" -> espannol
            "en" -> ingles
            "pt" -> portugues
            "fr" -> frances
            "de" -> aleman
            "it" -> italiano
            else -> espannol
        }
    }

    // 3) Mantener un MutableState<Idioma> y actualizarlo cuando cambie idioma
    val idiomaState = remember { mutableStateOf(idioma) }
    LaunchedEffect(idioma) {
        idiomaState.value = idioma
    }

    CompositionLocalProvider(LocalIdiomaState provides idiomaState) {
        content()
    }
}

// Estado del idioma
@Composable
fun IdiomaState(): MutableState<Idioma> {
    // Idioma por defecto
    return remember { mutableStateOf(espannol) }
}

// Cambia el idioma global
fun cambiarIdioma(idiomaState: MutableState<Idioma>, nuevoIdioma: Idioma) {
    idiomaState.value = nuevoIdioma
}

// Función para obtener la traducción de una clave, según el idioma actual
@Composable
fun traducir(key: String): String {
    val idiomaState = LocalIdiomaState.current
    return idiomaState.value.palabras[key] ?: key
}

// Idiomas
// Español

val espannol = Idioma(
    palabras = mapOf(

        // Nuevos pares clave-valor
        "error_autenticacion_supabase" to "Error de autenticación en Supabase",
        "error_autenticacion_1" to "Error en autenticación.",
        "mantener_sesion" to "Mantener sesión",
        "recordar_email" to "Recordar email",
        "debug_zona_pruebas" to "Debug: Ir a la zona de pruebas",
        "introduce_correo" to "Introduce tu correo",
        "correo_restablece_contrasennia" to "Se ha enviado un correo para restablecer tu contraseña. Revisa tu bandeja de entrada.",
        "error_enviando_correo" to "Error al enviar el correo",
        "info_restablecer_contrasennia" to "Una vez restablezcas tu contraseña desde el navegador, vuelve a esta app y entra con tu nueva clave.",
        "formato_correo_invalido" to "Formato de correo inválido",
        "error_registrar" to "Error al registrar",
        "correo_aun_no_verificado" to "❗ Tu correo aún no está verificado.",
        "correo_reenviado" to "Correo reenviado correctamente",
        "falta_info_correo_reenviado" to "❌ Falta información para reenviar el correo",
        "error_reenviar" to "❌ Error al reenviar.",
        "cargando" to "(cargando...)",
        "imagen" to "Imagen",
        "enviar_mensaje" to "Enviar Mensaje",
        "grupo" to "Grupo",
        "item_editar" to "Editar",
        "item_eliminar" to "Eliminar",
        "participantes_grupo" to "Participantes del Grupo",
        "editar_mensaje" to "Editar mensaje",
        "nuevo_contenido" to "Nuevo contenido",
        "error_leer_mensaje" to "Error al leer el mensaje",
        "crear" to "Crear",
        "tema_creado_correcto" to "Tema creado correctamente",
        "error_crear_tema" to "Error al crear el tema",
        "buscar" to "Buscar...",
        "nuevo_tema" to "Nuevo Tema",
        "mas_crea_temas" to "Presiona el + para crear un nuevo tema",
        "no_temas_encontrados" to "No se encontraron temas",
        "nuevo_tema" to "Nuevo Tema",
        "nombre_tema" to "Nombre del Tema",
        "cargando_tema" to "(cargando tema…)",
        "hilo_no_encontrado" to "Hilo no encontrado",
        "clave_no_disponible" to "(clave no disponible)",
        "clave_tema_no_disponible" to "(clave o tema no disponible)",
        "nuevo_hilo" to "Nuevo Hilo",
        "titulo_nuevo_hilo" to "Título del Hilo",
        "tema_no_encontrado" to "Tema no encontrado",







        "cambiar_idioma" to "Cambiar Idioma",
        "escribe_mensaje" to "Escribe un mensaje...",

        // Pantalla de Contactos
        "nuevo_contacto" to "Nuevo Contacto",
        "nuevo_chat" to "Nuevo Chat",
        "contactos" to "Contactos",
        "nombre_label" to "Nombre:",
        "alias_label" to "Alias:",
        "introduce_idContacto" to "Introduce el idContacto:",
        "id_contacto" to "idContacto",
        "guardar" to "Guardar",
        "cancelar" to "Cancelar",
        "crear_chat" to "Crear Chat",
        "selecciona_contactos_chat" to "Selecciona los contactos para el chat:",

        // Pantalla de Chat
        "nuevo_chat" to "Nuevo Chat",
        "agregar_participante" to "Agregar Participante",
        "crear_chat" to "Crear Chat",

        // Navegación y menús
        "login" to "Iniciar Sesión",
        "registro" to "Registro",
        "contactos" to "Contactos",
        "ajustes" to "Ajustes",
        "usuarios" to "Usuarios",
        "restablecer" to "Restablecer Contraseña",

        // Pantalla de Cambio de Tema / Ajustes de Tema
        "ajustes_tema" to "Ajustes / Tema",
        "modo" to "Modo",
        "modo_oscuro" to "Oscuro",
        "modo_claro" to "Claro",
        "colores" to "Colores",
        "tema_azul" to "Azul",
        "tema_amarillo" to "Amarillo",
        "tema_verde" to "Verde",
        "tema_rojo" to "Rojo",
        "tema_morado" to "Morado",
        "tema_gris" to "Gris",
        "tema_naranja" to "Naranja",

        // Pantalla de Ajustes / Ayuda
        "enviar_reporte" to "Enviar Reporte",
        "reporte_enviado" to "Reporte enviado",
        "por_favor" to "Por favor",

        // Otros textos generales
        "cambiar_tema" to "Cambiar Tema",
        "app_title" to "ConneXus",

        // Pantalla de Foro
        "foro" to "Foro",
        "nuevo_tema" to "Nuevo Tema",
        "agregar_tema" to "Agregar Tema",

        // Pantalla de Tema del Foro
        "tema_del_foro" to "Tema del Foro",
        "nuevo_mensaje" to "Nuevo Mensaje",
        "enviar" to "Enviar",

        // Otros textos de la interfaz
        "sin_resultados" to "No hay mensajes",
        "por_favor_completa" to "Por favor, completa el campo",

        // DefaultTopBar y navegación
        "atras" to "Atrás",
        "chats" to "Chats",

        // Pantalla de Usuarios
        "mostrar_usuarios" to "Mostrar Usuarios",
        "nombre_label" to "Nombre:",
        "alias_label" to "Alias:",
        "activo_label" to "Activo:",

        // Pantalla Registro
        "nombre" to "Nombre",
        "email" to "Email",
        "contrasena" to "Contraseña",
        "confirmar_contrasena" to "Confirmar Contraseña",
        "registrar" to "Registrar",
        "error_contrasenas" to "Las contraseñas no coinciden o están vacías",

        // Pantalla Login
        "iniciar_sesion" to "Iniciar Sesión",

        // Pantalla Ajustes / Control de Cuentas
        "ajustes_control_cuentas" to "Ajustes / Control cuentas",

        // Pantalla Ajustes / Ayuda
        "ajustes_ayuda" to "Ajustes / Ayuda",
        "escribe_reporte" to "Escribe tu reporte",
        "reporte_vacio" to "Por favor, escribe algo",

        // Pantalla Email no existe
        "email_no_existe" to "Email no existe",
        "correo_no_registrado" to "La dirección de correo no está registrada.",
        "verifica_correo_registro" to "Verifica que hayas escrito bien tu correo o regístrate.",
        "ir_a_registro" to "Ir a Registro",

        // Pantalla Email en el Sistema
        "email_en_sistema_titulo" to "Email en el Sistema",
        "email_en_sistema_mensaje" to "Email en el Sistema",
        "email_no_existe_titulo" to "Email no existe",
        "email_no_existe_mensaje" to "Email no existe en el Sistema",
        "codigo_verificacion" to "Código de Verificación",
        "restablecer_contrasena" to "Restablecer Contraseña",
        "cancelar" to "Cancelar",

        // Pantalla Restablecer Contraseña
        "enviar_correo" to "Enviar Correo",
        "error_correo_vacio" to "Debes ingresar un correo",
        "degug_restablecer_ok" to "Degug: Restablecer OK",
        "degug_restablecer_fail" to "Degug: Restablecer FAIL",

        // Pantalla Home
        "ir_a_login" to "Ir a Login",
        "ir_a_registro_home" to "Ir a Registro",
        "contactos_home" to "Contactos",
        "ajustes_home" to "Ajustes",
        "usuarios_home" to "Usuarios",

        // Otros textos extraídos del código
        "nuevo_contacto" to "Nuevo Contacto",
        "guardar" to "Guardar",
        "alias_privado" to "Alias Privado",
        "alias_publico" to "Alias Público",
        "descripcion" to "Descripción",
        "modificar" to "Modificar",
        "aplicar" to "Aplicar",
        "cerrar_sesion" to "Cerrar Sesión",
        "cambiar_modo_oscuro_tema" to "Cambiar Modo Oscuro / Tema",
        "cambiar_fuente" to "Cambiar Fuente",
        "eliminar_chats" to "(Eliminar Chats)",
        "control_de_cuentas" to "(Control de Cuentas)",
        "ayuda" to "Ayuda",
        /**
         * Localization strings for the Spanish language.
         */
        /**
         * Localization strings for the Spanish language.
         */
        "olvidaste_contrasena" to "¿Olvidaste tu contraseña?",
        "registrarse" to "Registrarse",
        "acceder" to "Acceder",
        "debug_ir_a_contactos" to "Debug: Ir a Contactos",
        "debug_ajustes_control_cuentas" to "Debug: Ajustes control cuentas",
        "lista_de_cuentas" to "Lista de Cuentas",
        "lista_de_preguntas_frecuentes" to "Lista de Preguntas Frecuentes",
        "no_puedo_agregar_a_alguien" to "No puedo agregar a alguien",
        "me_han_silenciado" to "Me han silenciado",
        "como_cambio_mi_alias_publico" to "Cómo cambio mi alias público",
        "puedo_cambiar_mi_contrasena" to "¿Puedo cambiar mi contraseña?",
        "vendeis_mis_datos" to "¿Vendéis mis datos?",
        "envia_un_reporte" to "Envía un reporte",
        "persona" to "Persona",
        "avatar" to "Avatar",
        "cambiar_a_tema_azul" to "Cambiar a tema azul",
        "cambiar_a_tema_amarillo" to "Cambiar a tema amarillo",
        "cambiar_a_tema_verde" to "Cambiar a tema verde",
        "cambiar_a_tema_rojo" to "Cambiar a tema rojo",
        "cambiar_a_tema_morado" to "Cambiar a tema morado",
        "cambiar_a_tema_gris" to "Cambiar a tema gris",
        "cambiar_a_tema_naranja" to "Cambiar a tema naranja",

        // Nuevos strings de la pantalla de Idiomas
        "idiomas" to "Idiomas",
        "espanol" to "Español",
        "ingles" to "Inglés",
        "portugues" to "Portugués",
        "frances" to "Francés",
        "aleman" to "Alemán",
        "italiano" to "Italiano",

        // Nuevos strings para las pantallas de conversación
        "nombre_del_chat" to "Nombre del Chat",
        "agregar_participante" to "Agregar Participante",
        "agregar" to "Agregar",
        "participantes" to "Participantes:",
        "chat_room" to "Chat Room",
        "nuevo_mensaje" to "Nuevo Mensaje",
        "enviar" to "Enviar",
        "agregar_chat" to "Agregar Chat",
        "chat" to "Chat:",
        "participantes_chat" to "Participantes: ",

        // Nuevos strings para las pantallas de ajustes
        "lista_de_cuentas" to "Lista de Cuentas",
        "envia_un_reporte" to "Envía un reporte",
        "escribe_tu_reporte" to "Escribe tu reporte",
        "reporte_vacio" to "Por favor, escribe algo",
        "reporte_enviado" to "Reporte enviado",
        "modo_oscuro" to "Modo Oscuro",
        "modo_claro" to "Modo Claro",
        "cambiar_a_tema_azul" to "Cambiar a tema azul",
        "cambiar_a_tema_amarillo" to "Cambiar a tema amarillo",
        "cambiar_a_tema_verde" to "Cambiar a tema verde",
        "cambiar_a_tema_rojo" to "Cambiar a tema rojo",
        "cambiar_a_tema_morado" to "Cambiar a tema morado",
        "cambiar_a_tema_gris" to "Cambiar a tema gris",
        "cambiar_a_tema_naranja" to "Cambiar a tema naranja",

    // Nuevos strings para la interfaz de Tipos de Fuente
    "ajustes_fuente" to "Ajustes de Fuente",
    "fuente" to "Fuente",
    "cambiar_fuente" to "Cambiar Fuente",
    "default_font" to "Predeterminado",
    "serif_font" to "Serif",
    "monospace_font" to "Monoespaciada",
    "cursive_font" to "Cursiva",
    "sans_serif_font" to "Sans Serif"
    )
)

/**
 * Localization strings for the English language.
 */
// Inglés

/**
 * Localization strings for the English language.
 * Contains a mapping of keys to English translations.
 */
val ingles = Idioma(
    palabras = mapOf(
    "escribe_mensaje" to "Write a message",
    "cambiar_idioma" to "Change Language",
    "nuevo_contacto" to "New Contact",
    "nuevo_chat" to "New Chat",
    "contactos" to "Contacts",
    "nombre_label" to "Name:",
    "alias_label" to "Alias:",
    "introduce_idContacto" to "Enter the contact ID:",
    "id_contacto" to "Contact ID",
    "guardar" to "Save",
    "cancelar" to "Cancel",
    "crear_chat" to "Create Chat",
    "selecciona_contactos_chat" to "Select contacts for the chat:",
    "agregar_participante" to "Add Participant",
    "login" to "Login",
    "registro" to "Register",
    "ajustes" to "Settings",
    "usuarios" to "Users",
    "restablecer" to "Reset Password",
    "ajustes_tema" to "Settings / Theme",
    "modo" to "Mode",
    "modo_oscuro" to "Dark",
    "modo_claro" to "Light",
    "colores" to "Colors",
    "tema_azul" to "Blue",
    "tema_amarillo" to "Yellow",
    "tema_verde" to "Green",
    "tema_rojo" to "Red",
    "tema_morado" to "Purple",
    "tema_gris" to "Gray",
    "tema_naranja" to "Orange",
    "enviar_reporte" to "Send Report",
    "reporte_enviado" to "Report sent",
    "por_favor" to "Please",
    "cambiar_tema" to "Change Theme",
    "app_title" to "ConneXus",
    "foro" to "Forum",
    "nuevo_tema" to "New Topic",
    "agregar_tema" to "Add Topic",
    "tema_del_foro" to "Forum Topic",
    "nuevo_mensaje" to "New Message",
    "enviar" to "Send",
    "sin_resultados" to "No messages",
    "por_favor_completa" to "Please complete the field",
    "atras" to "Back",
    "chats" to "Chats",
    "mostrar_usuarios" to "Show Users",
    "activo_label" to "Active:",
    "nombre" to "Name",
    "email" to "Email",
    "contrasena" to "Password",
    "confirmar_contrasena" to "Confirm Password",
    "registrar" to "Register",
    "error_contrasenas" to "Passwords do not match or are empty",
    "iniciar_sesion" to "Login",
    "ajustes_control_cuentas" to "Settings / Account Control",
    "ajustes_ayuda" to "Settings / Help",
    "escribe_reporte" to "Write your report",
    "reporte_vacio" to "Please, write something",
    "email_en_sistema_mensaje" to "Email exists",
    "email_no_existe_titulo" to "Email does not exist",
    "email_no_existe_mensaje" to "Email does not exist",
    "email_en_sistema_titulo" to "Email in System",
    "correo_no_registrado" to "Email address is not registered.",
    "verifica_correo_registro" to "Check if your email is correct or register.",
    "ir_a_registro" to "Go to Register",
    "email_en_sistema" to "Email in System",
    "codigo_verificacion" to "Verification Code",
    "restablecer_contrasena" to "Reset Password",
    "enviar_correo" to "Send Email",
    "error_correo_vacio" to "You must enter an email",
    "degug_restablecer_ok" to "Debug: Reset OK",
    "degug_restablecer_fail" to "Debug: Reset FAIL",
    "ir_a_login" to "Go to Login",
    "ir_a_registro_home" to "Go to Register",
    "contactos_home" to "Contacts",
    "ajustes_home" to "Settings",
    "usuarios_home" to "Users",
    "alias_privado" to "Private Alias",
    "alias_publico" to "Public Alias",
    "descripcion" to "Description",
    "modificar" to "Edit",
    "aplicar" to "Apply",
    "cerrar_sesion" to "Logout",
    "cambiar_modo_oscuro_tema" to "Change Dark Mode / Theme",
    "cambiar_fuente" to "Change Font",
    "eliminar_chats" to "(Delete Chats)",
    "control_de_cuentas" to "(Account Control)",
    "ayuda" to "Help",
    "olvidaste_contrasena" to "Forgot your password?",
    "registrarse" to "Sign Up",
    "acceder" to "Log In",
    "debug_ir_a_contactos" to "Debug: Go to Contacts",
    "debug_ajustes_control_cuentas" to "Debug: Settings Account Control",
    "lista_de_cuentas" to "Account List",
    "lista_de_preguntas_frecuentes" to "FAQ List",
    "no_puedo_agregar_a_alguien" to "Can't add someone",
    "me_han_silenciado" to "I've been muted",
    "como_cambio_mi_alias_publico" to "How to change my public alias",
    "puedo_cambiar_mi_contrasena" to "Can I change my password?",
    "vendeis_mis_datos" to "Do you sell my data?",
    "envia_un_reporte" to "Send a report",
    "persona" to "Person",
    "avatar" to "Avatar",
    "cambiar_a_tema_azul" to "Switch to blue theme",
    "cambiar_a_tema_amarillo" to "Switch to yellow theme",
    "cambiar_a_tema_verde" to "Switch to green theme",
    "cambiar_a_tema_rojo" to "Switch to red theme",
    "cambiar_a_tema_morado" to "Switch to purple theme",
    "cambiar_a_tema_gris" to "Switch to gray theme",
    "cambiar_a_tema_naranja" to "Switch to orange theme",
    "idiomas" to "Languages",
    "espanol" to "Spanish",
    "ingles" to "English",
    "portugues" to "Portuguese",
    "frances" to "French",
    "aleman" to "German",
    "italiano" to "Italian",
    "nombre_del_chat" to "Chat Name",
    "agregar" to "Add",
    "participantes" to "Participants:",
    "chat_room" to "Chat Room",
    "agregar_chat" to "Add Chat",
    "chat" to "Chat:",
    "participantes_chat" to "Participants: ",
    "escribe_tu_reporte" to "Write your report",

    // Nuevos strings para la interfaz de Tipos de Fuente
    "ajustes_fuente" to "Font Settings",
    "fuente" to "Font",
    "cambiar_fuente" to "Change Font",
    "default_font" to "Default",
    "serif_font" to "Serif",
    "monospace_font" to "Monospace",
    "cursive_font" to "Cursive",
    "sans_serif_font" to "Sans Serif"
    )
)

/**
 * Localization strings for the Portuguese language.
 */
// Portugués

/**
 * Localization strings for the Portuguese language.
 * Contains a mapping of keys to Portuguese translations.
 */
val portugues = Idioma(
    palabras = mapOf(
        // Pantalla de Contactos
        "escribe_mensaje" to "Escreva uma mensagem",
        "cambiar_idioma" to "Mudar Idioma",
        "nuevo_contacto" to "Novo Contato",
        "nuevo_chat" to "Novo Chat",
        "contactos" to "Contatos",
        "nombre_label" to "Nome:",
        "alias_label" to "Apelido:",
        "introduce_idContacto" to "Digite o ID do Contato:",
        "id_contacto" to "ID do Contato",
        "guardar" to "Salvar",
        "cancelar" to "Cancelar",
        "crear_chat" to "Criar Chat",
        "selecciona_contactos_chat" to "Selecione os contatos para o chat:",

        // Pantalla de Chat
        "nuevo_chat" to "Novo Chat",
        "agregar_participante" to "Adicionar Participante",
        "crear_chat" to "Criar Chat",

        // Navegación y menús
        "login" to "Entrar",
        "registro" to "Cadastro",
        "contactos" to "Contatos",
        "ajustes" to "Configurações",
        "usuarios" to "Usuários",
        "restablecer" to "Redefinir Senha",

        // Pantalla de Cambio de Tema / Ajustes de Tema
        "ajustes_tema" to "Configurações / Tema",
        "modo" to "Modo",
        "modo_oscuro" to "Escuro",
        "modo_claro" to "Claro",
        "colores" to "Cores",
        "tema_azul" to "Azul",
        "tema_amarillo" to "Amarelo",
        "tema_verde" to "Verde",
        "tema_rojo" to "Vermelho",
        "tema_morado" to "Roxo",
        "tema_gris" to "Cinza",
        "tema_naranja" to "Laranja",

        // Pantalla de Ajustes / Ayuda
        "enviar_reporte" to "Enviar Relatório",
        "reporte_enviado" to "Relatório enviado",
        "por_favor" to "Por favor",

        // Otros textos generales
        "cambiar_tema" to "Mudar Tema",
        "app_title" to "ConneXus",

        // Pantalla de Foro
        "foro" to "Fórum",
        "nuevo_tema" to "Novo Tópico",
        "agregar_tema" to "Adicionar Tópico",

        // Pantalla de Tema del Foro
        "tema_del_foro" to "Tópico do Fórum",
        "nuevo_mensaje" to "Nova Mensagem",
        "enviar" to "Enviar",

        // Otros textos de la interfaz
        "sin_resultados" to "Sem mensagens",
        "por_favor_completa" to "Por favor, preencha o campo",

        // DefaultTopBar y navegación
        "atras" to "Voltar",
        "chats" to "Chats",

        // Pantalla de Usuarios
        "mostrar_usuarios" to "Mostrar Usuários",
        "nombre_label" to "Nome:",
        "alias_label" to "Apelido:",
        "activo_label" to "Ativo:",

        // Pantalla Registro
        "nombre" to "Nome",
        "email" to "Email",
        "contrasena" to "Senha",
        "confirmar_contrasena" to "Confirmar Senha",
        "registrar" to "Cadastrar",
        "error_contrasenas" to "As senhas não coincidem ou estão vazias",

        // Pantalla Login
        "iniciar_sesion" to "Entrar",

        // Pantalla Ajustes / Control de Cuentas
        "ajustes_control_cuentas" to "Configurações / Controle de contas",

        // Pantalla Ajustes / Ayuda
        "ajustes_ayuda" to "Configurações / Ajuda",
        "escribe_reporte" to "Escreva seu relatório",
        "reporte_vacio" to "Por favor, escreva algo",

        // Pantalla Email
        "email_no_existe_mensaje" to "Email não existe",
        "email_no_existe_titulo" to "O endereço de email não está registrado.",
        "verifica_correo_registro" to "Verifique se você digitou seu email corretamente ou cadastre-se.",
        "ir_a_registro" to "Ir para Cadastro",
        "email_en_sistema_titulo" to "Email no Sistema",
        "email_en_sistema_mensaje" to "Email no Sistema",
        "codigo_verificacion" to "Código de Verificação",
        "restablecer_contrasena" to "Redefinir Senha",
        "cancelar" to "Cancelar",

        // Pantalla Restablecer Contraseña
        "enviar_correo" to "Enviar Email",
        "error_correo_vacio" to "Você deve inserir um email",
        "degug_restablecer_ok" to "Debug: Redefinir OK",
        "degug_restablecer_fail" to "Debug: Redefinir FAIL",

        // Pantalla Home
        "ir_a_login" to "Ir para Login",
        "ir_a_registro_home" to "Ir para Cadastro",
        "contactos_home" to "Contatos",
        "ajustes_home" to "Configurações",
        "usuarios_home" to "Usuários",

        // Otros textos extraídos del código
        "nuevo_contacto" to "Novo Contato",
        "guardar" to "Salvar",
        "alias_privado" to "Apelido Privado",
        "alias_publico" to "Apelido Público",
        "descripcion" to "Descrição",
        "modificar" to "Modificar",
        "aplicar" to "Aplicar",
        "cerrar_sesion" to "Sair",
        "cambiar_modo_oscuro_tema" to "Mudar Modo Escuro / Tema",
        "cambiar_fuente" to "Mudar Fonte",
        "eliminar_chats" to "(Excluir Chats)",
        "control_de_cuentas" to "(Controle de Contas)",
        "ayuda" to "Ajuda",
        "olvidaste_contrasena" to "Esqueceu sua senha?",
        "registrarse" to "Cadastrar-se",
        "acceder" to "Acessar",
        "debug_ir_a_contactos" to "Debug: Ir para Contatos",
        "debug_ajustes_control_cuentas" to "Debug: Configurações Controle de contas",
        "lista_de_cuentas" to "Lista de Contas",
        "lista_de_preguntas_frecuentes" to "Lista de Perguntas Frequentes",
        "no_puedo_agregar_a_alguien" to "Não consigo adicionar alguém",
        "me_han_silenciado" to "Fui silenciado",
        "como_cambio_mi_alias_publico" to "Como mudo meu apelido público",
        "puedo_cambiar_mi_contrasena" to "Posso mudar minha senha?",
        "vendeis_mis_datos" to "Vocês vendem meus dados?",
        "envia_un_reporte" to "Envie um relatório",
        "persona" to "Pessoa",
        "avatar" to "Avatar",
        "cambiar_a_tema_azul" to "Mudar para tema azul",
        "cambiar_a_tema_amarillo" to "Mudar para tema amarelo",
        "cambiar_a_tema_verde" to "Mudar para tema verde",
        "cambiar_a_tema_rojo" to "Mudar para tema vermelho",
        "cambiar_a_tema_morado" to "Mudar para tema roxo",
        "cambiar_a_tema_gris" to "Mudar para tema cinza",
        "cambiar_a_tema_naranja" to "Mudar para tema laranja",

        // Nuevos strings de la pantalla de Idiomas
        "idiomas" to "Idiomas",
        "espanol" to "Espanhol",
        "ingles" to "Inglês",
        "portugues" to "Português",
        "frances" to "Francês",
        "aleman" to "Alemão",
        "italiano" to "Italiano",

        // Nuevos strings para las pantallas de conversación
        "nombre_del_chat" to "Nome do Chat",
        "agregar_participante" to "Adicionar Participante",
        "agregar" to "Adicionar",
        "participantes" to "Participantes:",
        "chat_room" to "Sala de Chat",
        "nuevo_mensaje" to "Nova Mensagem",
        "enviar" to "Enviar",
        "agregar_chat" to "Adicionar Chat",
        "chat" to "Chat:",
        "participantes_chat" to "Participantes: ",

        // Nuevos strings para las pantallas de ajustes
        "lista_de_cuentas" to "Lista de Contas",
        "envia_un_reporte" to "Envie um relatório",
        "escribe_tu_reporte" to "Escreva seu relatório",
        "reporte_vacio" to "Por favor, escreva algo",
        "reporte_enviado" to "Relatório enviado",
        "modo_oscuro" to "Modo Escuro",
        "modo_claro" to "Modo Claro",
        "cambiar_a_tema_azul" to "Mudar para tema azul",
        "cambiar_a_tema_amarillo" to "Mudar para tema amarelo",
        "cambiar_a_tema_verde" to "Mudar para tema verde",
        "cambiar_a_tema_rojo" to "Mudar para tema vermelho",
        "cambiar_a_tema_morado" to "Mudar para tema roxo",
        "cambiar_a_tema_gris" to "Mudar para tema cinza",
        "cambiar_a_tema_naranja" to "Mudar para tema laranja",

        // Nuevos strings para la interfaz de Tipos de Fuente
        "ajustes_fuente" to "Configurações de fonte",
        "fuente" to "Fonte",
        "cambiar_fuente" to "Alterar Fonte",
        "default_font" to "Padrão",
        "serif_font" to "Serif",
        "monospace_font" to "Monoespaçada",
        "cursive_font" to "Cursiva",
        "sans_serif_font" to "Sem Serif"
    )
)

/**
 * Localization strings for the French language.
 */
// Francés

/**
 * Localization strings for the French language.
 * Contains a mapping of keys to French translations.
 */
val frances = Idioma(
    palabras = mapOf(
        // Écran des Contacts
        "escribe_mensaje" to "Écrire un message",
        "cambiar_idioma" to "Changer de Langue",
        "nuevo_contacto" to "Nouveau Contact",
        "nuevo_chat" to "Nouveau Chat",
        "contactos" to "Contacts",
        "nombre_label" to "Nom:",
        "alias_label" to "Alias:",
        "introduce_idContacto" to "Entrez l'ID du contact:",
        "id_contacto" to "ID du Contact",
        "guardar" to "Enregistrer",
        "cancelar" to "Annuler",
        "crear_chat" to "Créer un chat",
        "selecciona_contactos_chat" to "Sélectionnez les contacts pour le chat:",

        // Écran de Chat
        "nuevo_chat" to "Nouveau Chat",
        "agregar_participante" to "Ajouter un participant",
        "crear_chat" to "Créer un chat",

        // Navigation et menus
        "login" to "Se connecter",
        "registro" to "Inscription",
        "contactos" to "Contacts",
        "ajustes" to "Paramètres",
        "usuarios" to "Utilisateurs",
        "restablecer" to "Réinitialiser le mot de passe",

        // Écran Changement de Thème / Réglages de Thème
        "ajustes_tema" to "Paramètres / Thème",
        "modo" to "Mode",
        "modo_oscuro" to "Sombre",
        "modo_claro" to "Clair",
        "colores" to "Couleurs",
        "tema_azul" to "Bleu",
        "tema_amarillo" to "Jaune",
        "tema_verde" to "Vert",
        "tema_rojo" to "Rouge",
        "tema_morado" to "Violet",
        "tema_gris" to "Gris",
        "tema_naranja" to "Orange",

        // Écran Réglages / Aide
        "enviar_reporte" to "Envoyer un rapport",
        "reporte_enviado" to "Rapport envoyé",
        "por_favor" to "S'il vous plaît",

        // Autres textes généraux
        "cambiar_tema" to "Changer le thème",
        "app_title" to "ConneXus",

        // Écran du Forum
        "foro" to "Forum",
        "nuevo_tema" to "Nouveau sujet",
        "agregar_tema" to "Ajouter un sujet",

        // Écran du Sujet du Forum
        "tema_del_foro" to "Sujet du forum",
        "nuevo_mensaje" to "Nouveau message",
        "enviar" to "Envoyer",

        // Autres textes de l'interface
        "sin_resultados" to "Aucun message",
        "por_favor_completa" to "Veuillez remplir le champ",

        // Barre de navigation
        "atras" to "Retour",
        "chats" to "Chats",

        // Écran des Utilisateurs
        "mostrar_usuarios" to "Afficher les utilisateurs",
        "nombre_label" to "Nom:",
        "alias_label" to "Alias:",
        "activo_label" to "Actif:",

        // Écran d'Inscription
        "nombre" to "Nom",
        "email" to "Email",
        "contrasena" to "Mot de passe",
        "confirmar_contrasena" to "Confirmer le mot de passe",
        "registrar" to "S'inscrire",
        "error_contrasenas" to "Les mots de passe ne correspondent pas ou sont vides",

        // Écran de Connexion
        "iniciar_sesion" to "Se connecter",

        // Écran Réglages / Contrôle des Comptes
        "ajustes_control_cuentas" to "Paramètres / Contrôle des comptes",

        // Écran Réglages / Aide
        "ajustes_ayuda" to "Paramètres / Aide",
        "escribe_reporte" to "Écrivez votre rapport",
        "reporte_vacio" to "Veuillez écrire quelque chose",

        // Écran Email Inexistant
        "email_en_sistema_titulo" to "Email dans le Système",
        "email_en_sistema_mensaje" to "Email dans le Système",
        "email_no_existe_titulo" to "Email n'existe pas",
        "email_no_existe_mensaje" to "Email n'existe pas",
        "verifica_correo_registro" to "Vérifiez que vous avez correctement saisi votre email ou inscrivez-vous.",
        "ir_a_registro" to "Aller à l'inscription",

        // Écran Email dans le Système
        "email_en_sistema" to "Email dans le système",
        "codigo_verificacion" to "Code de vérification",
        "restablecer_contrasena" to "Réinitialiser le mot de passe",
        "cancelar" to "Annuler",

        // Écran Réinitialiser le Mot de Passe
        "enviar_correo" to "Envoyer l'email",
        "error_correo_vacio" to "Vous devez entrer un email",
        "degug_restablecer_ok" to "Debug: Réinitialisation OK",
        "degug_restablecer_fail" to "Debug: Réinitialisation ÉCHOUÉE",

        // Écran d'Accueil
        "ir_a_login" to "Aller à la connexion",
        "ir_a_registro_home" to "Aller à l'inscription",
        "contactos_home" to "Contacts",
        "ajustes_home" to "Paramètres",
        "usuarios_home" to "Utilisateurs",

        // Autres textes extraits du code
        "nuevo_contacto" to "Nouveau Contact",
        "guardar" to "Enregistrer",
        "alias_privado" to "Alias privé",
        "alias_publico" to "Alias public",
        "descripcion" to "Description",
        "modificar" to "Modifier",
        "aplicar" to "Appliquer",
        "cerrar_sesion" to "Se déconnecter",
        "cambiar_modo_oscuro_tema" to "Changer le mode sombre / thème",
        "cambiar_fuente" to "Changer la police",
        "eliminar_chats" to "(Supprimer les chats)",
        "control_de_cuentas" to "(Contrôle des comptes)",
        "ayuda" to "Aide",
        "olvidaste_contrasena" to "Vous avez oublié votre mot de passe ?",
        "registrarse" to "S'inscrire",
        "acceder" to "Accéder",
        "debug_ir_a_contactos" to "Debug: Aller aux contacts",
        "debug_ajustes_control_cuentas" to "Debug: Paramètres contrôle des comptes",
        "lista_de_cuentas" to "Liste des comptes",
        "lista_de_preguntas_frecuentes" to "Liste des questions fréquentes",
        "no_puedo_agregar_a_alguien" to "Je ne peux pas ajouter quelqu'un",
        "me_han_silenciado" to "J'ai été réduit au silence",
        "como_cambio_mi_alias_publico" to "Comment changer mon alias public",
        "puedo_cambiar_mi_contrasena" to "Puis-je changer mon mot de passe ?",
        "vendeis_mis_datos" to "Vendez-vous mes données ?",
        "envia_un_reporte" to "Envoyez un rapport",
        "persona" to "Personne",
        "avatar" to "Avatar",
        "cambiar_a_tema_azul" to "Changer vers le thème bleu",
        "cambiar_a_tema_amarillo" to "Changer vers le thème jaune",
        "cambiar_a_tema_verde" to "Changer vers le thème vert",
        "cambiar_a_tema_rojo" to "Changer vers le thème rouge",
        "cambiar_a_tema_morado" to "Changer vers le thème violet",
        "cambiar_a_tema_gris" to "Changer vers le thème gris",
        "cambiar_a_tema_naranja" to "Changer vers le thème orange",

        // Nouveaux textes de l'écran Langues
        "idiomas" to "Langues",
        "espanol" to "Espagnol",
        "ingles" to "Anglais",
        "portugues" to "Portugais",
        "frances" to "Français",
        "aleman" to "Allemand",
        "italiano" to "Italien",

        // Nouveaux textes pour les écrans de conversation
        "nombre_del_chat" to "Nom du chat",
        "agregar_participante" to "Ajouter un participant",
        "agregar" to "Ajouter",
        "participantes" to "Participants:",
        "chat_room" to "Salle de chat",
        "nuevo_mensaje" to "Nouveau message",
        "enviar" to "Envoyer",
        "agregar_chat" to "Ajouter un chat",
        "chat" to "Chat:",
        "participantes_chat" to "Participants: ",

        // Nouveaux textes pour les écrans des réglages
        "lista_de_cuentas" to "Liste des comptes",
        "envia_un_reporte" to "Envoyez un rapport",
        "escribe_tu_reporte" to "Écrivez votre rapport",
        "reporte_vacio" to "Veuillez écrire quelque chose",
        "reporte_enviado" to "Rapport envoyé",
        "modo_oscuro" to "Mode sombre",
        "modo_claro" to "Mode clair",
        "cambiar_a_tema_azul" to "Changer vers le thème bleu",
        "cambiar_a_tema_amarillo" to "Changer vers le thème jaune",
        "cambiar_a_tema_verde" to "Changer vers le thème vert",
        "cambiar_a_tema_rojo" to "Changer vers le thème rouge",
        "cambiar_a_tema_morado" to "Changer vers le thème violet",
        "cambiar_a_tema_gris" to "Changer vers le thème gris",
        "cambiar_a_tema_naranja" to "Changer vers le thème orange",

        // Nouveaux textes pour l'interface des Types de Police
        "ajustes_fuente" to "Paramètres de police",
        "fuente" to "Police",
        "cambiar_fuente" to "Changer la police",
        "default_font" to "Par défaut",
        "serif_font" to "Serif", // Vous pouvez aussi utiliser « Avec empattement » si vous préférez\n    "monospace_font" to "Monospace",
        "cursive_font" to "Cursive",
        "sans_serif_font" to "Sans empattement"
    )
)

/**
 * Localization strings for the German language.
 */
// Alemán

/**
 * Localization strings for the German language.
 * Contains a mapping of keys to German translations.
 */
val aleman = Idioma(
    palabras = mapOf(
        // Kontakte-Bildschirm
        "escribe_mensaje" to "Nachricht schreiben",
        "cambiar_idioma" to "Sprache ändern",
        "nuevo_contacto" to "Neuer Kontakt",
        "nuevo_chat" to "Neuer Chat",
        "contactos" to "Kontakte",
        "nombre_label" to "Name:",
        "alias_label" to "Alias:",
        "introduce_idContacto" to "Geben Sie die Kontakt-ID ein:",
        "id_contacto" to "Kontakt-ID",
        "guardar" to "Speichern",
        "cancelar" to "Abbrechen",
        "crear_chat" to "Chat erstellen",
        "selecciona_contactos_chat" to "Wählen Sie die Kontakte für den Chat aus:",

        // Chat-Bildschirm
        "nuevo_chat" to "Neuer Chat",
        "agregar_participante" to "Teilnehmer hinzufügen",
        "crear_chat" to "Chat erstellen",

        // Navigation und Menüs
        "login" to "Anmelden",
        "registro" to "Registrierung",
        "contactos" to "Kontakte",
        "ajustes" to "Einstellungen",
        "usuarios" to "Benutzer",
        "restablecer" to "Passwort zurücksetzen",

        // Einstellungen / Themen-Bildschirm
        "ajustes_tema" to "Einstellungen / Thema",
        "modo" to "Modus",
        "modo_oscuro" to "Dunkel",
        "modo_claro" to "Hell",
        "colores" to "Farben",
        "tema_azul" to "Blau",
        "tema_amarillo" to "Gelb",
        "tema_verde" to "Grün",
        "tema_rojo" to "Rot",
        "tema_morado" to "Lila",
        "tema_gris" to "Grau",
        "tema_naranja" to "Orange",

        // Einstellungen / Hilfe
        "enviar_reporte" to "Bericht senden",
        "reporte_enviado" to "Bericht gesendet",
        "por_favor" to "Bitte",

        // Allgemeine Texte
        "cambiar_tema" to "Thema ändern",
        "app_title" to "ConneXus",

        // Forum-Bildschirm
        "foro" to "Forum",
        "nuevo_tema" to "Neues Thema",
        "agregar_tema" to "Thema hinzufügen",

        // Forum-Thema-Bildschirm
        "tema_del_foro" to "Forum-Thema",
        "nuevo_mensaje" to "Neue Nachricht",
        "enviar" to "Senden",

        // Interface-Texte
        "sin_resultados" to "Keine Nachrichten",
        "por_favor_completa" to "Bitte füllen Sie das Feld aus",

        // Navigationsleiste
        "atras" to "Zurück",
        "chats" to "Chats",

        // Benutzer-Bildschirm
        "mostrar_usuarios" to "Benutzer anzeigen",
        "nombre_label" to "Name:",
        "alias_label" to "Alias:",
        "activo_label" to "Aktiv:",

        // Registrierungs-Bildschirm
        "nombre" to "Name",
        "email" to "Email",
        "contrasena" to "Passwort",
        "confirmar_contrasena" to "Passwort bestätigen",
        "registrar" to "Registrieren",
        "error_contrasenas" to "Die Passwörter stimmen nicht überein oder sind leer",

        // Login-Bildschirm
        "iniciar_sesion" to "Anmelden",

        // Einstellungen / Kontoverwaltung
        "ajustes_control_cuentas" to "Einstellungen / Kontoverwaltung",

        // Einstellungen / Hilfe
        "ajustes_ayuda" to "Einstellungen / Hilfe",
        "escribe_reporte" to "Schreiben Sie Ihren Bericht",
        "reporte_vacio" to "Bitte schreiben Sie etwas",

        // E-Mail existiert nicht
        "email_en_sistema_titulo" to "Email im System",
        "email_en_sistema_mensaje" to "Email im System",
        "email_no_existe_titulo" to "Email existiert nicht",
        "email_no_existe_mensaje" to "Email existiert nicht",
        "verifica_correo_registro" to "Überprüfen Sie, ob Sie Ihre E-Mail richtig eingegeben haben oder registrieren Sie sich.",
        "ir_a_registro" to "Zur Registrierung gehen",

        // E-Mail im System
        "email_en_sistema" to "Email im System",
        "codigo_verificacion" to "Bestätigungscode",
        "restablecer_contrasena" to "Passwort zurücksetzen",
        "cancelar" to "Abbrechen",

        // Passwort zurücksetzen
        "enviar_correo" to "Email senden",
        "error_correo_vacio" to "Sie müssen eine E-Mail eingeben",
        "degug_restablecer_ok" to "Debug: Zurücksetzen OK",
        "degug_restablecer_fail" to "Debug: Zurücksetzen FEHLGESCHLAGEN",

        // Startseite
        "ir_a_login" to "Zum Login gehen",
        "ir_a_registro_home" to "Zur Registrierung gehen",
        "contactos_home" to "Kontakte",
        "ajustes_home" to "Einstellungen",
        "usuarios_home" to "Benutzer",

        // Weitere Texte aus dem Code
        "nuevo_contacto" to "Neuer Kontakt",
        "guardar" to "Speichern",
        "alias_privado" to "Privater Alias",
        "alias_publico" to "Öffentlicher Alias",
        "descripcion" to "Beschreibung",
        "modificar" to "Bearbeiten",
        "aplicar" to "Anwenden",
        "cerrar_sesion" to "Abmelden",
        "cambiar_modo_oscuro_tema" to "Dunkelmodus / Thema wechseln",
        "cambiar_fuente" to "Schriftart ändern",
        "eliminar_chats" to "(Chats löschen)",
        "control_de_cuentas" to "(Kontoverwaltung)",
        "ayuda" to "Hilfe",
        "olvidaste_contrasena" to "Passwort vergessen?",
        "registrarse" to "Registrieren",
        "acceder" to "Einloggen",
        "debug_ir_a_contactos" to "Debug: Zu Kontakten gehen",
        "debug_ajustes_control_cuentas" to "Debug: Einstellungen Kontoverwaltung",
        "lista_de_cuentas" to "Liste der Konten",
        "lista_de_preguntas_frecuentes" to "Liste der häufig gestellten Fragen",
        "no_puedo_agregar_a_alguien" to "Ich kann niemanden hinzufügen",
        "me_han_silenciado" to "Ich wurde stummgeschaltet",
        "como_cambio_mi_alias_publico" to "Wie ändere ich meinen öffentlichen Alias",
        "puedo_cambiar_mi_contrasena" to "Kann ich mein Passwort ändern?",
        "vendeis_mis_datos" to "Verkauft ihr meine Daten?",
        "envia_un_reporte" to "Sende einen Bericht",
        "persona" to "Person",
        "avatar" to "Avatar",
        "cambiar_a_tema_azul" to "Wechsel zu blauem Thema",
        "cambiar_a_tema_amarillo" to "Wechsel zu gelbem Thema",
        "cambiar_a_tema_verde" to "Wechsel zu grünem Thema",
        "cambiar_a_tema_rojo" to "Wechsel zu rotem Thema",
        "cambiar_a_tema_morado" to "Wechsel zu lila Thema",
        "cambiar_a_tema_gris" to "Wechsel zu grauem Thema",
        "cambiar_a_tema_naranja" to "Wechsel zu orangem Thema",

        // Neue Texte des Sprachbildschirms
        "idiomas" to "Sprachen",
        "espanol" to "Spanisch",
        "ingles" to "Englisch",
        "portugues" to "Portugiesisch",
        "frances" to "Französisch",
        "aleman" to "Deutsch",
        "italiano" to "Italienisch",

        // Neue Texte für die Konversationsbildschirme
        "nombre_del_chat" to "Chat-Name",
        "agregar_participante" to "Teilnehmer hinzufügen",
        "agregar" to "Hinzufügen",
        "participantes" to "Teilnehmer:",
        "chat_room" to "Chatraum",
        "nuevo_mensaje" to "Neue Nachricht",
        "enviar" to "Senden",
        "agregar_chat" to "Chat hinzufügen",
        "chat" to "Chat:",
        "participantes_chat" to "Teilnehmer: ",

        // Neue Texte für die Einstellungsbildschirme
        "lista_de_cuentas" to "Liste der Konten",
        "envia_un_reporte" to "Sende einen Bericht",
        "escribe_tu_reporte" to "Schreibe deinen Bericht",
        "reporte_vacio" to "Bitte schreibe etwas",
        "reporte_enviado" to "Bericht gesendet",
        "modo_oscuro" to "Dunkelmodus",
        "modo_claro" to "Hellmodus",
        "cambiar_a_tema_azul" to "Wechsel zu blauem Thema",
        "cambiar_a_tema_amarillo" to "Wechsel zu gelbem Thema",
        "cambiar_a_tema_verde" to "Wechsel zu grünem Thema",
        "cambiar_a_tema_rojo" to "Wechsel zu rotem Thema",
        "cambiar_a_tema_morado" to "Wechsel zu lila Thema",
        "cambiar_a_tema_gris" to "Wechsel zu grauem Thema",
        "cambiar_a_tema_naranja" to "Wechsel zu orangem Thema",

        // Neue Texte für die Schriftarten-Schnittstelle
        "ajustes_fuente" to "Schrifteinstellungen",
        "fuente" to "Schriftart",
        "cambiar_fuente" to "Schriftart ändern",
        "default_font" to "Standard",
        "serif_font" to "Serifenschrift",
        "monospace_font" to "Monospace",
        "cursive_font" to "Kursiv",
        "sans_serif_font" to "Serifenlos"
    )
)

// Italiano
/**
 * Localization strings for the Italian language.
 */
 // Italiano
/**
 * Localization strings for the Italian language.
 * Contains a mapping of keys to Italian translations.
 */
val italiano = Idioma(
    palabras = mapOf(
        // Schermata Contatti
        "escribe_mensaje" to "Scrivi un messaggio",
        "cambiar_idioma" to "Cambia Lingua",
        "nuevo_contacto" to "Nuovo Contatto",
        "nuevo_chat" to "Nuovo Chat",
        "contactos" to "Contatti",
        "nombre_label" to "Nome:",
        "alias_label" to "Alias:",
        "introduce_idContacto" to "Inserisci l'ID Contatto:",
        "id_contacto" to "ID Contatto",
        "guardar" to "Salva",
        "cancelar" to "Annulla",
        "crear_chat" to "Crea Chat",
        "selecciona_contactos_chat" to "Seleziona i contatti per la chat:",

        // Schermata Chat
        "nuevo_chat" to "Nuovo Chat",
        "agregar_participante" to "Aggiungi Partecipante",
        "crear_chat" to "Crea Chat",

        // Navigazione e menu
        "login" to "Accedi",
        "registro" to "Registrazione",
        "contactos" to "Contatti",
        "ajustes" to "Impostazioni",
        "usuarios" to "Utenti",
        "restablecer" to "Reimposta la password",

        // Schermata Tema / Impostazioni del Tema
        "ajustes_tema" to "Impostazioni / Tema",
        "modo" to "Modalità",
        "modo_oscuro" to "Scuro",
        "modo_claro" to "Chiaro",
        "colores" to "Colori",
        "tema_azul" to "Blu",
        "tema_amarillo" to "Giallo",
        "tema_verde" to "Verde",
        "tema_rojo" to "Rosso",
        "tema_morado" to "Viola",
        "tema_gris" to "Grigio",
        "tema_naranja" to "Arancione",

        // Schermata Impostazioni / Aiuto
        "enviar_reporte" to "Invia Segnalazione",
        "reporte_enviado" to "Segnalazione inviata",
        "por_favor" to "Per favore",

        // Altri testi generali
        "cambiar_tema" to "Cambia Tema",
        "app_title" to "ConneXus",

        // Schermata Forum
        "foro" to "Forum",
        "nuevo_tema" to "Nuovo Tema",
        "agregar_tema" to "Aggiungi Tema",

        // Schermata Tema del Forum
        "tema_del_foro" to "Tema del Forum",
        "nuevo_mensaje" to "Nuovo Messaggio",
        "enviar" to "Invia",

        // Altri testi dell'interfaccia
        "sin_resultados" to "Nessun messaggio",
        "por_favor_completa" to "Per favore, completa il campo",

        // Barra di navigazione
        "atras" to "Indietro",
        "chats" to "Chat",

        // Schermata Utenti
        "mostrar_usuarios" to "Mostra Utenti",
        "nombre_label" to "Nome:",
        "alias_label" to "Alias:",
        "activo_label" to "Attivo:",

        // Schermata Registrazione
        "nombre" to "Nome",
        "email" to "Email",
        "contrasena" to "Password",
        "confirmar_contrasena" to "Conferma Password",
        "registrar" to "Registrati",
        "error_contrasenas" to "Le password non corrispondono o sono vuote",

        // Schermata Login
        "iniciar_sesion" to "Accedi",

        // Schermata Impostazioni / Controllo account
        "ajustes_control_cuentas" to "Impostazioni / Controllo account",

        // Schermata Impostazioni / Aiuto
        "ajustes_ayuda" to "Impostazioni / Aiuto",
        "escribe_reporte" to "Scrivi la tua segnalazione",
        "reporte_vacio" to "Per favore, scrivi qualcosa",

        // Schermata Email inesistente
        "email_en_sistema_titulo" to "Email nel Sistema",
        "email_en_sistema_mensaje" to "Email nel Sistema",
        "email_no_existe_titulo" to "Email non esiste",
        "email_no_existe_mensaje" to "Email non esiste",
        "verifica_correo_registro" to "Verifica di aver scritto correttamente la tua email o registrati.",
        "ir_a_registro" to "Vai alla Registrazione",

        // Schermata Email nel Sistema
        "email_en_sistema" to "Email nel sistema",
        "codigo_verificacion" to "Codice di Verifica",
        "restablecer_contrasena" to "Reimposta la Password",
        "cancelar" to "Annulla",

        // Schermata Reimposta Password
        "enviar_correo" to "Invia Email",
        "error_correo_vacio" to "Devi inserire un'email",
        "degug_restablecer_ok" to "Debug: Reimpostazione OK",
        "degug_restablecer_fail" to "Debug: Reimpostazione FALLITA",

        // Schermata Home
        "ir_a_login" to "Vai al Login",
        "ir_a_registro_home" to "Vai alla Registrazione",
        "contactos_home" to "Contatti",
        "ajustes_home" to "Impostazioni",
        "usuarios_home" to "Utenti",

        // Altri testi estratti dal codice
        "nuevo_contacto" to "Nuovo Contatto",
        "guardar" to "Salva",
        "alias_privado" to "Alias Privato",
        "alias_publico" to "Alias Pubblico",
        "descripcion" to "Descrizione",
        "modificar" to "Modifica",
        "aplicar" to "Applica",
        "cerrar_sesion" to "Esci",
        "cambiar_modo_oscuro_tema" to "Cambia Modalità Scuro / Tema",
        "cambiar_fuente" to "Cambia Font",
        "eliminar_chats" to "(Elimina Chat)",
        "control_de_cuentas" to "(Controllo account)",
        "ayuda" to "Aiuto",
        "olvidaste_contrasena" to "Hai dimenticato la password?",
        "registrarse" to "Registrati",
        "acceder" to "Accedi",
        "debug_ir_a_contactos" to "Debug: Vai ai Contatti",
        "debug_ajustes_control_cuentas" to "Debug: Impostazioni controllo account",
        "lista_de_cuentas" to "Elenco degli account",
        "lista_de_preguntas_frecuentes" to "Elenco delle domande frequenti",
        "no_puedo_agregar_a_alguien" to "Non posso aggiungere qualcuno",
        "me_han_silenciado" to "Sono stato silenziato",
        "como_cambio_mi_alias_publico" to "Come cambio il mio alias pubblico",
        "puedo_cambiar_mi_contrasena" to "Posso cambiare la mia password?",
        "vendeis_mis_datos" to "Vendete i miei dati?",
        "envia_un_reporte" to "Invia una segnalazione",
        "persona" to "Persona",
        "avatar" to "Avatar",
        "cambiar_a_tema_azul" to "Cambia al tema blu",
        "cambiar_a_tema_amarillo" to "Cambia al tema giallo",
        "cambiar_a_tema_verde" to "Cambia al tema verde",
        "cambiar_a_tema_rojo" to "Cambia al tema rosso",
        "cambiar_a_tema_morado" to "Cambia al tema viola",
        "cambiar_a_tema_gris" to "Cambia al tema grigio",
        "cambiar_a_tema_naranja" to "Cambia al tema arancione",

        // Nuovi testi della schermata Lingue
        "idiomas" to "Lingue",
        "espanol" to "Spagnolo",
        "ingles" to "Inglese",
        "portugues" to "Portoghese",
        "frances" to "Francese",
        "aleman" to "Tedesco",
        "italiano" to "Italiano",

        // Nuovi testi per le schermate di conversazione
        "nombre_del_chat" to "Nome della Chat",
        "agregar_participante" to "Aggiungi Partecipante",
        "agregar" to "Aggiungi",
        "participantes" to "Partecipanti:",
        "chat_room" to "Sala Chat",
        "nuevo_mensaje" to "Nuovo Messaggio",
        "enviar" to "Invia",
        "agregar_chat" to "Aggiungi Chat",
        "chat" to "Chat:",
        "participantes_chat" to "Partecipanti: ",

        // Nuovi testi per le schermate di impostazioni
        "lista_de_cuentas" to "Elenco degli account",
        "envia_un_reporte" to "Invia una segnalazione",
        "escribe_tu_reporte" to "Scrivi la tua segnalazione",
        "reporte_vacio" to "Per favore, scrivi qualcosa",
        "reporte_enviado" to "Segnalazione inviata",
        "modo_oscuro" to "Modalità Scuro",
        "modo_claro" to "Modalità Chiaro",
        "cambiar_a_tema_azul" to "Cambia al tema blu",
        "cambiar_a_tema_amarillo" to "Cambia al tema giallo",
        "cambiar_a_tema_verde" to "Cambia al tema verde",
        "cambiar_a_tema_rojo" to "Cambia al tema rosso",
        "cambiar_a_tema_morado" to "Cambia al tema viola",
        "cambiar_a_tema_gris" to "Cambia al tema grigio",
        "cambiar_a_tema_naranja" to "Cambia al tema arancione",

        // Nuovi testi per l'interfaccia dei Tipi di Carattere
        "ajustes_fuente" to "Impostazioni del font",
        "fuente" to "Font",
        "cambiar_fuente" to "Cambia font",
        "default_font" to "Predefinito",
        "serif_font" to "Serif",
        "monospace_font" to "Monospaziato",
        "cursive_font" to "Corsivo",
        "sans_serif_font" to "Senza grazie"
    )
)

