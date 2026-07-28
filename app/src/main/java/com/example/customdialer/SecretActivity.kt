package com.example.customdialer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SecretActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secret)

        // La imagen mostrada viene de: res/drawable/secret_image
        // Reemplaza ese archivo por tu propia imagen (PNG/JPG/vector) con el mismo nombre
        // "secret_image", o cambia la referencia en activity_secret.xml.

        // No hay botón de cierre: el usuario sale con el botón Atrás del sistema,
        // que por defecto ya cierra esta actividad (comportamiento nativo de Android).
    }
}
