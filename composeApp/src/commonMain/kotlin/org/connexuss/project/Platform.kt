package org.connexuss.project

/**
 * Interfaz para la abstracción de plataformas.
 *
 * Proporciona la propiedad que representa el nombre de la plataforma.
 */
interface Platform {
    val name: String
}

/**
 * Retorna una instancia de la plataforma actual.
 */
expect fun getPlatform(): Platform