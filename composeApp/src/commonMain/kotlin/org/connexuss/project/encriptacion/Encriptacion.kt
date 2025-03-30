package org.connexuss.project.encriptacion

/**
 * Genera un par de claves (pública y privada) a partir de valores fijos.
 *
 * @return Un par de cadenas que representan la clave pública y la clave privada.
 */
fun generarClaves(): Pair<String, String> {
    val clavePublica = "clavePublica".hashCode().toString()
    val clavePrivada = "clavePrivada".hashCode().toString()
    return Pair(clavePublica, clavePrivada)
}

/**
 * Calcula un hash simple de un texto utilizando el metodo hashCode.
 *
 * @param texto El texto a ser procesado.
 * @return El resultado del hash como cadena.
 */
fun hash(texto: String): String {
    return texto.hashCode().toString()
}