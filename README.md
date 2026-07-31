# 📱 appTelefono — Marcador Android con imagen secreta

Aplicación de Android (Kotlin) que replica la interfaz clásica del
marcador telefónico (lista de contactos "Sugeridos" + teclado numérico +
botón "Llamar") y añade funciones ocultas activadas mediante **códigos
secretos**, al estilo de los códigos USSD (`*#06#`, etc.), pero
personalizados por el propio usuario.

## ⚠️ Descargo de responsabilidad

Este proyecto se comparte **con fines educativos y de aprendizaje** sobre
desarrollo de apps Android (interfaces de usuario, `Intent`s,
`SharedPreferences`, almacenamiento interno, `ActivityResultContracts`,
etc.).

El autor **no se hace responsable del mal uso** que terceros puedan darle
a esta aplicación (por ejemplo, instalarla en el dispositivo de otra
persona sin su consentimiento, usarla para ocultar contenido de forma
engañosa, vigilancia no consentida, o cualquier otro uso que vulnere la
privacidad, la ley o los términos de servicio aplicables). El uso de este
código, así como del APK generado a partir de él, es responsabilidad
exclusiva de quien lo descarga, compila, instala, modifica y/o
distribuye. Úsalo de forma ética, legal y siempre con el consentimiento
de las personas involucradas.

## ✨ Funcionalidad

La app se ve y se comporta como un marcador de teléfono normal:

- Muestra una lista de contactos "Sugeridos".
- Tiene un teclado numérico (0-9, `*`, `#`) y un botón "Llamar".
- Al marcar un número normal y pulsar "Llamar", abre el marcador real del
  sistema para completar la llamada (`Intent.ACTION_DIAL`).

Pero además, **mientras se escribe en el teclado**, la app detecta en
tiempo real si lo marcado coincide con alguno de estos códigos secretos
(no hace falta pulsar "Llamar"):

| Código por defecto | Acción |
|---|---|
| `*1234#` | Muestra a pantalla completa la imagen secreta guardada |
| `*9999#` | Abre una pantalla para elegir/cambiar esa imagen desde la galería |
| `*8888#` | Abre una pantalla para cambiar el código que muestra la imagen (`*1234#`) por otro a elección |

- La imagen elegida por el usuario se guarda en el **almacenamiento
  interno y privado de la app** (no requiere permisos de almacenamiento y
  no es visible para otras apps).
- El código que abre la imagen se guarda en `SharedPreferences` y persiste
  aunque se cierre o reinicie la app.
- Para salir de la pantalla de la imagen se usa el botón **Atrás** del
  sistema (no tiene botón de cierre visible).

## 📦 Como probar la app

Este repositorio contiene el **APK ya compilado**, para obtener
un archivo `.apk` instalable, solo debes descargar el archivo appTelefono.APK

### Cómo instalar correctamente

   - Descarga el archivo `appTelefono.apk`
   - Al abrirlo, si es la primera vez que instala una app fuera de Google
     Play, Android pedirá permitir la instalación desde esa fuente
     ("Instalar apps desconocidas") — solo hay que aceptarlo una vez para
     ese origen.
   - Confirmar la instalación.

## 📂 Estructura del proyecto

```
CustomDialer/
├── app/
│   └── src/main/
│       ├── java/com/example/customdialer/
│       │   ├── MainActivity.kt          # Pantalla principal (marcador)
│       │   ├── SecretActivity.kt        # Muestra la imagen secreta
│       │   ├── UploadImageActivity.kt   # Elegir/cambiar la imagen
│       │   ├── ChangeCodeActivity.kt    # Cambiar el código de la imagen
│       │   ├── SecretImageStore.kt      # Guarda la imagen en filesDir
│       │   ├── SecretCodeStore.kt       # Guarda el código en SharedPreferences
│       │   └── Contact.kt               # Modelo de contacto
│       └── res/                         # Layouts, colores, strings, drawables
├── build.gradle.kts
├── settings.gradle.kts
├── .gitignore
└── README.md
```

## 🚀 Cómo compilar y ejecutar en modo desarrollo

1. Clona el repositorio:
   ```bash
   git clone https://github.com/sayaxxx/appTelefono
   ```
2. Ábrelo en **Android Studio** (`File → Open`). Se generará el Gradle
   Wrapper automáticamente la primera vez.
3. Conecta un dispositivo Android (con depuración USB) o usa un emulador.
4. Pulsa **Run ▶**.

> `minSdk = 24` (Android 7.0 en adelante).

## ⚙️ Personalización

### Cambiar los códigos secretos por defecto

En `MainActivity.kt`:

```kotlin
private val DEFAULT_SECRET_CODE_VIEW = "*1234#"   // código que abre la imagen
private val SECRET_CODE_UPLOAD = "*9999#"         // código para cambiar la imagen
private val SECRET_CODE_CHANGE_CODE = "*8888#"    // código para cambiar el código de arriba
```

### Cambiar la imagen que se muestra

- **Desde la app** (recomendado): marca `*9999#` y elige una foto de la
  galería.
- **Desde el código**: reemplaza `app/src/main/res/drawable/secret_image.xml`
  (el placeholder de ejemplo) por tu propia imagen `secret_image.png`.
  Esta se usa solo mientras el usuario no haya elegido ninguna imagen
  personalizada desde la app.

### Cambiar el código que abre la imagen

- **Desde la app**: marca `*8888#`, escribe el nuevo código y guárdalo.
- **Desde el código**: cambia `DEFAULT_SECRET_CODE_VIEW` en `MainActivity.kt`
  (solo aplica antes de que el usuario guarde uno propio desde la app).

### Cambiar los contactos "Sugeridos"

También en `MainActivity.kt`:

```kotlin
private val contacts = listOf(
    Contact("Madre", "+573208475211", "M", "#B39DDB"),
    Contact("Numero Unico de Emergencias", "123", "N", "#EF9A9A"),
    Contact("Papá", "+57314215748", "P", "#F9C74F"),
    Contact("Hermana", "+573157154521", "H", "#CBB6F5")
)
```

## 🛠️ Detalles técnicos

- El botón **Llamar** solo se usa para llamadas normales; abre el
  marcador real del sistema con `Intent(ACTION_DIAL)`, por lo que la app
  **no requiere el permiso peligroso `CALL_PHONE`**.
- La app **no reemplaza** al marcador predeterminado del sistema (eso
  requeriría declararla como app de teléfono predeterminada ante Android,
  con permisos y roles adicionales como `RoleManager.ROLE_DIALER`).
- La detección de imagen usa `ActivityResultContracts.GetContent()`
  (Storage Access Framework), por lo que **no pide permisos de
  almacenamiento en tiempo de ejecución**.

## 📄 Licencia

Este proyecto se distribuye tal cual, sin garantías de ningún tipo. Puedes
usarlo, modificarlo, compilarlo y compartirlo libremente, siempre
respetando el [descargo de responsabilidad](#️-descargo-de-responsabilidad)
indicado arriba.
