package org.connexuss.project.usuario

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import org.connexuss.project.comunicacion.ConversacionesUsuario
import org.connexuss.project.encriptacion.hash

// Clase usuario con sus atributos y metodos
class Usuario {
    private var nombre: String = ""
    private var edad: Int = 0
    private var correo: String = ""
    private var aliasPublico: String = ""
    private var aliasPrivado: String = ""
    private var idUnico: String = ""
    private var activo: Boolean = false
    private lateinit var contactos: List<String>
    private lateinit var chatUser: ConversacionesUsuario
    private var descripcion: String = ""
    private var contrasennia: String = ""

    // Constructor completo
    constructor(nombre: String, edad: Int, correo: String, aliasPublico: String, activo: Boolean, contactos: List<String>, chatUser: ConversacionesUsuario) {
        this.idUnico = UtilidadesUsuario().generarIdUnico()
        this.nombre = nombre
        this.edad = edad
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
        this.edad = usuario.edad
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

    fun getEdad(): Int {
        return edad
    }
    fun setEdad(edad: Int) {
        this.edad = edad
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


    // Metodo para imprimir los datos públicos del usuario
    @Composable
    fun imprimirDatosPublicos() {
        Text("Nombre: $nombre")
        Text("Alias: $aliasPublico")
        Text("Activo: $activo")
        Text("Edad: $edad")
    }

    // Metodo para imprimir los datos privados del usuario
    @Composable
    fun imprimirDatosPrivados() {
        Text("Correo: $correo")
        Text("Id Unico: $idUnico")
        Text("Alias Privado: $aliasPrivado")
    }
}