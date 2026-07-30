# CustomDialer — Marcador con código secreto

App de Android (Kotlin) que replica la interfaz del marcador de teléfono
(lista de "Sugeridos" + teclado numérico + botón Llamar) y añade dos
funciones ocultas activadas por códigos secretos:

- Un código que **muestra** la imagen secreta a pantalla completa.
- Un código que abre una pantalla para **elegir/cambiar** esa imagen desde
  la galería del celular, sin tocar código ni reinstalar la app.

## Cómo abrir y compilar el proyecto

1. Abre **Android Studio** (versión reciente, con Kotlin y Android SDK 34
   instalados).
2. `File → Open...` y selecciona la carpeta `CustomDialer` (la que contiene
   este README).
3. Android Studio generará automáticamente el Gradle Wrapper la primera vez
   que abras el proyecto (si te lo pide, acepta "Sync Now" / "Trust Project").
4. Conecta un celular Android (con depuración USB activada) o usa un
   emulador, y pulsa **Run ▶**.

> minSdk = 24 (Android 7.0) — compatible con la gran mayoría de equipos.

## Cómo cambiar los códigos secretos

Abre el archivo:

```
app/src/main/java/com/example/customdialer/MainActivity.kt
```

y modifica estas dos líneas:

```kotlin
// Código que muestra la imagen guardada
private val SECRET_CODE_VIEW = "*1234#"

// Código que abre la pantalla para elegir/cambiar la imagen
private val SECRET_CODE_UPLOAD = "*9999#"
```

Puedes usar cualquier combinación de números, `*` y `#` (ej. `*#06#`,
`0000#`, `*7*7`). En cuanto el usuario marca exactamente uno de esos
códigos en el teclado, la app salta de inmediato a la pantalla
correspondiente, sin esperar a que pulse "Llamar".

## Cómo personalizar la imagen (sin tocar código)

1. En el teclado, marca el código de personalización (por defecto
   `*9999#`).
2. Se abre la pantalla **"Personalizar imagen secreta"**.
3. Pulsa **"Elegir imagen de la galería"** y selecciona cualquier foto de
   tu celular (usa el selector nativo de Android, así que no pide
   permisos especiales de almacenamiento).
4. La imagen se guarda automáticamente en el almacenamiento privado de la
   app. Desde ese momento, cada vez que marques el código de vista
   (`*1234#`) se mostrará esa foto.
5. Puedes repetir el proceso las veces que quieras para cambiarla, o
   pulsar **"Quitar imagen personalizada"** para volver a la imagen de
   ejemplo por defecto.

La imagen elegida se guarda de forma privada dentro del almacenamiento
interno de la app (`filesDir`), por lo que no es visible para otras
aplicaciones ni requiere permisos de almacenamiento en tiempo de
ejecución.

## Cómo cambiar la imagen de ejemplo por defecto (opcional, vía código)

Si en cambio quieres definir una imagen fija desde el propio proyecto
(sin que el usuario tenga que elegirla desde la app), reemplaza:

```
app/src/main/res/drawable/secret_image.xml
```

por tu imagen `secret_image.png` (borrando antes el `.xml` de ejemplo).
Esa imagen se usa como respaldo mientras el usuario no haya elegido
ninguna imagen personalizada con el código `*9999#`.

## Cómo funciona (resumen técnico)

- `MainActivity.kt`: pantalla principal, arma la lista de contactos
  "Sugeridos", dibuja el teclado numérico y captura cada dígito pulsado en
  un `StringBuilder`.
- Cada vez que se pulsa una tecla, la app compara el texto marcado con
  `SECRET_CODE_VIEW` y `SECRET_CODE_UPLOAD`. **En cuanto coincide
  exactamente con alguno, salta de inmediato** a la pantalla
  correspondiente (no hace falta pulsar "Llamar").
- El botón **Llamar** ahora solo sirve para marcar números normales: abre
  el marcador real del sistema mediante un `Intent(ACTION_DIAL)`.
  (Se usa `ACTION_DIAL` en vez de `ACTION_CALL` para no requerir el
  permiso peligroso `CALL_PHONE`; si prefieres que la app llame
  directamente sin pasos extra, puedo ayudarte a añadir ese permiso y el
  manejo de `ACTION_CALL`.)
- `UploadImageActivity.kt`: pantalla con un botón que abre el selector de
  imágenes del sistema (`ActivityResultContracts.GetContent`). La imagen
  elegida se copia al almacenamiento interno de la app mediante
  `SecretImageStore`.
- `SecretImageStore.kt`: guarda/lee/borra la imagen personalizada en
  `filesDir` (almacenamiento privado de la app, no requiere permisos).
- `SecretActivity.kt`: muestra la imagen personalizada guardada por el
  usuario (si existe) o, si no, la imagen de ejemplo
  `res/drawable/secret_image`, a pantalla completa. **No tiene botón de
  cierre** — para salir, el usuario usa el botón Atrás del sistema
  (comportamiento nativo de Android, cierra la actividad
  automáticamente).

## Personalizar los contactos "Sugeridos"

También en `MainActivity.kt`, edita la lista `contacts` con los nombres,
teléfonos, inicial y color de avatar que quieras:

```kotlin
private val contacts = listOf(
    Contact("Madre", "+573208532766", "M", "#B39DDB"),
    Contact("Numero Unico de Emergencias", "123", "N", "#EF9A9A"),
    Contact("Papá", "+573142143478", "P", "#F9C74F"),
    Contact("Hermana", "+573166373633", "H", "#CBB6F5")
)
```

## Nota importante

Esta app **no reemplaza** al marcador predeterminado del sistema (eso
requeriría declararla como "app de teléfono predeterminada" ante Android,
con permisos adicionales). Funciona como una app independiente con su
propia interfaz de marcado; para llamadas reales delega en el marcador del
sistema mediante `ACTION_DIAL`. Si quieres que sea instalable como marcador
predeterminado, puedo ayudarte a añadir los permisos e intent-filters
necesarios (`android.intent.action.DIAL`, `CALL_PRIVILEGED`, rol
`RoleManager.ROLE_DIALER`, etc.).
