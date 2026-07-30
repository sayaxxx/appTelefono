package com.example.customdialer

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SecretActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secret)

        val imageView = findViewById<ImageView>(R.id.secretImageView)

        // Si el usuario ya personalizó la imagen (código *9999#), se muestra esa.
        // Si no, se queda con la imagen de ejemplo definida en el layout
        // (res/drawable/secret_image).
        val customBitmap = SecretImageStore.loadImage(this)
        if (customBitmap != null) {
            imageView.setImageBitmap(customBitmap)
        }

        // No hay botón de cierre: el usuario sale con el botón Atrás del sistema,
        // que por defecto ya cierra esta actividad (comportamiento nativo de Android).
    }
}
