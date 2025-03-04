package org.connexuss.project.encriptacion

// funciones de encriptacion hash (Resumen)
fun hash(texto: String): String {
    return texto.hashCode().toString()
}