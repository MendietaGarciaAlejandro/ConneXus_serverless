package org.connexuss.project.encriptacion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import dev.whyoleg.cryptography.algorithms.ECDH
import dev.whyoleg.cryptography.algorithms.ECDSA
import dev.whyoleg.cryptography.algorithms.HMAC
import dev.whyoleg.cryptography.algorithms.PBKDF2
import dev.whyoleg.cryptography.algorithms.SHA512
import dev.whyoleg.cryptography.random.CryptographyRandom
import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.connexuss.project.comunicacion.Post
import org.connexuss.project.interfaces.DefaultTopBar
import org.connexuss.project.interfaces.LimitaTamanioAncho
import org.connexuss.project.persistencia.SettingsState
import org.connexuss.project.supabase.ClavesUsuarioRepositorio
import org.connexuss.project.supabase.SupabaseUsuariosRepositorio

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
        claveAES = generaClaveAES()
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
 * Deriva un hash PBKDF2-HMAC-SHA512 de la contraseña.
 *
 * @param password Texto de la contraseña.
 * @param saltBytes Lista de bytes aleatorios (salt) de al menos 16 bytes.
 * @param iterations Nº de iteraciones (por defecto 100 000).
 * @return El resultado como ByteString (derivado de 32 bytes).
 */
suspend fun derivePasswordEmailHash(
    password: String,
    saltBytes: ByteArray,
    iterations: Int = 100_000
): ByteArray {
    val provider = CryptographyProvider.Default
    val derivation = provider
        .get(PBKDF2)
        .secretDerivation(
            digest     = SHA512,
            iterations = iterations,
            outputSize = 32.bytes,
            salt       = saltBytes
        )
    return derivation.deriveSecretBlocking(password.toByteArray()).toByteArray()
}

@Serializable
data class CredencialesUsuario(
    val idUsuario: String,
    val saltHex: String,   // 32 hex chars = 16 bytes
    val hashHex: String,   // 64 hex chars = 32 bytes
    val emailHex:  String   // email hash (determinístico)
)

suspend fun migrarCredencialesExistentes(repoUsuario: SupabaseUsuariosRepositorio) {
    // 1. Obtenemos todos los usuarios
    val usuarios = repoUsuario.getUsuarios()  // asume Flow<List<Usuario>> o similar
        .firstOrNull() ?: emptyList()

    for (usuario in usuarios) {
        val plainPwd = usuario.contrasennia
        val plainEmail = usuario.correo
        if (plainPwd.isNullOrBlank() || plainEmail.isNullOrBlank()) {
            // O bien saltarnos usuarios sin password, o marcar para reset de contraseña
            continue
        }

        // 1) mismo salt+hash para cada usuario
        val saltBytes      = CryptographyRandom.nextBytes(16)
        val saltHex        = saltBytes.toHex()

        // 2) derivar
        val pwdHashHex     = derivePasswordEmailHash(plainPwd,   saltBytes).toHex()
        val emailHashHex   = derivePasswordEmailHash(plainEmail, saltBytes).toHex()

        val creds = CredencialesUsuario(
            idUsuario = usuario.idUnico,
            saltHex   = saltHex,
            hashHex   = pwdHashHex,
            emailHex  = emailHashHex
        )
        repoUsuario.addCredenciales(creds)
    }

    // 4. (Opcional) Vaciar o eliminar contrasennia de usuario
    //repoUsuario.clearAllPasswords()
}

@Composable
fun PantallaMigracionCredenciales(
    navController: NavHostController,
    userRepo: SupabaseUsuariosRepositorio = SupabaseUsuariosRepositorio()
) {
    var credentials by remember { mutableStateOf<List<CredencialesUsuario>>(emptyList()) }
    var isMigrated by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    MaterialTheme {
        Scaffold(
            topBar = {
                DefaultTopBar(
                    title = "Migrar Credenciales",
                    navController = navController,
                    showBackButton = true,
                    irParaAtras = true
                )
            }
        ) { padding ->
            LimitaTamanioAncho { modifier ->
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                loading = true
                                error = null
                                try {
                                    // Ejecuta migración
                                    migrarCredencialesExistentes(userRepo)
                                    isMigrated = true
                                    // Carga resultado
                                    credentials =
                                        userRepo.getCredenciales().firstOrNull() ?: emptyList()
                                } catch (e: Exception) {
                                    error = e.message
                                } finally {
                                    loading = false
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (loading) "Migrando..." else "Iniciar migración")
                    }

                    if (error != null) {
                        Text("Error: $error", color = MaterialTheme.colors.error)
                    }

                    if (isMigrated) {
                        Text("Credenciales migradas:", style = MaterialTheme.typography.h6)
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            items(credentials) { cred ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Text("Usuario: ${cred.idUsuario}")
                                    Text("Salt: ${cred.saltHex}")
                                    Text("Hash: ${cred.hashHex}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Encriptacion Posts

/**
 * Datos cifrados de un post ECIES + AES-GCM:
 * - postId: ID de post original
 * - ephemeralPubHex: clave pública efímera DER→hex
 * - ivHex: IV de 12 bytes hex
 * - ciphertextHex: ciphertext+tag hex
 */
@Serializable
data class EncryptedPost(
    val postId: String,
    val ephemeralPubHex: String,
    val ivHex: String,
    val ciphertextHex: String
)

@Serializable
data class ClavesUsuario(
    val idUsuario: String,
    val pubKeyMsgHex: String,
    val pubKeyPostHex: String
)

/**
 * Cifra un Post con ECIES (ECDH + AES-GCM):
 */
suspend fun encryptECIESPost(
    post: Post,
    receiverPubKey: ECDH.PublicKey
): EncryptedPost {
    val provider    = CryptographyProvider.Default
    val ecdh        = provider.get(ECDH)
    val ephemeralKP = ecdh.keyPairGenerator(EC.Curve.P256).generateKey()

    // Derivar secreto compartido
    val sharedBytes: ByteArray = ephemeralKP.privateKey
        .sharedSecretGenerator()
        .generateSharedSecretToByteArray(receiverPubKey)

    // Interpreta el secreto directo como clave RAW AES-256
    val aesKey = provider.get(AES.GCM)
        .keyDecoder()
        .decodeFromByteArray(AES.Key.Format.RAW, sharedBytes)

    // Cifrar con AES-GCM (auto-genera IV y lo antepone)
    val encryptedAll = aesKey.cipher()
        .encrypt(post.content.encodeToByteArray())

    // Extraer IV y ciphertext+tag
    val ivBytes = encryptedAll.sliceArray(0 until 12)
    val ctBytes = encryptedAll.sliceArray(12 until encryptedAll.size)

    return EncryptedPost(
        postId          = post.idPost,
        ephemeralPubHex = ephemeralKP.publicKey
            .encodeToByteArray(EC.PublicKey.Format.DER)
            .toHex(),
        ivHex           = ivBytes.toHex(),
        ciphertextHex   = ctBytes.toHex()
    )
}

/**
 * Desencripta un EncryptedPost generado por encryptECIESPost.
 */
suspend fun decryptECIESPost(
    encrypted: EncryptedPost,
    receiverPrivKey: ECDH.PrivateKey
): String {
    val provider    = CryptographyProvider.Default
    val ecdh        = provider.get(ECDH)

    // Reconstruir clave pública efímera
    val ephemeralPK = ecdh.publicKeyDecoder(EC.Curve.P256)
        .decodeFromByteArray(EC.PublicKey.Format.DER, encrypted.ephemeralPubHex.hexToByteArray())

    // Derivar secreto compartido con la misma API antigua
    val sharedBytes = receiverPrivKey
        .sharedSecretGenerator()
        .generateSharedSecretToByteArray(ephemeralPK)

    // Construir clave AES para desencriptar
    val aesKey = provider.get(AES.GCM)
        .keyDecoder()
        .decodeFromByteArray(AES.Key.Format.RAW, sharedBytes)

    // Reconstruir datos cifrados (IV + ciphertext+tag)
    val ivBytes = encrypted.ivHex.hexToByteArray()
    val ctBytes = encrypted.ciphertextHex.hexToByteArray()
    val encryptedAll = ivBytes + ctBytes

    // Desencriptar y retornar texto
    val plainBytes = aesKey.cipher().decrypt(ciphertext = encryptedAll)
    return plainBytes.decodeToString()
}

@Composable
fun PantallaClavesUsuario(
    navController: NavHostController,
    settingsState: SettingsState,
    clavesRepo: ClavesUsuarioRepositorio,
    userId: String
) {
    val scope = rememberCoroutineScope()

    // Colectamos los Flows como estados Compose
    val pubClaves by clavesRepo
        .getClavesByUserId(userId)
        .collectAsState(initial = null)
    val privMsgKey by settingsState
        .privMsgKeyFlow
        .collectAsState(initial = null)
    val privPostKey by settingsState
        .privPostKeyFlow
        .collectAsState(initial = null)

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Sección de claves públicas
        Text("Claves Públicas:", style = MaterialTheme.typography.h6)
        val msgPubText = pubClaves?.pubKeyMsgHex ?: "(no configurada)"
        val postPubText = pubClaves?.pubKeyPostHex ?: "(no configurada)"
        Text("Mensajes: $msgPubText")
        Text("Posts:    $postPubText")

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            scope.launch {
                // TODO: Generar nuevo par de claves y llamar a clavesRepo.upsertClaves(...)
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Regenerar claves")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sección de claves privadas
        Text("Claves Privadas (guardadas localmente):", style = MaterialTheme.typography.h6)
        val msgPrivText = privMsgKey ?: "(no existe)"
        val postPrivText = privPostKey ?: "(no existe)"
        Text("Mensajes: $msgPrivText")
        Text("Posts:    $postPrivText")
    }
}