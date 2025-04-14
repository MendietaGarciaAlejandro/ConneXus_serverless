package org.connexuss.project.usuario

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.connexuss.project.comunicacion.ConversacionesUsuario
import org.connexuss.project.encriptacion.hash
import org.connexuss.project.misc.UsuariosPreCreados
import org.connexuss.project.misc.imagenesPerfilAbstrasto
import org.connexuss.project.misc.imagenesPerfilDibujo
import org.connexuss.project.misc.imagenesPerfilPersona
import org.jetbrains.compose.resources.DrawableResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// Clase usuario con sus atributos y metodos
/*
@Serializable
class Usuario {
    private var idUnico: String = ""
    private var nombre: String = ""
    private var correo: String = ""
    private var aliasPublico: String = ""
    private var aliasPrivado: String = ""
    private var activo: Boolean = false
    private var contactos: List<String>? = emptyList()
    private var chatUser: ConversacionesUsuario? = null
    private var descripcion: String = ""
    private var contrasennia: String = ""
    private var usuariosBloqueados: List<String> = emptyList()
    @Transient
    private var imagenPerfil: DrawableResource? = null
    // Constructor completo
    constructor(nombre: String, correo: String, aliasPublico: String, activo: Boolean, contactos: List<String>?, chatUser: ConversacionesUsuario?) {
        this.idUnico = UtilidadesUsuario().generarIdUnico()
        this.nombre = nombre
        this.correo = correo
        this.aliasPublico = aliasPublico
        this.aliasPrivado = hash(aliasPublico)
        this.activo = activo
        this.contactos = contactos
        this.chatUser = chatUser
        this.imagenPerfil = generarImagenPerfilRandom()
    }

    fun generarImagenPerfilRandom(): DrawableResource {
        // Cojo todas las listas de imagenes de perfil para que seleccione una aleatoria
        val todasImagenes = imagenesPerfilPersona + imagenesPerfilAbstrasto + imagenesPerfilDibujo
        return todasImagenes.random().resource
    }

    //Debug: Contructor con idUnico
    constructor(idUnico: String, nombre: String, correo: String, aliasPublico: String, activo: Boolean, contactos: List<String>?, chatUser: ConversacionesUsuario?) {
        this.idUnico = idUnico
        this.nombre = nombre
        this.correo = correo
        this.aliasPublico = aliasPublico
        this.aliasPrivado = hash(aliasPublico)
        this.activo = activo
        this.contactos = contactos
        if (chatUser != null) {
            this.chatUser = chatUser
        }
        this.imagenPerfil = generarImagenPerfilRandom()}

    // Constructor vacio
    constructor()

    // Constructor de copia
    constructor(usuario: Usuario) {
        this.idUnico = usuario.idUnico
        this.nombre = usuario.nombre
        this.correo = usuario.correo
        this.aliasPublico = usuario.aliasPublico
        this.activo = usuario.activo
    }

    // Getter y Setter del usuario
    fun getNombreCompleto(): String {
        return nombre
    }
    fun setNombreCompleto(nombreCompleto: String) {
        this.nombre = nombreCompleto
    }

    fun getCorreo(): String {
        return correo
    }
    fun setCorreo(correo: String) {
        this.correo = correo
    }

    fun getAlias(): String {
        return aliasPublico
    }
    fun setAlias(alias: String) {
        this.aliasPublico = alias
    }

    fun getAliasPrivado(): String {
        return aliasPrivado
    }
    fun setAliasPrivado(aliasPrivado: String) {
        this.aliasPrivado = aliasPrivado
    }

    fun getIdUnico(): String {
        return idUnico
    }
    fun setIdUnico(idUnico: String) {
        this.idUnico = idUnico
    }

    fun getActivo(): Boolean {
        return activo
    }
    fun setActivo(activo: Boolean) {
        this.activo = activo
    }

    fun getContactos(): List<String>? {
        return contactos
    }

    fun setContactos(contactos: List<String>) {
        this.contactos = contactos
    }

    fun getChatUser(): ConversacionesUsuario? {
        return chatUser
    }

    fun setChatUser(chatUser: ConversacionesUsuario?) {
        this.chatUser = chatUser
    }

    fun getDescripcion(): String {
        return descripcion
    }

    fun setDescripcion(descripcion: String) {
        this.descripcion = descripcion
    }

    fun getContrasennia(): String {
        return contrasennia
    }

    fun setContrasennia(contrasennia: String) {
        this.contrasennia = contrasennia
    }

    fun getUsuariosBloqueados(): List<String> {
        return usuariosBloqueados
    }

    fun setUsuariosBloqueados(usuariosBloqueados: List<String>) {
        this.usuariosBloqueados = usuariosBloqueados
    }

    fun getImagenPerfil(): DrawableResource? {
        return imagenPerfil
    }

    fun setImagenPerfil(imagenPerfil: DrawableResource) {
        this.imagenPerfil = imagenPerfil
    }


    // Metodo para imprimir los datos públicos del usuario
    @Composable
    fun imprimirDatosPublicos() {
        Text("Nombre: $nombre")
        Text("Alias: $aliasPublico")
        Text("Activo: $activo")
    }

    // Metodo para imprimir los datos privados del usuario
    @Composable
    fun imprimirDatosPrivados() {
        Text("Correo: $correo")
        Text("Id Unico: $idUnico")
        Text("Alias Privado: $aliasPrivado")
    }
}
 */

// Clase que almacena los usuarios
class AlmacenamientoUsuario {
    private var usuarios: ArrayList<Usuario> = ArrayList()

    fun agregarUsuario(usuario: Usuario) {
        usuarios.add(usuario)
    }

    fun eliminarUsuario(usuario: Usuario) {
        usuarios.remove(usuario)
    }

    fun obtenerUsuarios(): ArrayList<Usuario> {
        return usuarios
    }

    fun obtenerUsuarioPorId(id: String): Usuario {
        return usuarios.find { it.getIdUnico() == id }!!
    }

    fun obtenerUsuarioPorAlias(alias: String): Usuario {
        return usuarios.find { it.getAlias() == alias }!!
    }

    fun obtenerUsuarioPorCorreo(correo: String): Usuario {
        return usuarios.find { it.getCorreo() == correo }!!
    }

    fun obtenerUsuarioPorNombre(nombre: String): Usuario {
        return usuarios.find { it.getNombreCompleto() == nombre }!!
    }
}

// Clase que contiene metodos de utilidades para el usuario
class UtilidadesUsuario {

    fun generarIdUnico(length: Int = 10): String {
        val charset = ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

    @OptIn(ExperimentalUuidApi::class)
    fun generaUuidUnico(): Uuid {
        return Uuid.random()
    }

    private fun validarCorreo(correo: String): Boolean {
        return correo.contains("@")
    }

    private fun validarNombre(nombre: String): Boolean {
        return nombre.isNotEmpty()
    }

    fun generarAlias(nombre: String): String {
        val alias = nombre.map { it.code }.joinToString("")
        return alias
    }

    fun bloquearContacto(idUsuario: String): Boolean {
        //Se debe implementar la lógica para bloquear un contacto
        //pienso que se le debe pasar el id del usuario a bloquear, comprobar si lo tiene en la lista de contactos...
        // si es asi, lo elimina y lo añade a la lista de bloqueados
        return true
    }

    fun instanciaUsuario(nombre: String?, correo: String?, aliasPublico: String?, activo: Boolean?): Usuario? {
        val correoValido = correo?.let { validarCorreo(it) } ?: false
        val nombreValido = nombre?.let { validarNombre(it) } ?: false
        val aliasPublicoValido = aliasPublico?.let { validarNombre(it) } ?: false
        if (!correoValido || !nombreValido || !aliasPublicoValido) {
            throw IllegalArgumentException("Datos de usuario no validos")
        }
        return if (activo != null) {
            Usuario(
                generarIdUnico(),
                nombre!!,
                correo!!,
                aliasPublico!!,
                aliasPrivado = hash(aliasPublico),
                activo,
                descripcion = "",
                contrasennia = "",
                imagenPerfil = generarImagenPerfilRandom()
            )
        } else {
            null
        }
    }

    //Debug: Contructor con idUnico
    fun instanciaUsuario(
        idUnico: String,
        nombre: String,
        correo: String,
        aliasPublico: String,
        activo: Boolean
    ): Usuario {
        val idUnico = idUnico
        val correoValido = validarCorreo(correo)
        val nombreValido = validarNombre(nombre)
        val aliasPublicoValido = validarNombre(aliasPublico)
        if (!correoValido || !nombreValido || !aliasPublicoValido) {
            throw IllegalArgumentException("Datos de usuario no validos")
        }
        return Usuario(
            idUnico,
            nombre,
            correo,
            aliasPublico,
            aliasPrivado = hash(aliasPublico),
            activo,
            descripcion = "",
            contrasennia = "",
            imagenPerfil = generarImagenPerfilRandom()
        )
    }


    // Genera un alias Publico aleatorio para un usuario
    companion object {
        private val aliasGenerados = mutableSetOf<String>()

        private val primeraParte = listOf(
            "Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot", "Golf", "Hotel", "India", "Juliet",
            "Kilo", "Lima", "Mike", "November", "Oscar", "Papa", "Quebec", "Romeo", "Sierra", "Tango",
            "Uniform", "Victor", "Whiskey", "Xray", "Yankee", "Zulu", "Aurora", "Blaze", "Cobalt", "Dynamo",
            "Eclipse", "Falcon", "Galaxy", "Horizon", "Inferno", "Jade", "Knight", "Legend", "Mystic", "Nova",
            "Orion", "Phoenix", "Quantum", "Raven", "Solar", "Titan", "Umbra", "Vortex", "Warden", "Zenith"
        )

        // Lista de 50 palabras para la segunda parte
        private val segundaParte = listOf(
            "Archer", "Beacon", "Cipher", "Drift", "Ember", "Flux", "Glimmer", "Halo", "Ion", "Jolt",
            "Karma", "Lumen", "Mirage", "Nimbus", "Oasis", "Pulse", "Quasar", "Ripple", "Surge", "Tempest",
            "Ultraviolet", "Vigor", "Whirl", "Xenon", "Yield", "Zephyr", "Apex", "Bolt", "Crest", "Dusk",
            "Emberlight", "Frost", "Gale", "Haze", "Ice", "Jinx", "Knell", "Loom", "Mist", "Nexus",
            "Orb", "Prism", "Quiver", "Riddle", "Shard", "Torrent", "Umber", "Veil", "Wisp"
        )

        // Lista de 50 palabras para la tercera parte
        private val terceraParte = listOf(
            "Amber", "Brisk", "Crux", "Dawn", "Edge", "Fable", "Glade", "Hearth", "Isle", "Jadeite",
            "Keen", "Lush", "Muse", "Nook", "Omen", "Pledge", "Quill", "Rove", "Spry", "Thorne",
            "Unity", "Verge", "Wild", "Xylem", "Yarn", "Zeal", "Arc", "Breeze", "Core", "Driftwood",
            "Eon", "Fervor", "Grit", "Hearthstone", "Ignite", "Jumble", "Knack", "Luster", "Motif", "Nurture",
            "Orbit", "Pulse", "Quest", "Roam", "Solace", "Thrive", "Untamed", "Valor", "Whim", "Zest"
        )
    }

    // Función que obtiene los alias existentes en UsuariosPreCreados
    private fun obtenerAliasExistentes(): Set<String> {
        return UsuariosPreCreados.map { it.getAlias() }.toSet()
    }

    fun generarImagenPerfilRandom(): DrawableResource {
        // Cojo todas las listas de imagenes de perfil para que seleccione una aleatoria
        val todasImagenes = imagenesPerfilPersona + imagenesPerfilAbstrasto + imagenesPerfilDibujo
        return todasImagenes.random().resource
    }

    fun generarAliasPublico(): String {
        val aliasExistentes = obtenerAliasExistentes() + aliasGenerados
        var alias: String
        do {
            alias = "${primeraParte.random()} ${segundaParte.random()} ${terceraParte.random()}"
        } while (alias in aliasExistentes)
        aliasGenerados.add(alias)
        return alias
    }
}

// NO BORRAR!!! Son las clases desarrolladas para implementar en la base de datos
@Serializable
data class Usuario(
    @SerialName("idunico")
    var idUnico: String = "",

    @SerialName("nombre")
    var nombre: String = "",

    @SerialName("correo")
    var correo: String = "",

    @SerialName("aliaspublico")
    var aliasPublico: String = "",

    @SerialName("aliasprivado")
    var aliasPrivado: String = "",

    @SerialName("activo")
    var activo: Boolean = false,

    @SerialName("descripcion")
    var descripcion: String = "",

    @SerialName("contrasennia")
    var contrasennia: String = "",

    /**
     * No mapeamos 'imagenPerfil' a la BD,
     * pues es un @Transient en tu clase original.
     */
    @Transient
    var imagenPerfil: DrawableResource? = null
) {
    // Constructor extra si quieres
    constructor(
        nombre: String,
        correo: String,
        aliasPublico: String,
        activo: Boolean,
        chatUser: ConversacionesUsuario?
    ) : this() {
        this.idUnico = UtilidadesUsuario().generarIdUnico()
        this.nombre = nombre
        this.correo = correo
        this.aliasPublico = aliasPublico
        this.aliasPrivado = hash(aliasPublico)
        this.activo = activo
        this.imagenPerfil = generarImagenPerfilRandom()
    }

    fun generarImagenPerfilRandom(): DrawableResource {
        val todasImagenes = imagenesPerfilPersona + imagenesPerfilAbstrasto + imagenesPerfilDibujo
        return todasImagenes.random().resource
    }

    //Debug: Contructor con idUnico
    constructor(idUnico: String, nombre: String, correo: String, aliasPublico: String, activo: Boolean) : this() {
        this.idUnico = idUnico
        this.nombre = nombre
        this.correo = correo
        this.aliasPublico = aliasPublico
        this.aliasPrivado = hash(aliasPublico)
        this.activo = activo
        this.imagenPerfil = generarImagenPerfilRandom()
    }

    // Constructor de copia
    constructor(usuario: Usuario) : this() {
        this.idUnico = usuario.idUnico
        this.nombre = usuario.nombre
        this.correo = usuario.correo
        this.aliasPublico = usuario.aliasPublico
        this.activo = usuario.activo
    }

    // Getter y Setter del usuario
    fun getNombreCompleto(): String {
        return nombre
    }
    fun setNombreCompleto(nombreCompleto: String) {
        this.nombre = nombreCompleto
    }

    fun getCorreo(): String {
        return correo
    }
    fun setCorreo(correo: String) {
        this.correo = correo
    }

    fun getAlias(): String {
        return aliasPublico
    }
    fun setAlias(alias: String) {
        this.aliasPublico = alias
    }

    fun getAliasPrivado(): String {
        return aliasPrivado
    }
    fun setAliasPrivado(aliasPrivado: String) {
        this.aliasPrivado = aliasPrivado
    }

    fun getIdUnico(): String {
        return idUnico
    }
    fun setIdUnico(idUnico: String) {
        this.idUnico = idUnico
    }

    fun getActivo(): Boolean {
        return activo
    }
    fun setActivo(activo: Boolean) {
        this.activo = activo
    }

    fun getDescripcion(): String {
        return descripcion
    }

    fun setDescripcion(descripcion: String) {
        this.descripcion = descripcion
    }

    fun getContrasennia(): String {
        return contrasennia
    }

    fun setContrasennia(contrasennia: String) {
        this.contrasennia = contrasennia
    }

    fun getImagenPerfil(): DrawableResource? {
        return imagenPerfil
    }

    fun setImagenPerfil(imagenPerfil: DrawableResource) {
        this.imagenPerfil = imagenPerfil
    }


    // Metodo para imprimir los datos públicos del usuario
    @Composable
    fun imprimirDatosPublicos() {
        Text("Nombre: $nombre")
        Text("Alias: $aliasPublico")
        Text("Activo: $activo")
    }

    // Metodo para imprimir los datos privados del usuario
    @Composable
    fun imprimirDatosPrivados() {
        Text("Correo: $correo")
        Text("Id Unico: $idUnico")
        Text("Alias Privado: $aliasPrivado")
    }
}

@Serializable
data class UsuarioBloqueado(
    @SerialName("idusuario")
    val idUsuario: String,   // ID del usuario que bloquea
    @SerialName("idbloqueado")
    val idBloqueado: String   // ID del usuario bloqueado
)

@Serializable
data class UsuarioContacto(
    @SerialName("idusuario")
    val idUsuario: String,   // ID del usuario
    @SerialName("idcontacto")
    val idContacto: String   // ID del contacto
)