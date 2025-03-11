package org.connexuss.project.interfaces.idiomas

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.listSaver

data class Idioma(val palabras: Map<String, String>)

val IdiomaSaver: Saver<Idioma, Map<String, String>> = object : Saver<Idioma, Map<String, String>> {
    override fun restore(value: Map<String, String>): Idioma? {
        return Idioma(value)  // Reconstruimos el objeto a partir del mapa de palabras
    }

    override fun SaverScope.save(value: Idioma): Map<String, String>? {
        return value.palabras  // Guardamos solo el mapa de palabras
    }
}

val LocalIdiomaState = staticCompositionLocalOf<MutableState<Idioma>> {
    error("No se ha proporcionado un estado de idioma")
}

@Composable
fun ProveedorDeIdioma(content: @Composable () -> Unit) {
    val idiomaState = rememberSaveable(stateSaver = IdiomaSaver) { mutableStateOf(espannol) }
    CompositionLocalProvider(LocalIdiomaState provides idiomaState) {
        content()
    }
}

// Estado del idioma
@Composable
fun IdiomaState(): MutableState<Idioma> {
    return remember { mutableStateOf(espannol) }
}

// Cambia el idioma global
fun cambiarIdioma(idiomaState: MutableState<Idioma>, nuevoIdioma: Idioma) {
    idiomaState.value = nuevoIdioma  // ✅ Correcto
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

        // Otros textos (puedes agregar más claves según lo necesites)
        "cambiar_tema" to "Cambiar Tema",

        // General
        "app_title" to "ConneXus",
        // Pantalla de Foro
        "foro" to "Foro",
        "nuevo_tema" to "Nuevo Tema",
        "agregar_tema" to "Agregar Tema",
        // Pantalla de Tema del Foro
        "tema_del_foro" to "Tema del Foro",
        "nuevo_mensaje" to "Nuevo Mensaje",
        "enviar" to "Enviar",
        // Otros posibles textos que uses en el futuro
        "sin_resultados" to "No hay mensajes",
        "por_favor_completa" to "Por favor, completa el campo",

        // DefaultTopBar
        "atras" to "Atrás",
        "ajustes" to "Ajustes",
        // MiBottomBar
        "chats" to "Chats",
        "foro" to "Foro",
        // General
        "app_title" to "ConneXus",
        // Pantalla de Usuarios
        "usuarios" to "Usuarios",
        "mostrar_usuarios" to "Mostrar Usuarios",
        "nombre_label" to "Nombre:",
        "alias_label" to "Alias:",
        "activo_label" to "Activo:",
        // Pantalla Registro
        "registro" to "Registro",
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
        "enviar_reporte" to "Enviar Reporte",
        "escribe_reporte" to "Escribe tu reporte",
        "reporte_vacio" to "Por favor, escribe algo",
        "reporte_enviado" to "Reporte enviado",
        // Pantalla Cambiar Tema
        "cambiar_tema" to "Cambiar Tema",
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
        // Pantalla Email no existe
        "email_no_existe" to "Email no existe",
        "correo_no_registrado" to "La dirección de correo no está registrada.",
        "verifica_correo_registro" to "Verifica que hayas escrito bien tu correo o regístrate.",
        "ir_a_registro" to "Ir a Registro",
        // Pantalla Email en el Sistema
        "email_en_sistema" to "Email en el Sistema",
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
        // Nuevos textos extraídos del código
        "nuevo_contacto" to "Nuevo Contacto",
        "nuevo_chat" to "Nuevo Chat",
        "guardar" to "Guardar",
        "cancelar" to "Cancelar",
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
        "cambiar_a_tema_naranja" to "Cambiar a tema naranja"
    )
)

// Inglés

val ingles = Idioma(
    palabras = mapOf(
        // Chat Screen
        "nuevo_chat" to "New Chat",
        "agregar_participante" to "Add Participant",
        "crear_chat" to "Create Chat",

        // Navigation and menus
        "login" to "Login",
        "registro" to "Register",
        "contactos" to "Contacts",
        "ajustes" to "Settings",
        "usuarios" to "Users",
        "restablecer" to "Reset Password",

        // Theme Change / Settings Screen
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

        // Settings / Help Screen
        "enviar_reporte" to "Send Report",
        "reporte_enviado" to "Report Sent",
        "por_favor" to "Please",

        // Other texts (you can add more keys as needed)
        "cambiar_tema" to "Change Theme",

        // General
        "app_title" to "ConneXus",
        // Forum Screen
        "foro" to "Forum",
        "nuevo_tema" to "New Topic",
        "agregar_tema" to "Add Topic",
        // Forum Topic Screen
        "tema_del_foro" to "Forum Topic",
        "nuevo_mensaje" to "New Message",
        "enviar" to "Send",
        // Other possible texts you might use in the future
        "sin_resultados" to "No messages",
        "por_favor_completa" to "Please complete the field",

        // DefaultTopBar
        "atras" to "Back",
        "ajustes" to "Settings",
        // MiBottomBar
        "chats" to "Chats",
        "foro" to "Forum",
        // General
        "app_title" to "ConneXus",
        // Users Screen
        "usuarios" to "Users",
        "mostrar_usuarios" to "Show Users",
        "nombre_label" to "Name:",
        "alias_label" to "Alias:",
        "activo_label" to "Active:",
        // Registration Screen
        "registro" to "Register",
        "nombre" to "Name",
        "email" to "Email",
        "contrasena" to "Password",
        "confirmar_contrasena" to "Confirm Password",
        "registrar" to "Register",
        "error_contrasenas" to "Passwords do not match or are empty",
        // Login Screen
        "iniciar_sesion" to "Login",
        // Settings / Account Control Screen
        "ajustes_control_cuentas" to "Settings / Account Control",
        // Settings / Help Screen
        "ajustes_ayuda" to "Settings / Help",
        "enviar_reporte" to "Send Report",
        "escribe_reporte" to "Write your report",
        "reporte_vacio" to "Please write something",
        "reporte_enviado" to "Report Sent",
        // Change Theme Screen
        "cambiar_tema" to "Change Theme",
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
        // Email Not Found Screen
        "email_no_existe" to "Email Not Found",
        "correo_no_registrado" to "The email address is not registered.",
        "verifica_correo_registro" to "Please verify your email or register.",
        "ir_a_registro" to "Go to Register",
        // Email in System Screen
        "email_en_sistema" to "Email in System",
        "codigo_verificacion" to "Verification Code",
        "restablecer_contrasena" to "Reset Password",
        "cancelar" to "Cancel",
        // Reset Password Screen
        "enviar_correo" to "Send Email",
        "error_correo_vacio" to "Please enter an email",
        "degug_restablecer_ok" to "Debug: Reset OK",
        "degug_restablecer_fail" to "Debug: Reset FAIL",
        // Home Screen
        "ir_a_login" to "Go to Login",
        "ir_a_registro_home" to "Go to Register",
        "contactos_home" to "Contacts",
        "ajustes_home" to "Settings",
        "usuarios_home" to "Users",
        // New texts extracted from the code
        "nuevo_contacto" to "New Contact",
        "nuevo_chat" to "New Chat",
        "guardar" to "Save",
        "cancelar" to "Cancel",
        "alias_privado" to "Private Alias",
        "alias_publico" to "Public Alias",
        "descripcion" to "Description",
        "modificar" to "Modify",
        "aplicar" to "Apply",
        "cerrar_sesion" to "Logout",
        "cambiar_modo_oscuro_tema" to "Change Dark Mode / Theme",
        "cambiar_fuente" to "Change Font",
        "eliminar_chats" to "(Delete Chats)",
        "control_de_cuentas" to "(Account Control)",
        "ayuda" to "Help",
        "olvidaste_contrasena" to "Forgot your password?",
        "registrarse" to "Register",
        "acceder" to "Access",
        "debug_ir_a_contactos" to "Debug: Go to Contacts",
        "debug_ajustes_control_cuentas" to "Debug: Account Control Settings",
        "lista_de_cuentas" to "Account List",
        "lista_de_preguntas_frecuentes" to "FAQ List",
        "no_puedo_agregar_a_alguien" to "I can't add someone",
        "me_han_silenciado" to "I have been silenced",
        "como_cambio_mi_alias_publico" to "How do I change my public alias?",
        "puedo_cambiar_mi_contrasena" to "Can I change my password?",
        "vendeis_mis_datos" to "Do you sell my data?",
        "envia_un_reporte" to "Send a report",
        "persona" to "Person",
        "avatar" to "Avatar",
        "cambiar_a_tema_azul" to "Change to Blue Theme",
        "cambiar_a_tema_amarillo" to "Change to Yellow Theme",
        "cambiar_a_tema_verde" to "Change to Green Theme",
        "cambiar_a_tema_rojo" to "Change to Red Theme",
        "cambiar_a_tema_morado" to "Change to Purple Theme",
        "cambiar_a_tema_gris" to "Change to Gray Theme",
        "cambiar_a_tema_naranja" to "Change to Orange Theme"
    )
)

// Portugués

val portugues = Idioma(
    palabras = mapOf(
        // Tela de Chat
        "nuevo_chat" to "Novo Chat",
        "agregar_participante" to "Adicionar Participante",
        "crear_chat" to "Criar Chat",

        // Navegação e menus
        "login" to "Login",
        "registro" to "Registro",
        "contactos" to "Contatos",
        "ajustes" to "Configurações",
        "usuarios" to "Usuários",
        "restablecer" to "Redefinir Senha",

        // Tela de Alteração de Tema / Configurações de Tema
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

        // Tela de Configurações / Ajuda
        "enviar_reporte" to "Enviar Relatório",
        "reporte_enviado" to "Relatório Enviado",
        "por_favor" to "Por favor",

        // Outros textos (você pode adicionar mais chaves conforme necessário)
        "cambiar_tema" to "Mudar Tema",

        // Geral
        "app_title" to "ConneXus",
        // Tela de Fórum
        "foro" to "Fórum",
        "nuevo_tema" to "Novo Tópico",
        "agregar_tema" to "Adicionar Tópico",
        // Tela de Tópico do Fórum
        "tema_del_foro" to "Tópico do Fórum",
        "nuevo_mensaje" to "Nova Mensagem",
        "enviar" to "Enviar",
        // Outros textos possíveis que você pode usar no futuro
        "sin_resultados" to "Não há mensagens",
        "por_favor_completa" to "Por favor, preencha o campo",

        // DefaultTopBar
        "atras" to "Voltar",
        "ajustes" to "Configurações",
        // MiBottomBar
        "chats" to "Chats",
        "foro" to "Fórum",
        // Geral
        "app_title" to "ConneXus",
        // Tela de Usuários
        "usuarios" to "Usuários",
        "mostrar_usuarios" to "Mostrar Usuários",
        "nombre_label" to "Nome:",
        "alias_label" to "Apelido:",
        "activo_label" to "Ativo:",
        // Tela de Registro
        "registro" to "Registro",
        "nombre" to "Nome",
        "email" to "E-mail",
        "contrasena" to "Senha",
        "confirmar_contrasena" to "Confirmar Senha",
        "registrar" to "Registrar",
        "error_contrasenas" to "As senhas não coincidem ou estão vazias",
        // Tela de Login
        "iniciar_sesion" to "Login",
        // Tela de Configurações / Controle de Contas
        "ajustes_control_cuentas" to "Configurações / Controle de Contas",
        // Tela de Configurações / Ajuda
        "ajustes_ayuda" to "Configurações / Ajuda",
        "enviar_reporte" to "Enviar Relatório",
        "escribe_reporte" to "Escreva seu relatório",
        "reporte_vacio" to "Por favor, escreva algo",
        "reporte_enviado" to "Relatório Enviado",
        // Tela de Mudar Tema
        "cambiar_tema" to "Mudar Tema",
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
        // Tela de E-mail Não Encontrado
        "email_no_existe" to "E-mail Não Encontrado",
        "correo_no_registrado" to "O endereço de e-mail não está registrado.",
        "verifica_correo_registro" to "Verifique se você digitou o e-mail corretamente ou registre-se.",
        "ir_a_registro" to "Ir para Registro",
        // Tela de E-mail no Sistema
        "email_en_sistema" to "E-mail no Sistema",
        "codigo_verificacion" to "Código de Verificação",
        "restablecer_contrasena" to "Redefinir Senha",
        "cancelar" to "Cancelar",
        // Tela de Redefinir Senha
        "enviar_correo" to "Enviar E-mail",
        "error_correo_vacio" to "Por favor, insira um e-mail",
        "degug_restablecer_ok" to "Debug: Redefinir OK",
        "degug_restablecer_fail" to "Debug: Redefinir FAIL",
        // Tela Inicial
        "ir_a_login" to "Ir para Login",
        "ir_a_registro_home" to "Ir para Registro",
        "contactos_home" to "Contatos",
        "ajustes_home" to "Configurações",
        "usuarios_home" to "Usuários",
        // Novos textos extraídos do código
        "nuevo_contacto" to "Novo Contato",
        "nuevo_chat" to "Novo Chat",
        "guardar" to "Salvar",
        "cancelar" to "Cancelar",
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
        "registrarse" to "Registrar",
        "acceder" to "Acessar",
        "debug_ir_a_contactos" to "Debug: Ir para Contatos",
        "debug_ajustes_control_cuentas" to "Debug: Configurações de Controle de Contas",
        "lista_de_cuentas" to "Lista de Contas",
        "lista_de_preguntas_frecuentes" to "Lista de Perguntas Frequentes",
        "no_puedo_agregar_a_alguien" to "Não consigo adicionar alguém",
        "me_han_silenciado" to "Fui silenciado",
        "como_cambio_mi_alias_publico" to "Como mudo meu apelido público?",
        "puedo_cambiar_mi_contrasena" to "Posso mudar minha senha?",
        "vendeis_mis_datos" to "Vocês vendem meus dados?",
        "envia_un_reporte" to "Enviar um relatório",
        "persona" to "Pessoa",
        "avatar" to "Avatar",
        "cambiar_a_tema_azul" to "Mudar para Tema Azul",
        "cambiar_a_tema_amarillo" to "Mudar para Tema Amarelo",
        "cambiar_a_tema_verde" to "Mudar para Tema Verde",
        "cambiar_a_tema_rojo" to "Mudar para Tema Vermelho",
        "cambiar_a_tema_morado" to "Mudar para Tema Roxo",
        "cambiar_a_tema_gris" to "Mudar para Tema Cinza",
        "cambiar_a_tema_naranja" to "Mudar para Tema Laranja"
    )
)


// Francés

val frances = Idioma(
    palabras = mapOf(
        // Écran de Chat
        "nuevo_chat" to "Nouveau Chat",
        "agregar_participante" to "Ajouter Participant",
        "crear_chat" to "Créer Chat",

        // Navigation et menus
        "login" to "Connexion",
        "registro" to "Inscription",
        "contactos" to "Contacts",
        "ajustes" to "Paramètres",
        "usuarios" to "Utilisateurs",
        "restablecer" to "Réinitialiser Mot de Passe",

        // Écran de Changement de Thème / Paramètres de Thème
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

        // Écran de Paramètres / Aide
        "enviar_reporte" to "Envoyer Rapport",
        "reporte_enviado" to "Rapport Envoyé",
        "por_favor" to "S'il vous plaît",

        // Autres textes (vous pouvez ajouter plus de clés selon vos besoins)
        "cambiar_tema" to "Changer Thème",

        // Général
        "app_title" to "ConneXus",
        // Écran de Forum
        "foro" to "Forum",
        "nuevo_tema" to "Nouveau Sujet",
        "agregar_tema" to "Ajouter Sujet",
        // Écran de Sujet du Forum
        "tema_del_foro" to "Sujet du Forum",
        "nuevo_mensaje" to "Nouveau Message",
        "enviar" to "Envoyer",
        // Autres textes possibles que vous pourriez utiliser à l'avenir
        "sin_resultados" to "Aucun message",
        "por_favor_completa" to "Veuillez compléter le champ",

        // DefaultTopBar
        "atras" to "Retour",
        "ajustes" to "Paramètres",
        // MiBottomBar
        "chats" to "Chats",
        "foro" to "Forum",
        // Général
        "app_title" to "ConneXus",
        // Écran des Utilisateurs
        "usuarios" to "Utilisateurs",
        "mostrar_usuarios" to "Afficher Utilisateurs",
        "nombre_label" to "Nom :",
        "alias_label" to "Alias :",
        "activo_label" to "Actif :",
        // Écran d'Inscription
        "registro" to "Inscription",
        "nombre" to "Nom",
        "email" to "E-mail",
        "contrasena" to "Mot de Passe",
        "confirmar_contrasena" to "Confirmer Mot de Passe",
        "registrar" to "S'inscrire",
        "error_contrasenas" to "Les mots de passe ne correspondent pas ou sont vides",
        // Écran de Connexion
        "iniciar_sesion" to "Connexion",
        // Écran de Paramètres / Contrôle des Comptes
        "ajustes_control_cuentas" to "Paramètres / Contrôle des Comptes",
        // Écran de Paramètres / Aide
        "ajustes_ayuda" to "Paramètres / Aide",
        "enviar_reporte" to "Envoyer Rapport",
        "escribe_reporte" to "Écrivez votre rapport",
        "reporte_vacio" to "Veuillez écrire quelque chose",
        "reporte_enviado" to "Rapport Envoyé",
        // Écran de Changement de Thème
        "cambiar_tema" to "Changer Thème",
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
        // Écran d'E-mail Non Trouvé
        "email_no_existe" to "E-mail Non Trouvé",
        "correo_no_registrado" to "L'adresse e-mail n'est pas enregistrée.",
        "verifica_correo_registro" to "Veuillez vérifier votre e-mail ou inscrivez-vous.",
        "ir_a_registro" to "Aller à l'Inscription",
        // Écran d'E-mail dans le Système
        "email_en_sistema" to "E-mail dans le Système",
        "codigo_verificacion" to "Code de Vérification",
        "restablecer_contrasena" to "Réinitialiser Mot de Passe",
        "cancelar" to "Annuler",
        // Écran de Réinitialisation du Mot de Passe
        "enviar_correo" to "Envoyer E-mail",
        "error_correo_vacio" to "Veuillez entrer un e-mail",
        "degug_restablecer_ok" to "Debug : Réinitialiser OK",
        "degug_restablecer_fail" to "Debug : Réinitialiser FAIL",
        // Écran d'Accueil
        "ir_a_login" to "Aller à la Connexion",
        "ir_a_registro_home" to "Aller à l'Inscription",
        "contactos_home" to "Contacts",
        "ajustes_home" to "Paramètres",
        "usuarios_home" to "Utilisateurs",
        // Nouveaux textes extraits du code
        "nuevo_contacto" to "Nouveau Contact",
        "nuevo_chat" to "Nouveau Chat",
        "guardar" to "Enregistrer",
        "cancelar" to "Annuler",
        "alias_privado" to "Alias Privé",
        "alias_publico" to "Alias Public",
        "descripcion" to "Description",
        "modificar" to "Modifier",
        "aplicar" to "Appliquer",
        "cerrar_sesion" to "Déconnexion",
        "cambiar_modo_oscuro_tema" to "Changer Mode Sombre / Thème",
        "cambiar_fuente" to "Changer Police",
        "eliminar_chats" to "(Supprimer Chats)",
        "control_de_cuentas" to "(Contrôle des Comptes)",
        "ayuda" to "Aide",
        "olvidaste_contrasena" to "Mot de passe oublié ?",
        "registrarse" to "S'inscrire",
        "acceder" to "Accéder",
        "debug_ir_a_contactos" to "Debug : Aller aux Contacts",
        "debug_ajustes_control_cuentas" to "Debug : Paramètres de Contrôle des Comptes",
        "lista_de_cuentas" to "Liste des Comptes",
        "lista_de_preguntas_frecuentes" to "Liste des Questions Fréquentes",
        "no_puedo_agregar_a_alguien" to "Je ne peux pas ajouter quelqu'un",
        "me_han_silenciado" to "J'ai été réduit au silence",
        "como_cambio_mi_alias_publico" to "Comment changer mon alias public ?",
        "puedo_cambiar_mi_contrasena" to "Puis-je changer mon mot de passe ?",
        "vendeis_mis_datos" to "Vendez-vous mes données ?",
        "envia_un_reporte" to "Envoyer un rapport",
        "persona" to "Personne",
        "avatar" to "Avatar",
        "cambiar_a_tema_azul" to "Changer pour Thème Bleu",
        "cambiar_a_tema_amarillo" to "Changer pour Thème Jaune",
        "cambiar_a_tema_verde" to "Changer pour Thème Vert",
        "cambiar_a_tema_rojo" to "Changer pour Thème Rouge",
        "cambiar_a_tema_morado" to "Changer pour Thème Violet",
        "cambiar_a_tema_gris" to "Changer pour Thème Gris",
        "cambiar_a_tema_naranja" to "Changer pour Thème Orange"
    )
)

// Alemán

val aleman = Idioma(
    palabras = mapOf(
        // Chat-Bildschirm
        "nuevo_chat" to "Neuer Chat",
        "agregar_participante" to "Teilnehmer Hinzufügen",
        "crear_chat" to "Chat Erstellen",

        // Navigation und Menüs
        "login" to "Anmelden",
        "registro" to "Registrieren",
        "contactos" to "Kontakte",
        "ajustes" to "Einstellungen",
        "usuarios" to "Benutzer",
        "restablecer" to "Passwort Zurücksetzen",

        // Bildschirm Thema Ändern / Einstellungen Thema
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

        // Bildschirm Einstellungen / Hilfe
        "enviar_reporte" to "Bericht Senden",
        "reporte_enviado" to "Bericht Gesendet",
        "por_favor" to "Bitte",

        // Andere Texte (Sie können weitere Schlüssel nach Bedarf hinzufügen)
        "cambiar_tema" to "Thema Ändern",

        // Allgemein
        "app_title" to "ConneXus",
        // Forum-Bildschirm
        "foro" to "Forum",
        "nuevo_tema" to "Neues Thema",
        "agregar_tema" to "Thema Hinzufügen",
        // Forum-Themen-Bildschirm
        "tema_del_foro" to "Forum-Thema",
        "nuevo_mensaje" to "Neue Nachricht",
        "enviar" to "Senden",
        // Weitere mögliche Texte, die Sie in der Zukunft verwenden könnten
        "sin_resultados" to "Keine Nachrichten",
        "por_favor_completa" to "Bitte füllen Sie das Feld aus",

        // DefaultTopBar
        "atras" to "Zurück",
        "ajustes" to "Einstellungen",
        // MiBottomBar
        "chats" to "Chats",
        "foro" to "Forum",
        // Allgemein
        "app_title" to "ConneXus",
        // Benutzer-Bildschirm
        "usuarios" to "Benutzer",
        "mostrar_usuarios" to "Benutzer Anzeigen",
        "nombre_label" to "Name:",
        "alias_label" to "Alias:",
        "activo_label" to "Aktiv:",
        // Registrierungs-Bildschirm
        "registro" to "Registrieren",
        "nombre" to "Name",
        "email" to "E-Mail",
        "contrasena" to "Passwort",
        "confirmar_contrasena" to "Passwort Bestätigen",
        "registrar" to "Registrieren",
        "error_contrasenas" to "Die Passwörter stimmen nicht überein oder sind leer",
        // Anmelde-Bildschirm
        "iniciar_sesion" to "Anmelden",
        // Bildschirm Einstellungen / Konto-Kontrolle
        "ajustes_control_cuentas" to "Einstellungen / Konto-Kontrolle",
        // Bildschirm Einstellungen / Hilfe
        "ajustes_ayuda" to "Einstellungen / Hilfe",
        "enviar_reporte" to "Bericht Senden",
        "escribe_reporte" to "Schreiben Sie Ihren Bericht",
        "reporte_vacio" to "Bitte schreiben Sie etwas",
        "reporte_enviado" to "Bericht Gesendet",
        // Bildschirm Thema Ändern
        "cambiar_tema" to "Thema Ändern",
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
        // Bildschirm E-Mail Nicht Gefunden
        "email_no_existe" to "E-Mail Nicht Gefunden",
        "correo_no_registrado" to "Die E-Mail-Adresse ist nicht registriert.",
        "verifica_correo_registro" to "Bitte überprüfen Sie Ihre E-Mail oder registrieren Sie sich.",
        "ir_a_registro" to "Zur Registrierung",
        // Bildschirm E-Mail im System
        "email_en_sistema" to "E-Mail im System",
        "codigo_verificacion" to "Verifizierungscode",
        "restablecer_contrasena" to "Passwort Zurücksetzen",
        "cancelar" to "Abbrechen",
        // Bildschirm Passwort Zurücksetzen
        "enviar_correo" to "E-Mail Senden",
        "error_correo_vacio" to "Bitte geben Sie eine E-Mail ein",
        "degug_restablecer_ok" to "Debug: Zurücksetzen OK",
        "degug_restablecer_fail" to "Debug: Zurücksetzen FAIL",
        // Startbildschirm
        "ir_a_login" to "Zur Anmeldung",
        "ir_a_registro_home" to "Zur Registrierung",
        "contactos_home" to "Kontakte",
        "ajustes_home" to "Einstellungen",
        "usuarios_home" to "Benutzer",
        // Neue Texte aus dem Code
        "nuevo_contacto" to "Neuer Kontakt",
        "nuevo_chat" to "Neuer Chat",
        "guardar" to "Speichern",
        "cancelar" to "Abbrechen",
        "alias_privado" to "Privater Alias",
        "alias_publico" to "Öffentlicher Alias",
        "descripcion" to "Beschreibung",
        "modificar" to "Ändern",
        "aplicar" to "Anwenden",
        "cerrar_sesion" to "Abmelden",
        "cambiar_modo_oscuro_tema" to "Dunkelmodus / Thema Ändern",
        "cambiar_fuente" to "Schriftart Ändern",
        "eliminar_chats" to "(Chats Löschen)",
        "control_de_cuentas" to "(Konto-Kontrolle)",
        "ayuda" to "Hilfe",
        "olvidaste_contrasena" to "Passwort Vergessen?",
        "registrarse" to "Registrieren",
        "acceder" to "Zugreifen",
        "debug_ir_a_contactos" to "Debug: Zu Kontakten",
        "debug_ajustes_control_cuentas" to "Debug: Konto-Kontrolle Einstellungen",
        "lista_de_cuentas" to "Kontoliste",
        "lista_de_preguntas_frecuentes" to "Liste der Häufigen Fragen",
        "no_puedo_agregar_a_alguien" to "Ich kann niemanden hinzufügen",
        "me_han_silenciado" to "Ich wurde stummgeschaltet",
        "como_cambio_mi_alias_publico" to "Wie ändere ich meinen öffentlichen Alias?",
        "puedo_cambiar_mi_contrasena" to "Kann ich mein Passwort ändern?",
        "vendeis_mis_datos" to "Verkaufen Sie meine Daten?",
        "envia_un_reporte" to "Bericht Senden",
        "persona" to "Person",
        "avatar" to "Avatar",
        "cambiar_a_tema_azul" to "Zu Blauem Thema Wechseln",
        "cambiar_a_tema_amarillo" to "Zu Gelbem Thema Wechseln",
        "cambiar_a_tema_verde" to "Zu Grünem Thema Wechseln",
        "cambiar_a_tema_rojo" to "Zu Rotem Thema Wechseln",
        "cambiar_a_tema_morado" to "Zu Lila Thema Wechseln",
        "cambiar_a_tema_gris" to "Zu Grauem Thema Wechseln",
        "cambiar_a_tema_naranja" to "Zu Orangem Thema Wechseln"
    )
)

// Italiano

val italian = Idioma(
    palabras = mapOf(
        // Schermata Chat
        "nuevo_chat" to "Nuova Chat",
        "agregar_participante" to "Aggiungi Partecipante",
        "crear_chat" to "Crea Chat",

        // Navigazione e menu
        "login" to "Accedi",
        "registro" to "Registrazione",
        "contactos" to "Contatti",
        "ajustes" to "Impostazioni",
        "usuarios" to "Utenti",
        "restablecer" to "Reimposta Password",

        // Schermata Cambio Tema / Impostazioni Tema
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
        "reporte_enviado" to "Segnalazione Inviata",
        "por_favor" to "Per favore",

        // Altri testi (puoi aggiungere altre chiavi se necessario)
        "cambiar_tema" to "Cambia Tema",

        // Generale
        "app_title" to "ConneXus",
        // Schermata Forum
        "foro" to "Forum",
        "nuevo_tema" to "Nuovo Argomento",
        "agregar_tema" to "Aggiungi Argomento",
        // Schermata Argomento del Forum
        "tema_del_foro" to "Argomento del Forum",
        "nuevo_mensaje" to "Nuovo Messaggio",
        "enviar" to "Invia",
        // Altri possibili testi che potresti usare in futuro
        "sin_resultados" to "Nessun messaggio",
        "por_favor_completa" to "Per favore, completa il campo",

        // DefaultTopBar
        "atras" to "Indietro",
        "ajustes" to "Impostazioni",
        // MiBottomBar
        "chats" to "Chat",
        "foro" to "Forum",
        // Generale
        "app_title" to "ConneXus",
        // Schermata Utenti
        "usuarios" to "Utenti",
        "mostrar_usuarios" to "Mostra Utenti",
        "nombre_label" to "Nome:",
        "alias_label" to "Alias:",
        "activo_label" to "Attivo:",
        // Schermata Registrazione
        "registro" to "Registrazione",
        "nombre" to "Nome",
        "email" to "E-mail",
        "contrasena" to "Password",
        "confirmar_contrasena" to "Conferma Password",
        "registrar" to "Registrati",
        "error_contrasenas" to "Le password non corrispondono o sono vuote",
        // Schermata Login
        "iniciar_sesion" to "Accedi",
        // Schermata Impostazioni / Controllo Account
        "ajustes_control_cuentas" to "Impostazioni / Controllo Account",
        // Schermata Impostazioni / Aiuto
        "ajustes_ayuda" to "Impostazioni / Aiuto",
        "enviar_reporte" to "Invia Segnalazione",
        "escribe_reporte" to "Scrivi la tua segnalazione",
        "reporte_vacio" to "Per favore, scrivi qualcosa",
        "reporte_enviado" to "Segnalazione Inviata",
        // Schermata Cambia Tema
        "cambiar_tema" to "Cambia Tema",
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
        // Schermata Email Non Trovata
        "email_no_existe" to "Email Non Trovata",
        "correo_no_registrado" to "L'indirizzo email non è registrato.",
        "verifica_correo_registro" to "Verifica di aver scritto correttamente l'email o registrati.",
        "ir_a_registro" to "Vai alla Registrazione",
        // Schermata Email nel Sistema
        "email_en_sistema" to "Email nel Sistema",
        "codigo_verificacion" to "Codice di Verifica",
        "restablecer_contrasena" to "Reimposta Password",
        "cancelar" to "Annulla",
        // Schermata Reimposta Password
        "enviar_correo" to "Invia Email",
        "error_correo_vacio" to "Inserisci un'email",
        "degug_restablecer_ok" to "Debug: Reimposta OK",
        "degug_restablecer_fail" to "Debug: Reimposta FAIL",
        // Schermata Home
        "ir_a_login" to "Vai al Login",
        "ir_a_registro_home" to "Vai alla Registrazione",
        "contactos_home" to "Contatti",
        "ajustes_home" to "Impostazioni",
        "usuarios_home" to "Utenti",
        // Nuovi testi estratti dal codice
        "nuevo_contacto" to "Nuovo Contatto",
        "nuevo_chat" to "Nuova Chat",
        "guardar" to "Salva",
        "cancelar" to "Annulla",
        "alias_privado" to "Alias Privato",
        "alias_publico" to "Alias Pubblico",
        "descripcion" to "Descrizione",
        "modificar" to "Modifica",
        "aplicar" to "Applica",
        "cerrar_sesion" to "Esci",
        "cambiar_modo_oscuro_tema" to "Cambia Modalità Scuro / Tema",
        "cambiar_fuente" to "Cambia Font",
        "eliminar_chats" to "(Elimina Chat)",
        "control_de_cuentas" to "(Controllo Account)",
        "ayuda" to "Aiuto",
        "olvidaste_contrasena" to "Password Dimenticata?",
        "registrarse" to "Registrati",
        "acceder" to "Accedi",
        "debug_ir_a_contactos" to "Debug: Vai ai Contatti",
        "debug_ajustes_control_cuentas" to "Debug: Impostazioni Controllo Account",
        "lista_de_cuentas" to "Elenco Account",
        "lista_de_preguntas_frecuentes" to "Elenco Domande Frequenti",
        "no_puedo_agregar_a_alguien" to "Non posso aggiungere qualcuno",
        "me_han_silenciado" to "Sono stato silenziato",
        "como_cambio_mi_alias_publico" to "Come cambio il mio alias pubblico?",
        "puedo_cambiar_mi_contrasena" to "Posso cambiare la mia password?",
        "vendeis_mis_datos" to "Vendete i miei dati?",
        "envia_un_reporte" to "Invia una segnalazione",
        "persona" to "Persona",
        "avatar" to "Avatar",
        "cambiar_a_tema_azul" to "Cambia a Tema Blu",
        "cambiar_a_tema_amarillo" to "Cambia a Tema Giallo",
        "cambiar_a_tema_verde" to "Cambia a Tema Verde",
        "cambiar_a_tema_rojo" to "Cambia a Tema Rosso",
        "cambiar_a_tema_morado" to "Cambia a Tema Viola",
        "cambiar_a_tema_gris" to "Cambia a Tema Grigio",
        "cambiar_a_tema_naranja" to "Cambia a Tema Arancione"
    )
)