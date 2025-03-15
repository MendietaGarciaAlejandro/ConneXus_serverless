package org.connexuss.project.usuario

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.ConversacionesUsuario
import org.connexuss.project.encriptacion.hash
import kotlin.random.Random

// Clase usuario con sus atributos y metodos
class Usuario {
    private var nombre: String = ""
    private var correo: String = ""
    private var aliasPublico: String = ""
    private var aliasPrivado: String = ""
    private var idUnico: String = ""
    private var activo: Boolean = false
    private lateinit var contactos: List<String>
    private lateinit var chatUser: ConversacionesUsuario
    private var descripcion: String = ""
    private var contrasennia: String = ""

    private var usuariosBloqueados: List<String> = emptyList()

    // Constructor completo
    constructor(nombre: String, correo: String, aliasPublico: String, activo: Boolean, contactos: List<String>, chatUser: ConversacionesUsuario) {
        this.idUnico = UtilidadesUsuario().generarIdUnico()
        this.nombre = nombre
        this.correo = correo
        this.aliasPublico = aliasPublico
        this.aliasPrivado = hash(aliasPublico)
        this.activo = activo
        this.contactos = contactos
        this.chatUser = chatUser
    }
    //Debug: Contructor con idUnico
    constructor(idUnico: String, nombre: String, correo: String, aliasPublico: String, activo: Boolean, contactos: List<String>, chatUser: ConversacionesUsuario) {
        this.idUnico = idUnico
        this.nombre = nombre
        this.correo = correo
        this.aliasPublico = aliasPublico
        this.aliasPrivado = hash(aliasPublico)
        this.activo = activo
        this.contactos = contactos
        this.chatUser = chatUser
    }

    // Constructor vacio
    constructor() {
    }

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

    fun getContactos(): List<String> {
        return contactos
    }

    fun setContactos(contactos: List<String>) {
        this.contactos = contactos
    }

    fun getChatUser(): ConversacionesUsuario {
        return chatUser
    }

    fun setChatUser(chatUser: ConversacionesUsuario) {
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

    private fun validarCorreo(correo: String): Boolean {
        return correo.contains("@")
    }

    fun validarNombre(nombre: String): Boolean {
        return nombre.isNotEmpty()
    }

    fun generarAlias(nombre: String): String {
        val alias = nombre.map { it.code }.joinToString("")
        return alias
    }

    fun instanciaUsuario(nombre: String,  correo: String, aliasPublico: String, activo: Boolean): Usuario {
        val correoValido = validarCorreo(correo)
        val nombreValido = validarNombre(nombre)
        val aliasPublicoValido = validarNombre(aliasPublico)
        if (!correoValido || !nombreValido || !aliasPublicoValido) {
            throw IllegalArgumentException("Datos de usuario no validos")
        }
        return Usuario(nombre, correo, aliasPublico, activo, emptyList(), ConversacionesUsuario(generarIdUnico(), generarIdUnico(), listOf( Conversacion( generarIdUnico(), emptyList() ) ) ) )
    }

    //Debug: Contructor con idUnico
    fun instanciaUsuario(idUnico: String, nombre: String, edad: Int, correo: String, aliasPublico: String, activo: Boolean): Usuario {
        val idUnico = idUnico
        val correoValido = validarCorreo(correo)
        val nombreValido = validarNombre(nombre)
        val aliasPublicoValido = validarNombre(aliasPublico)
        if (!correoValido || !nombreValido || !aliasPublicoValido) {
            throw IllegalArgumentException("Datos de usuario no validos")
        }
        return Usuario(idUnico, nombre, correo, aliasPublico, activo, emptyList(), ConversacionesUsuario(generarIdUnico(), generarIdUnico(), listOf( Conversacion( generarIdUnico(), emptyList() ) ) ) )
    }
}