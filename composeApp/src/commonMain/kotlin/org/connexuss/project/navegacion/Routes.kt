package org.connexuss.project.navegacion

import kotlinx.serialization.Serializable


sealed interface Routes {

    @Serializable
    data object HomeGraph : Routes

    @Serializable
    data object Home : Routes

    @Serializable
    data class HomeDetails(val id: String) : Routes
}