package org.connexuss.project.usuario

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