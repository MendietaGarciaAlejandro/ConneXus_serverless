# ConneXus 🔗

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)  
[![Kotlin](https://img.shields.io/badge/Kotlin-1.8-blue.svg)]  
[![Supabase](https://img.shields.io/badge/Supabase-PostgreSQL-green.svg)]

**ConneXus** es una aplicación multiplataforma de mensajería y foro anónimo que combina privacidad y libertad de expresión en un mismo entorno.

---

## 📖 Tabla de contenidos

1. [Descripción](#-descripción)  
2. [Alcance y limitaciones](#-alcance-y-limitaciones)  
3. [Tecnologías](#-tecnologías)  
4. [Arquitectura](#-arquitectura)  
5. [Instalación](#-instalación)  
6. [Uso](#-uso)  
7. [Pruebas](#-pruebas)  
8. [Contribuciones](#-contribuciones)  
9. [Licencia](#-licencia)  
10. [Contacto](#-contacto)

---

## ✨ Descripción

El proyecto **ConneXus** persigue revolucionar la comunicación digital mediante:

- **Chat privado con cifrado de extremo a extremo (E2EE)** — garantiza que solo emisor y receptor puedan leer los mensajes.  
- **Foro público anónimo** — permite a los usuarios debatir bajo seudónimos, sin exponer su identidad real.

Esta doble vertiente cubre tanto la necesidad de privacidad absoluta como la de libertad de participación abierta.

---

## 🎯 Alcance y limitaciones

### Alcance

- **Experiencia multiplataforma**: UI y lógica compartida entre Android, iOS, escritorio y web via Kotlin Multiplatform y Jetpack Compose Multiplatform.  
- **Mensajería E2EE**: cifrado de extremo a extremo para chats privados.  
- **Foro anónimo**: hilos de discusión con seudónimos públicos.  
- **Backend escalable**: migración de Firebase a Supabase para gestión de Auth, Realtime y SQL.  
- **Material Design**: interfaz moderna, accesible y consistente.

### Limitaciones

- **Dependencia de Internet**: requiere conexión estable para chat en tiempo real y foro.  
- **Moderación inicial**: el sistema de control de abusos anónimos puede necesitar ajustes tras despliegue.  
- **Seguridad en dispositivos**: vulnerabilidades del SO quedan fuera de nuestro control directo.

---

## ⚙️ Tecnologías

- **Lenguaje y UI**  
  - Kotlin Multiplatform  
  - Jetpack Compose Multiplatform  
- **Backend**  
  - Supabase (PostgreSQL, Auth, Realtime)  
  - Cloud Functions para lógica E2EE y moderación  
- **Cifrado y seguridad**  
  - Librerías Krypto y E2EE  
- **Estilo**  
  - Material Design

---

## 🏗️ Arquitectura
1. El cliente se conecta vía WebSocket para chat y con REST para operaciones CRUD.  
2. Supabase gestiona autenticación, suscripciones en tiempo real y almacenamiento.  
3. Cloud Functions aplican cifrado E2EE y reglas de moderación.

---

## 🛠️ Instalación

1. Clona el repositorio:
   \`\`\`bash
   git clone https://github.com/MendietaGarciaAlejandro/ConneXus_serverless.git
   cd ConneXus_serverless
   \`\`\`
2. Configura variables de entorno en \`.env\`:
   \`\`\`bash
   cp env.example .env
   # Edita SUPABASE_URL y SUPABASE_KEY
   \`\`\`
3. Compila el proyecto multiplataforma:
   \`\`\`bash
   ./gradlew assemble
   \`\`\`

---

## ▶️ Uso

1. Levanta tu instancia de Supabase (local o remoto).  
2. Ejecuta la app:
   - **Escritorio**: \`./gradlew runDesktop\`  
   - **Android**: \`./gradlew runAndroid\`  
   - **iOS**: \`./gradlew runIos\`  

3. Regístrate o inicia sesión, únete a un chat privado o al foro anónimo, ¡y empieza a conversar!

---

## 🧪 Pruebas

- **Unitarias y UI** con Kotlin Test y Compose Testing.  
- Invoca funciones cloud localmente:
  \`\`\`bash
  supabase functions serve
  \`\`\`
- Simula eventos con mocks JSON en \`mocks/\`.

---

## 🤝 Contribuciones

¡Bienvenidas! Para aportar:

1. Haz fork y crea un branch:
   \`\`\`bash
   git checkout -b feature/nombre-funcionalidad
   \`\`\`
2. Añade tu código y tests.  
3. Envía un Pull Request detallando tu propuesta.

---

## 📄 Licencia

Este proyecto está bajo la **MIT License**. Consulta [LICENSE](LICENSE) para más información.

---

## ✉️ Contacto

**Alejandro Mendieta García**  
✉️ alexmengar@outlook.es  
GitHub: [MendietaGarciaAlejandro]([https://github.com/MendietaGarciaAlejandro/ConneXus_serverless](https://github.com/MendietaGarciaAlejandro))
