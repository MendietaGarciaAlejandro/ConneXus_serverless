package org.connexuss.project.encriptacion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.AES
import dev.whyoleg.cryptography.algorithms.SHA512
import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.launch
import okio.ByteString.Companion.toByteString
import org.connexuss.project.interfaces.DefaultTopBar
import org.connexuss.project.interfaces.LimitaTamanioAncho

/**
 * Calcula un hash simple de un texto utilizando el metodo hashCode.
 *
 * @param texto El texto a ser procesado.
 * @return El resultado del hash como cadena.
 */
fun hash(texto: String): String {
    return texto.hashCode().toString()
}

/**
 * Calcula el hash SHA-512 de un texto.
 *
 * @param textoOriginal El texto a ser procesado.
 * @return El hash SHA-512 como ByteArray.
 */
suspend fun calcularHashSHA512(textoOriginal: String): ByteArray {
    val proveedor = CryptographyProvider.Default
    val algoritmoSHA512 = proveedor.get(SHA512)
    val hasher = algoritmoSHA512.hasher()
    return hasher.hash(textoOriginal.encodeToByteArray())
}

// Función de extensión para convertir ByteArray a cadena hexadecimal.
fun ByteArray.toHex(): String = joinToString("") {
    it.toInt().and(0xFF).toString(16).padStart(2, '0')
}

// Función de extensión para convertir una cadena hexadecimal a ByteArray.
fun String.hexToByteArray(): ByteArray {
    require(length % 2 == 0) { "La longitud de la cadena hexadecimal debe ser par" }
    return chunked(2).map { it.toInt(16).toByte() }.toByteArray()
}

/**
 * Genera una clave AES de 256 bits (una sola vez).
 */
suspend fun generaClaveAES(): AES.GCM.Key {
    val proveedor = CryptographyProvider.Default
    val algoritmoAES = proveedor.get(AES.GCM)
    val claveGenerador = algoritmoAES.keyGenerator(AES.Key.Size.B256)
    return claveGenerador.generateKey()
}

/**
 * Encripta el texto utilizando la clave proporcionada.
 */
suspend fun pruebasEncriptacionAES(texto: String, clave: AES.GCM.Key): ByteArray {
    val cipher = clave.cipher()
    return cipher.encrypt(plaintext = texto.encodeToByteArray())
}

/**
 * Desencripta el texto utilizando la clave proporcionada.
 */
suspend fun pruebasDesencriptacionAES(texto: ByteArray, clave: AES.GCM.Key): String {
    val cipher = clave.cipher()
    return cipher.decrypt(ciphertext = texto).decodeToString()
}

@Composable
fun PantallaPruebasEncriptacion(navController: NavHostController) {
    // Clave AES, se genera una sola vez.
    var claveAES by remember { mutableStateOf<AES.GCM.Key?>(null) }

    // Estados para los textos
    var textoOriginal by remember { mutableStateOf("") }
    var textoHasheado by remember { mutableStateOf("") }

    // Estados para encriptación AES
    var textoGlobalACifrar by remember { mutableStateOf("") }
    var textoGlobalCifrado by remember { mutableStateOf("") }
    var textoGlobalDescifrado by remember { mutableStateOf("") }

    // Estado para mensaje de error
    var errorMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    // Generamos la clave AES una sola vez
    LaunchedEffect(Unit) {
        claveAES = generaClaveAES()
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = "Pruebas de encriptación",
                    navController = navController,
                    showBackButton = true,
                    irParaAtras = true,
                    muestraEngranaje = true
                )
            }
        ) { padding ->
            LimitaTamanioAncho { modifier ->
                // Usamos Column con verticalScroll para evitar superposición
                Column(
                    modifier = modifier
                        .padding(padding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Sección de hash (mantienes tu código existente)
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextField(
                            value = textoOriginal,
                            onValueChange = { textoOriginal = it },
                            label = { Text("Ingresa el texto") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(
                            onClick = {
                                scope.launch {
                                    val hashBytes = calcularHashSHA512(textoOriginal)
                                    textoHasheado = hashBytes.toHex()
                                }
                            }
                        ) {
                            Text("Calcular Hash")
                        }
                        TextField(
                            value = textoHasheado,
                            onValueChange = { },
                            label = { Text("Hash (SHA512)") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Sección para pruebas de encriptación AES
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextField(
                            value = textoGlobalACifrar,
                            onValueChange = { textoGlobalACifrar = it },
                            label = { Text("Texto a cifrar") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(
                            onClick = {
                                scope.launch {
                                    claveAES?.let { key ->
                                        val textoCifradoBytes = pruebasEncriptacionAES(textoGlobalACifrar, key)
                                        // Convertimos a hexadecimal para una representación legible
                                        textoGlobalCifrado = textoCifradoBytes.toHex()
                                    } ?: run {
                                        errorMessage = "Clave AES no inicializada"
                                    }
                                }
                            }
                        ) {
                            Text("Cifrar")
                        }
                        TextField(
                            value = textoGlobalCifrado,
                            onValueChange = { },
                            label = { Text("Texto cifrado (hex)") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true
                        )
                        Button(
                            onClick = {
                                scope.launch {
                                    if (textoGlobalCifrado.isNotEmpty()) {
                                        try {
                                            claveAES?.let { key ->
                                                // Convertimos el texto cifrado (hex) a ByteArray
                                                val textoCifradoBytes = textoGlobalCifrado.hexToByteArray()
                                                val textoDesencriptado = pruebasDesencriptacionAES(textoCifradoBytes, key)
                                                textoGlobalDescifrado = textoDesencriptado
                                            } ?: run {
                                                errorMessage = "Clave AES no inicializada"
                                            }
                                        } catch (e: Exception) {
                                            errorMessage = "Error al descifrar: ${e.message}"
                                        }
                                    } else {
                                        errorMessage = "No hay texto cifrado para descifrar"
                                    }
                                }
                            }
                        ) {
                            Text("Descifrar")
                        }
                        TextField(
                            value = textoGlobalDescifrado,
                            onValueChange = { },
                            label = { Text("Texto descifrado") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true
                        )
                    }
                    if (errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            errorMessage,
                            color = MaterialTheme.colors.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}