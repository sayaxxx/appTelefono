package com.example.customdialer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

/**
 * Guarda y recupera la imagen secreta personalizada por el usuario.
 * Se guarda en el almacenamiento interno y privado de la app
 * (filesDir), así que no requiere permisos de almacenamiento y
 * no es visible para otras apps.
 */
object SecretImageStore {

    private const val FILE_NAME = "secret_image_custom.jpg"

    private fun imageFile(context: Context): File =
        File(context.filesDir, FILE_NAME)

    /** true si el usuario ya subió una imagen personalizada */
    fun hasCustomImage(context: Context): Boolean =
        imageFile(context).exists()

    /** Copia la imagen elegida por el usuario (Uri de la galería) al almacenamiento interno */
    fun saveImage(context: Context, sourceUri: Uri): Boolean {
        return try {
            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                FileOutputStream(imageFile(context)).use { output ->
                    input.copyTo(output)
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    /** Devuelve el Bitmap guardado, o null si el usuario aún no personalizó ninguna imagen */
    fun loadImage(context: Context): Bitmap? {
        val file = imageFile(context)
        if (!file.exists()) return null
        return BitmapFactory.decodeFile(file.absolutePath)
    }

    /** Elimina la imagen personalizada, volviendo a la imagen de ejemplo por defecto */
    fun clearImage(context: Context) {
        val file = imageFile(context)
        if (file.exists()) file.delete()
    }
}
