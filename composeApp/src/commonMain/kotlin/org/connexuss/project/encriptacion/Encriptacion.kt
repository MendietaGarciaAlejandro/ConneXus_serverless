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
import dev.whyoleg.cryptography.BinarySize.Companion.bytes
import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.AES
import dev.whyoleg.cryptography.algorithms.EC
import dev.whyoleg.cryptography.algorithms.ECDSA
import dev.whyoleg.cryptography.algorithms.HMAC
import dev.whyoleg.cryptography.algorithms.SHA256
import dev.whyoleg.cryptography.algorithms.SHA512
import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.connexuss.project.comunicacion.Conversacion
import org.connexuss.project.comunicacion.Hilo
import org.connexuss.project.comunicacion.Post
import org.connexuss.project.comunicacion.Tema
import org.connexuss.project.comunicacion.generateId
import org.connexuss.project.interfaces.navegacion.DefaultTopBar
import org.connexuss.project.interfaces.comun.LimitaTamanioAncho
import org.connexuss.project.interfaces.foro.ClaveTemaHolder
import org.connexuss.project.misc.UsuarioPrincipal
import org.connexuss.project.supabase.SupabaseConversacionesRepositorio
import org.connexuss.project.supabase.SupabaseHiloRepositorio
import org.connexuss.project.supabase.SupabasePostsRepositorio
import org.connexuss.project.supabase.SupabaseSecretosRepo
import org.connexuss.project.supabase.SupabaseTemasRepositorio
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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

/**
 * Convierte un ByteArray a una cadena hexadecimal.
 *
 * @param bytes El ByteArray a convertir.
 * @return La representación hexadecimal como String.
 */
fun hash(texto: String): String {
    return texto.hashCode().toString()
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
suspend fun generarClaveSimetricaAES(): AES.GCM.Key {
    val proveedor = CryptographyProvider.Default
    val algoritmoAES = proveedor.get(AES.GCM)
    val generadorClave = algoritmoAES.keyGenerator(AES.Key.Size.B256)
    return generadorClave.generateKey()
}

/**
 * Encripta datos con una clave AES-GCM y devuelve el vector de inicialización y el texto cifrado.
 *
 * @param datosPlanos Los datos a encriptar.
 * @return Un par con el vector de inicialización (IV) y el texto cifrado.
 */
suspend fun AES.GCM.Key.encriptarContenido(datosPlanos: ByteArray): Pair<ByteArray, ByteArray> {
    val datosCifrados = this.cipher().encrypt(datosPlanos)
    val vectorInicializacion = datosCifrados.sliceArray(0 until 12)
    val textoCifrado = datosCifrados.sliceArray(12 until datosCifrados.size)
    return vectorInicializacion to textoCifrado
}

/**
 * Desencripta datos con una clave AES-GCM.
 *
 * @param iv El vector de inicialización (IV).
 * @param textoCifrado El texto cifrado a desencriptar.
 * @return Los datos desencriptados.
 */
suspend fun AES.GCM.Key.desencriptarContenido(iv: ByteArray, textoCifrado: ByteArray): ByteArray {
    val cipher = this.cipher()
    return cipher.decrypt(iv + textoCifrado)
}

/**
 * Encripta una cadena de texto con una clave AES-GCM.
 *
 * @param textoPlano El texto a encriptar.
 * @return Un par con el vector de inicialización (IV) y el texto cifrado.
 */
suspend fun AES.GCM.Key.encriptarTexto(textoPlano: String): Pair<ByteArray, ByteArray> {
    return this.encriptarContenido(textoPlano.encodeToByteArray())
}

/**
 * Desencripta un texto cifrado con una clave AES-GCM.
 *
 * @param iv El vector de inicialización (IV).
 * @param textoCifrado El texto cifrado a desencriptar.
 * @return El texto desencriptado como cadena.
 */
suspend fun AES.GCM.Key.desencriptarTexto(iv: ByteArray?, textoCifrado: ByteArray): String {
    val datosDesencriptados = iv?.let { this.desencriptarContenido(it, textoCifrado) }
    if (datosDesencriptados != null) {
        return datosDesencriptados.decodeToString()
    }
    return ""
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

/**
 * Genera una clave HMAC utilizando SHA-512.
 */
suspend fun generaClaveHMAC(digest: SHA512): HMAC.Key {
    val provider = CryptographyProvider.Default
    val hmac = provider.get(HMAC)
    val keyGenerator = hmac.keyGenerator(digest)
    return keyGenerator.generateKey()
}

/**
 * Genera una firma HMAC para el texto proporcionado utilizando la clave HMAC.
 */
suspend fun generaFirmaHMAC(texto: String, key: HMAC.Key): ByteArray {
    return key.signatureGenerator().generateSignature(texto.encodeToByteArray())
}

/**
 * Verifica la firma HMAC utilizando la clave HMAC.
 */
suspend fun verificaFirmaHMAC(texto: String, signature: ByteArray, key: HMAC.Key): Boolean {
    return try {
        key.signatureVerifier().verifySignature(texto.encodeToByteArray(), signature)
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * Codifica la clave HMAC a formato DER.
 */
suspend fun codificaClaveHMAC(key: HMAC.Key, format: HMAC.Key.Format): ByteArray {
    return key.encodeToByteArray(format)
}

/**
 * Decodifica la clave HMAC desde formato DER.
 */
suspend fun decodificaClaveHMAC(
    encodedKey: ByteArray,
    digest: SHA512,
    format: HMAC.Key.Format = HMAC.Key.Format.RAW
): HMAC.Key {
    val provider = CryptographyProvider.Default
    val hmac = provider.get(HMAC)
    return hmac.keyDecoder(digest).decodeFromByteArray(format, encodedKey)
}

/**
 * Realiza pruebas de HMAC generando una clave, firmando un texto y verificando la firma.
 *
 * @param texto El texto a ser firmado y verificado.
 * @return true si la verificación es exitosa, false en caso contrario.
 */
suspend fun pruebasHMAC(texto: String): Boolean {
    // Generar clave HMAC
    val clave = generaClaveHMAC(digest = SHA512)

    // Generar firma usando la clave
    val firma = generaFirmaHMAC(texto, clave)

    // Verificar la firma con la misma clave
    val verificacionInicial = verificaFirmaHMAC(texto, firma, clave)
    println("Verificación inicial: $verificacionInicial")

    // Codificar la clave a formato RAW
    val claveCodificada = codificaClaveHMAC(
        clave,
        format = HMAC.Key.Format.RAW
    )

    // Decodificar la clave a partir del arreglo de bytes
    val claveDecodificada = decodificaClaveHMAC(
        claveCodificada,
        digest = SHA512,
        format = HMAC.Key.Format.RAW
    )

    // Verificar la firma usando la clave decodificada
    val verificacionDecodificada = verificaFirmaHMAC(texto, firma, claveDecodificada)
    println("Verificación con clave decodificada: $verificacionDecodificada")

    // Devuelve true si ambas verificaciones son exitosas
    return verificacionInicial && verificacionDecodificada
}

/**
 * Genera un par de claves ECDSA utilizando la curva especificada.
 */
suspend fun generaClaveECDSA(curve: EC.Curve = EC.Curve.P521): ECDSA.KeyPair {
    val provider = CryptographyProvider.Default
    val ecdsa = provider.get(ECDSA)
    val keyPairGenerator = ecdsa.keyPairGenerator(curve)
    return keyPairGenerator.generateKey()
}

/**
 * Genera una firma ECDSA para el texto proporcionado utilizando la clave privada.
 */
suspend fun generaFirmaECDSA(
    texto: String,
    privateKey: ECDSA.PrivateKey,
    digest: SHA512,
    format: ECDSA.SignatureFormat = ECDSA.SignatureFormat.DER
): ByteArray {
    return privateKey
        .signatureGenerator(digest = digest, format = format)
        .generateSignature(texto.encodeToByteArray())
}

/**
 * Verifica la firma ECDSA utilizando la clave pública.
 */
suspend fun verificaFirmaECDSA(
    texto: String,
    signature: ByteArray,
    publicKey: ECDSA.PublicKey,
    digest: SHA512,
    format: ECDSA.SignatureFormat = ECDSA.SignatureFormat.DER
): Boolean {
    return try {
        publicKey
            .signatureVerifier(digest = digest, format = format)
            .verifySignature(texto.encodeToByteArray(), signature)
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * Codifica la clave pública a formato DER.
 */
suspend fun codificaClavePublica(
    publicKey: ECDSA.PublicKey,
    format: EC.PublicKey.Format = EC.PublicKey.Format.DER
): ByteArray {
    return publicKey.encodeToByteArray(format)
}

/**
 * Decodifica la clave pública desde formato DER.
 */
suspend fun decodificaClavePublica(
    encodedKey: ByteArray,
    curve: EC.Curve = EC.Curve.P521,
    format: EC.PublicKey.Format = EC.PublicKey.Format.DER
): ECDSA.PublicKey {
    val provider = CryptographyProvider.Default
    val ecdsa = provider.get(ECDSA)
    return ecdsa.publicKeyDecoder(curve).decodeFromByteArray(format, encodedKey)
}

/**
 * Realiza pruebas de ECDSA generando un par de claves, firmando un texto y verificando la firma.
 *
 * @param texto El texto a ser firmado y verificado.
 * @return true si la verificación es exitosa, false en caso contrario.
 */
suspend fun pruebasECDSA(texto: String): Boolean {
    // Generar par de claves ECDSA
    val keyPair = generaClaveECDSA()

    // Generar firma usando la clave privada
    val signature = generaFirmaECDSA(
        texto, keyPair.privateKey,
        digest = SHA512,
        format = ECDSA.SignatureFormat.DER
    )

    // Verificar la firma con la clave pública
    val verificationResult = verificaFirmaECDSA(
        texto,
        signature,
        keyPair.publicKey,
        digest = SHA512,
        format = ECDSA.SignatureFormat.DER)
    println("Resultado de verificación inicial: $verificationResult")

    // Codificar la clave pública a formato DER
    val encodedPublicKey = codificaClavePublica(keyPair.publicKey)

    // Decodificar la clave pública
    val decodedPublicKey = decodificaClavePublica(encodedPublicKey)

    // Verificar la firma con la clave pública decodificada
    val decodedKeyVerificationResult = verificaFirmaECDSA(
        texto,
        signature,
        decodedPublicKey,
        digest = SHA512,
        format = ECDSA.SignatureFormat.DER)
    println("Resultado de verificación con clave decodificada: $decodedKeyVerificationResult")

    // Devolver true si ambas verificaciones son exitosas
    return verificationResult && decodedKeyVerificationResult
}

@Composable
fun PantallaPruebasEncriptacion(navController: NavHostController) {

    // Estados para los textos
    var textoOriginal by remember { mutableStateOf("") }
    var textoHasheado by remember { mutableStateOf("") }

    // --- Estados para AES ---
    var claveAES by remember { mutableStateOf<AES.GCM.Key?>(null) }
    var textoAES by remember { mutableStateOf("") }
    var resultadoCifradoAES by remember { mutableStateOf("") }
    var resultadoDescifradoAES by remember { mutableStateOf("") }

    // --- Estados para HMAC ---
    var claveHMAC by remember { mutableStateOf<HMAC.Key?>(null) }
    var textoHMAC by remember { mutableStateOf("") }
    var firmaHMAC by remember { mutableStateOf("") }
    var verificacionHMAC by remember { mutableStateOf("") }

    // --- Estados para ECDSA ---
    var keyPairECDSA by remember { mutableStateOf<ECDSA.KeyPair?>(null) }
    var textoECDSA by remember { mutableStateOf("") }
    var firmaECDSA by remember { mutableStateOf("") }
    var verificacionECDSA by remember { mutableStateOf("") }

    // Estado para mensajes de error generales
    var errorMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    // Generamos las claves/pares una sola vez
    LaunchedEffect(Unit) {
        claveAES = generarClaveSimetricaAES()
        claveHMAC = generaClaveHMAC(SHA512)
        keyPairECDSA = generaClaveECDSA()
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
                        // ===== Sección Hash (SHA-512) =====
                        Text("Pruebas Hash", style = MaterialTheme.typography.h6)
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
                    Text(
                        "--------------------------------------",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    // Sección para pruebas de encriptación AES
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // ===== Sección AES (Encriptación simétrica) =====
                        Text("Pruebas AES", style = MaterialTheme.typography.h6)
                        TextField(
                            value = textoAES,
                            onValueChange = { textoAES = it },
                            label = { Text("Texto AES a cifrar") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(onClick = {
                            scope.launch {
                                claveAES?.let { key ->
                                    val cifradoBytes = pruebasEncriptacionAES(textoAES, key)
                                    // Convertimos a hexadecimal para representación legible
                                    resultadoCifradoAES = cifradoBytes.toHex()
                                } ?: run {
                                    errorMessage = "Clave AES no inicializada"
                                }
                            }
                        }) { Text("Cifrar AES") }
                        TextField(
                            value = resultadoCifradoAES,
                            onValueChange = { },
                            label = { Text("Texto cifrado (AES, hex)") },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(onClick = {
                            scope.launch {
                                if (resultadoCifradoAES.isNotEmpty()) {
                                    try {
                                        claveAES?.let { key ->
                                            val cifradoBytes = resultadoCifradoAES.hexToByteArray()
                                            resultadoDescifradoAES =
                                                pruebasDesencriptacionAES(cifradoBytes, key)
                                        } ?: run {
                                            errorMessage = "Clave AES no inicializada"
                                        }
                                    } catch (e: Exception) {
                                        errorMessage = "Error al descifrar AES: ${e.message}"
                                    }
                                } else {
                                    errorMessage = "No hay texto cifrado para descifrar (AES)"
                                }
                            }
                        }) { Text("Descifrar AES") }
                        TextField(
                            value = resultadoDescifradoAES,
                            onValueChange = { },
                            label = { Text("Texto descifrado (AES)") },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Text(
                        "--------------------------------------",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // ===== Sección HMAC (Autenticación/MAC) =====
                        Text("Pruebas HMAC", style = MaterialTheme.typography.h6)
                        TextField(
                            value = textoHMAC,
                            onValueChange = { textoHMAC = it },
                            label = { Text("Texto para HMAC") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(onClick = {
                            scope.launch {
                                claveHMAC?.let { key ->
                                    val firmaBytes = generaFirmaHMAC(textoHMAC, key)
                                    firmaHMAC = firmaBytes.toHex()
                                } ?: run {
                                    errorMessage = "Clave HMAC no inicializada"
                                }
                            }
                        }) { Text("Generar Firma HMAC") }
                        TextField(
                            value = firmaHMAC,
                            onValueChange = { },
                            label = { Text("Firma HMAC (hex)") },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(onClick = {
                            scope.launch {
                                claveHMAC?.let { key ->
                                    val verif = verificaFirmaHMAC(
                                        textoHMAC,
                                        firmaHMAC.hexToByteArray(),
                                        key
                                    )
                                    verificacionHMAC =
                                        if (verif) "Firma válida" else "Firma inválida"
                                } ?: run {
                                    errorMessage = "Clave HMAC no inicializada"
                                }
                            }
                        }) { Text("Verificar Firma HMAC") }
                        TextField(
                            value = verificacionHMAC,
                            onValueChange = { },
                            label = { Text("Resultado verificación HMAC") },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Text(
                        "--------------------------------------",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // ===== Sección ECDSA (Firma digital asimétrica) =====
                        Text("Pruebas ECDSA", style = MaterialTheme.typography.h6)
                        TextField(
                            value = textoECDSA,
                            onValueChange = { textoECDSA = it },
                            label = { Text("Texto para ECDSA") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(onClick = {
                            scope.launch {
                                keyPairECDSA?.let { kp ->
                                    val firmaBytes = generaFirmaECDSA(
                                        textoECDSA,
                                        kp.privateKey,
                                        digest = SHA512,
                                        format = ECDSA.SignatureFormat.DER
                                    )
                                    firmaECDSA = firmaBytes.toHex()
                                } ?: run {
                                    errorMessage = "Par de claves ECDSA no inicializado"
                                }
                            }
                        }) { Text("Generar Firma ECDSA") }
                        TextField(
                            value = firmaECDSA,
                            onValueChange = { },
                            label = { Text("Firma ECDSA (hex)") },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(onClick = {
                            scope.launch {
                                keyPairECDSA?.let { kp ->
                                    val verif = verificaFirmaECDSA(
                                        textoECDSA,
                                        firmaECDSA.hexToByteArray(),
                                        kp.publicKey,
                                        digest = SHA512,
                                        format = ECDSA.SignatureFormat.DER
                                    )
                                    verificacionECDSA =
                                        if (verif) "Firma válida" else "Firma inválida"
                                } ?: run {
                                    errorMessage = "Par de claves ECDSA no inicializado"
                                }
                            }
                        }) { Text("Verificar Firma ECDSA") }
                        TextField(
                            value = verificacionECDSA,
                            onValueChange = { },
                            label = { Text("Resultado verificación ECDSA") },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Mensaje de error general
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
}

/**
 * Clase para representar un secreto en la base de datos.
 * Esta clase es utilizada para almacenar el resultado de la consulta a la tabla "vault.secrets".
 */
@Serializable
data class Secreto @OptIn(ExperimentalUuidApi::class) constructor(
    /** UUID único de la fila */
    @SerialName("id")
    val id: String = Uuid.random().toString(),

    /** Nombre descriptivo de la clave */
    @SerialName("name")
    val name: String,

    /** Descripción opcional */
    @SerialName("description")
    val description: String? = null,

    /** Valor de la clave simétrica (hex o Base64) */
    @SerialName("secret")
    val secret: String,

    /** UUID de la clave maestra en Vault o KMS */
    @SerialName("key_id")
    val keyId: String,

    /** Nonce usado en el cifrado (hex)
     *  Mapeado desde la columna bytea en formato hex
     */
    @SerialName("nonce")
    val nonceHex: String,

    /** Timestamp de creación (timestamptz) */
    @SerialName("created_at")
    val createdAt: Instant = Clock.System.now(),

    /** Timestamp de última modificación (timestamptz) */
    @SerialName("updated_at")
    val updatedAt: Instant? = null
)

// Reutilizamos tu data class Secreto si tu función devuelve exactamente esas columnas:
@Serializable
data class SecretoInsertado(
    @SerialName("id")
    val id: String? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("description")
    val description: String? = null,

    @SerialName("secret")
    val secret: String? = null,

    @SerialName("key_id")
    val keyId: String? = null,

    @SerialName("nonce")
    val nonce: String? = null,

    @SerialName("created_at")
    val createdAt: Instant? = null,

    @SerialName("updated_at")
    val updatedAt: Instant? = null
)

@Serializable
data class SecretoRPC(
    @SerialName("id")
    val id: String? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("secret")
    val secret: String? = null,

    @SerialName("decrypted_secret")
    val decryptedSecret: String? = null,

    @SerialName("nonce")
    val nonce: String? = null,
)

@OptIn(ExperimentalEncodingApi::class)
suspend fun encriptarTexto(
    textoPlano: String,
    clave: AES.GCM.Key): String
{
    val noPad = Base64.withPadding(Base64.PaddingOption.ABSENT)
    val bytesCifrados = clave.cipher().encrypt(textoPlano.toByteArray())
    return noPad.encode(bytesCifrados)
}

@OptIn(ExperimentalEncodingApi::class)
suspend fun desencriptaTexto(
    textoCifrado: String,
    clave: AES.GCM.Key
): String {
    val noPad = Base64.withPadding(Base64.PaddingOption.ABSENT)
    val bytesCifrados = noPad.decode(textoCifrado)
    val textoDesencriptado = clave.cipher().decrypt(bytesCifrados)
    return textoDesencriptado.decodeToString()
}

class EncriptacionSimetricaForo {

    /** Genera y devuelve una clave AES‑GCM de 256 bits */
    private suspend fun generarClaveAES(): AES.GCM.Key {
        val provider = CryptographyProvider.Default
        return provider.get(AES.GCM)
            .keyGenerator(AES.Key.Size.B256)
            .generateKey()
    }

    /**
     * Cifra `texto`, devuelve Pair(nonce, ciphertextConTag),
     * donde nonce son los primeros 12 bytes de encrypted.
     */
    suspend fun AES.GCM.Key.encriptar(texto: ByteArray): ByteArray {
        // encrypt() ya genera un nonce de 12 bytes y lo pone al principio
        return this.cipher().encrypt(texto)
    }

    /**
     * Cifra `plaintext`, devuelve Pair(nonce, ciphertextWithTag).
     * El méto-do cipher().encrypt() ya devuelve nonce||ciphertext||tag.
     * Aquí extraemos nonce (12 bytes) y el resto.
     */
    private suspend fun AES.GCM.Key.encriptarSplit(plaintext: ByteArray): Pair<ByteArray, ByteArray> {
        val encrypted = this.cipher().encrypt(plaintext)
        // 12 bytes de nonce (96 bits) según NIST
        val nonce = encrypted.copyOfRange(0, 12)
        val cipherAndTag = encrypted.copyOfRange(12, encrypted.size)
        return nonce to cipherAndTag
    }

    @Serializable
    data class VaultSecretInsert(

        @SerialName("secret")
        val secret: String,    // Base64(keyBytes)

        @SerialName("name")
        val name: String       // Identificador, p.ej. "tema-123"
    )

    @Serializable
    data class VaultSecretSelect(

        @SerialName("decrypted_secret")
        val decryptedSecret: String
    )

    /**
     * Dado el ciphertext con tag y el nonce, descifra.
     */
    suspend fun AES.GCM.Key.desencriptar(encrypted: ByteArray): ByteArray {
        // decrypt() extrae internamente los primeros 12 bytes como nonce
        return this.cipher().decrypt(encrypted)
    }


    /** Cifra y devuelve iv||ciphertext||tag juntos */
    suspend fun AES.GCM.Key.encriptarFull(plaintext: ByteArray): ByteArray =
        this.cipher().encrypt(plaintext)

    /** Descifra iv||ciphertext||tag de un solo golpe */
    suspend fun AES.GCM.Key.desencriptarFull(encrypted: ByteArray): ByteArray =
        this.cipher().decrypt(encrypted)

    /**
     * Crea un tema:
     * 1) Genera clave y cifra nombre.
     * 2) Inserta clave + nonce en vault.secrets vía RPC.
     * 3) Inserta tema cifrado en la tabla temas.
     */
    @OptIn(ExperimentalEncodingApi::class)
    suspend fun crearTemaSinPadding(
        nombrePlain: String,
        secretsRpcRepo: SupabaseSecretosRepo
    ): Tema {
        // 1) Generar clave y cifrar
        val key = generarClaveAES()
        val fullEncrypted = key.encriptarFull(nombrePlain.encodeToByteArray())
        val temaId = generateId()

        // 2) Insertar secreto en vault via RPC
        // convertimos key a Base64 estándar (con padding) o hex según tu función RPC
        val keyB64 = Base64.encode(key.encodeToByteArray(AES.Key.Format.RAW))
        try {
            secretsRpcRepo.insertarSecretoSimpleConRpc(
                temaId = temaId,
                claveHex = keyB64
            )
        } catch (e: Exception) {
            throw IllegalStateException("Error al insertar clave en Vault: ${e.message}")
        }

        // 3) Codificar ciphertext sin padding para la tabla temas
        val noPad = Base64.withPadding(Base64.PaddingOption.ABSENT)
        val nombreB64 = noPad.encode(fullEncrypted)

        val repoTema = SupabaseTemasRepositorio()

        val temaResultado = Tema(
            idTema = temaId,
            nombre = nombreB64
        )

        try {
            repoTema.addTema(temaResultado)
        } catch (e: Exception) {
            // Ignora el error, ya que la inserción se hace en el RPC
        }

        return temaResultado
    }

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun crearHiloSinPadding(
        nombrePlain: String,
        idTema: String
    ): Hilo {
        // 1) Generar clave y cifrar
        val key = ClaveTemaHolder.clave
            ?: throw IllegalStateException("Clave AES no inicializada")
        val fullEncrypted = key.encriptarFull(nombrePlain.encodeToByteArray())
        val hiloId = generateId()

        // 3) Codificar ciphertext sin padding para la tabla temas
        val noPad = Base64.withPadding(Base64.PaddingOption.ABSENT)
        val nombreB64 = noPad.encode(fullEncrypted)

        val repoTema = SupabaseHiloRepositorio()

        val hiloResultado = Hilo(
            idHilo = hiloId,
            nombre = nombreB64,
            idTema = idTema,
        )

        try {
            repoTema.addHilo(hiloResultado)
        } catch (e: Exception) {
            // Ignora el error, ya que la inserción se hace en el RPC
        }

        return hiloResultado
    }

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun crearPostSinPadding(
        nombrePlain: String,
        idHilo: String,
        aliaspublico: String,
        idFirmante: String
    ): Post {
        // 1) Generar clave y cifrar
        val key = ClaveTemaHolder.clave
            ?: throw IllegalStateException("Clave AES no inicializada")
        val fullEncrypted = key.encriptarFull(nombrePlain.encodeToByteArray())
        val postId = generateId()

        // 3) Codificar ciphertext sin padding para la tabla temas
        val noPad = Base64.withPadding(Base64.PaddingOption.ABSENT)
        val contenido64 = noPad.encode(fullEncrypted)

        val repoPosts = SupabasePostsRepositorio()

            val postResultado = Post(
                idPost = postId,
                content = contenido64,
                idHilo = idHilo,
                aliaspublico = aliaspublico,
                idFirmante = idFirmante,
            )

        try {
            repoPosts.addPost(postResultado)
        } catch (e: Exception) {
            // Ignora el error, ya que la inserción se hace en el RPC
        }

        return postResultado
    }

    /**
     * Recupera y desencripta un tema:
     * 1) Recupera clave y nonce desde vault via Edge Function.
     * 2) Recupera ciphertext de la tabla temas.
     * 3) Reconstruye y desencripta.
     */
    @OptIn(ExperimentalEncodingApi::class)
    suspend fun leerTema(
        temaId: String,
        secretsRpcRepo: SupabaseSecretosRepo
    ): String {
        // 1) Recuperar secreto desencriptado vía Edge Function
        val secretoDesencriptado = secretsRpcRepo.recuperarSecretoSimpleRpc(temaId)
            ?: throw IllegalStateException("Secreto no disponible para tema $temaId")

        // 2) Decodificar clave RAW (Base64 estándar con padding)
        val keyBytes = secretoDesencriptado.decryptedSecret.let { Base64.decode(it) }
        val aesKey = keyBytes.let {
            CryptographyProvider.Default
                .get(AES.GCM)
                .keyDecoder()
                .decodeFromByteArray(AES.Key.Format.RAW, it)
        }

        val repoSupabaseTema = SupabaseTemasRepositorio()

        // 4) Recuperar solo el ciphertext de la tabla temas
        val tema = repoSupabaseTema.getTemaPorId(temaId).first()
        val temaNombre = tema?.nombre
            ?: throw IllegalStateException("Tema no disponible para id $temaId")

        // 5) Decodificar ciphertext+tag (Base64 sin padding)
        val noPad = Base64.withPadding(Base64.PaddingOption.ABSENT)
        val encryptedFull: ByteArray = noPad.decode(temaNombre)

        // 7) Desencriptar con la clave y devolver texto
        val plainBytes: ByteArray = aesKey.cipher().decrypt(encryptedFull)
        return plainBytes.decodeToString()
    }

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun leerHilo(
        hiloId: String,
        clave: AES.GCM.Key,
    ): String {

        val repoSupabaseHilo = SupabaseHiloRepositorio()

        // 4) Recuperar solo el ciphertext de la tabla temas
        val hilo = repoSupabaseHilo.getHiloPorId(hiloId).first()
        val hiloNombre = hilo?.nombre
            ?: throw IllegalStateException("Hilo no disponible para id $hiloId")

        // 5) Decodificar ciphertext+tag (Base64 sin padding)
        val noPad = Base64.withPadding(Base64.PaddingOption.ABSENT)
        val encryptedFull: ByteArray = noPad.decode(hiloNombre)

        // 7) Desencriptar con la clave y devolver texto
        val plainBytes: ByteArray = clave.cipher().decrypt(encryptedFull)
        return plainBytes.decodeToString()
    }

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun leerPost(
        postId: String,
        clave: AES.GCM.Key,
    ): String {

        val repoSupabaseTema = SupabasePostsRepositorio()

        // 4) Recuperar solo el ciphertext de la tabla temas
        val post = repoSupabaseTema.getPostPorId(postId).first()
        val postContenido = post?.content
            ?: throw IllegalStateException("Post no disponible para id $postId")

        // 5) Decodificar ciphertext+tag (Base64 sin padding)
        val noPad = Base64.withPadding(Base64.PaddingOption.ABSENT)
        val encryptedFull: ByteArray = noPad.decode(postContenido)

        // 7) Desencriptar con la clave y devolver texto
        val plainBytes: ByteArray = clave.cipher().decrypt(encryptedFull)
        return plainBytes.decodeToString()
    }
}

class EncriptacionSimetricaChats {

    /** Genera y devuelve una clave AES‑GCM de 256 bits */
    private suspend fun generarClaveAES(): AES.GCM.Key {
        val provider = CryptographyProvider.Default
        return provider.get(AES.GCM)
            .keyGenerator(AES.Key.Size.B256)
            .generateKey()
    }

    /** Cifra y devuelve iv||ciphertext||tag juntos */
    private suspend fun AES.GCM.Key.encriptarFull(plaintext: ByteArray): ByteArray =
        this.cipher().encrypt(plaintext)

    /** Descifra iv||ciphertext||tag de un solo golpe */
    suspend fun AES.GCM.Key.desencriptarFull(encrypted: ByteArray): ByteArray =
        this.cipher().decrypt(encrypted)

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun crearChatSinPadding(
        nombrePlain: String?,
        secretsRpcRepo: SupabaseSecretosRepo
    ): Conversacion {
        // 1) Generar clave y cifrar
        val key = generarClaveAES()
        val fullEncrypted = nombrePlain?.let { key.encriptarFull(it.encodeToByteArray()) }
        val chatId = generateId()

        // 2) Insertar secreto en vault via RPC
        // convertimos key a Base64 estándar (con padding) o hex según tu función RPC
        val keyB64 = Base64.encode(key.encodeToByteArray(AES.Key.Format.RAW))
        try {
            secretsRpcRepo.insertarSecretoSimpleConRpc(
                temaId = chatId,
                claveHex = keyB64
            )
        } catch (e: Exception) {
            throw IllegalStateException("Error al insertar clave en Vault: ${e.message}")
        }

        // 3) Codificar ciphertext sin padding para la tabla temas
        val noPad = Base64.withPadding(Base64.PaddingOption.ABSENT)
        val nombreB64 = fullEncrypted?.let { noPad.encode(it) }

        val converRepo = SupabaseConversacionesRepositorio()

        val converResultado = Conversacion(
            id = chatId,
            nombre = nombreB64
        )

        try {
            converRepo.addConversacion(converResultado)
        } catch (e: Exception) {
            // Ignora el error, ya que la inserción se hace en el RPC
        }

        return converResultado
    }

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun leerConversacion(
        converId: String,
        secretsRpcRepo: SupabaseSecretosRepo
    ): String? {
        // 1) Recuperar secreto desencriptado vía Edge Function
        val secretoDesencriptado = secretsRpcRepo.recuperarSecretoSimpleRpc(converId)
            ?: throw IllegalStateException("Secreto no disponible para tema $converId")

        // 2) Decodificar clave RAW (Base64 estándar con padding)
        val keyBytes = secretoDesencriptado.decryptedSecret.let { Base64.decode(it) }
        val aesKey = keyBytes.let {
            CryptographyProvider.Default
                .get(AES.GCM)
                .keyDecoder()
                .decodeFromByteArray(AES.Key.Format.RAW, it)
        }

        val repoSupabaseConvers = SupabaseConversacionesRepositorio()

        // 4) Recuperar solo el ciphertext de la tabla temas
        val conver = repoSupabaseConvers.getConversacionPorId(converId).first()
        val converNombre = conver?.nombre

        // 5) Decodificar ciphertext+tag (Base64 sin padding)
        val noPad = Base64.withPadding(Base64.PaddingOption.ABSENT)
        val encryptedFull: ByteArray? = converNombre?.let { noPad.decode(it) }

        // 7) Desencriptar con la clave y devolver texto
        val plainBytes: ByteArray? = encryptedFull?.let { aesKey.cipher().decrypt(it) }
        if (plainBytes != null) {
            return plainBytes.decodeToString()
        }
        return null
    }
}

object EncriptacionResumenUsuario {
    private val provider = CryptographyProvider.Default
    private val SALT     = "MiSaltFijo1234".encodeToByteArray()

    /** Público: genera el hash hex de `password`. */
    suspend fun hashPassword(password: String): String = withContext(Dispatchers.Default) {
        val sha256 = provider.get(SHA256)
        val hasher = sha256.hasher()
        val input  = SALT + password.encodeToByteArray()
        hasher.hash(input).toString() // ByteString.toString() produce hex
    }

    /** Público: verifica `password` frente a `hashHex`. */
    suspend fun checkPassword(password: String, hashHex: String): Boolean =
        withContext(Dispatchers.Default) {
            val candidate = hashPassword(password)
            if (candidate.length != hashHex.length) return@withContext false
            var eq = true
            for (i in candidate.indices) {
                eq = eq and (candidate[i] == hashHex[i])
            }
            eq
        }
}

//object GroupCrypto {
//
//    private val provider = CryptographyProvider.Default
//
//    /** 1) Cada usuario genera su par ECDH P‑256. */
//    private suspend fun generateECDHKeyPair(): ECDH.KeyPair {
//        val curve = EC.Curve.P256
//        return provider.get(ECDH).keyPairGenerator(curve).generateKey()
//    }
//
//    /**
//     * Genera un par de claves ECDH y devuelve la clave privada y pública en formato RAW.
//     * La clave privada es de 32 bytes y la pública es de 65 bytes.
//     */
//    suspend fun cretePublicAndPrivateKeyPair(): Pair<ByteArray, ByteArray> {
//        val keyPair = generateECDHKeyPair()
//        val privateKey = keyPair.privateKey.encodeToByteArray(EC.PrivateKey.Format.RAW)
//        val publicKey = keyPair.publicKey.encodeToByteArray(EC.PublicKey.Format.RAW)
//        return privateKey to publicKey
//    }
//
//    /**
//     * 2) Deriva la clave de grupo a partir de la clave privada del admin y la pública del miembro.
//     *    – La clave de grupo es de 32 bytes.
//     */
//    suspend fun deriveGroupKey(
//        adminPrivRaw: ByteArray,
//        memberPubsRaw: List<ByteArray>,
//        groupId: ByteArray
//    ): ByteArray = withContext(Dispatchers.Default) {
//        // 1) Obtén el algoritmo ECDH para la curva P‑256
//        val ecdh = provider.get(ECDH)
//        val curveP256 = EC.Curve.P256
//
//        // 2) Decodifica la clave privada RAW (32 bytes)
//        val privKey = ecdh.privateKeyDecoder(curveP256)
//            .decodeFromByteArray(EC.PrivateKey.Format.RAW, adminPrivRaw)
//
//        // 4) Decodifica la clave pública RAW (65 bytes, SEC1 uncompressed)
//        val pubKey = ecdh.publicKeyDecoder(curveP256)
//            .decodeFromByteArray(EC.PublicKey.Format.RAW, memberPubsRaw.first())
//
//        val sharedSecretRaw: ByteArray = ecdh.privateKeyDecoder(curveP256)
//            .decodeFromByteArray(
//                EC.PrivateKey.Format.RAW,
//                privKey.encodeToByteArray(
//                    format = EC.PrivateKey.Format.RAW))
//            .sharedSecretGenerator().generateSharedSecretToByteArray(pubKey)
//
//        val tamaniaBInario: BinarySize = 32.bytes
//
//        val hkdf = provider.get(HKDF)
//        hkdf.secretDerivation(
//            SHA256,
//            tamaniaBInario,
//            null,
//            groupId
//        ).deriveSecretToByteArray(sharedSecretRaw)
//    }
//
//    /**
//     * 3) Cifra el mensaje con AES‑GCM y la clave de grupo.
//     *    – El nonce es de 12 bytes (96 bits).
//     */
//    suspend fun encryptMessage(
//        groupKey: ByteArray,
//        plaintext: ByteArray,
//        groupId: ByteArray
//    ): ByteArray = withContext(Dispatchers.Default) {
//        val aes = provider.get(AES.GCM)
//        val key = aes.keyDecoder()
//            .decodeFromByteArray(AES.Key.Format.RAW, groupKey)
//
//        // Cifra el mensaje, incluyendo groupId como AAD
//        key.cipher().encrypt(
//            plaintext       = plaintext,
//            associatedData  = groupId
//        )
//    }
//
//    /**
//     * 4) Desencripta el mensaje con AES‑GCM y la clave de grupo.
//     *    – El nonce es de 12 bytes (96 bits).
//     */
//    suspend fun decryptMessage(
//        groupKey: ByteArray,
//        encryptedFull: ByteArray,
//        groupId: ByteArray
//    ): ByteArray = withContext(Dispatchers.Default) {
//        val aes = provider.get(AES.GCM)
//        val key = aes.keyDecoder()
//            .decodeFromByteArray(AES.Key.Format.RAW, groupKey)
//
//        // Desencripta pasando el mismo groupId como AAD
//        key.cipher().decrypt(
//            ciphertext      = encryptedFull,
//            associatedData  = groupId
//        )
//    }
//}