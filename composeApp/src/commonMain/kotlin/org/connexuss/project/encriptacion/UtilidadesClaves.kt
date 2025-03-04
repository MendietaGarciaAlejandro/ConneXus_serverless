package org.connexuss.project.encriptacion

// funcion que devuelve un par de claves publica y privada
fun generarClaves(): Pair<String, String> {
    val clavePublica = "clavePublica".hashCode().toString()
    val clavePrivada = "clavePrivada".hashCode().toString()
    return Pair(clavePublica, clavePrivada)
}