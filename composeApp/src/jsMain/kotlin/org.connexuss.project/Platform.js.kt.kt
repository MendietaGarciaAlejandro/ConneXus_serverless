package org.connexuss.project

// File: Platform.js.kt

class JsPlatform: Platform {
    override val name: String = "Js"
}

actual fun getPlatform(): Platform = JsPlatform() // or WasmPlatform() if you want to use Kotlin/Wasm