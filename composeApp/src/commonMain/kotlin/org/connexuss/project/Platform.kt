package org.connexuss.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform