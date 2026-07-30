package com.example.customdialer

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class UploadImageActivity : AppCompatActivity() {

    private lateinit var previewImage: ImageView
    private lateinit var previewPlaceholder: TextView
    private lateinit var removeButton: Button

    // Abre el selector de imágenes del sistema (galería / archivos).
    // No requiere pedir permisos en tiempo de ejecución porque usa el
    // selector del sistema (Storage Access Framework).
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val saved = SecretImageStore.saveImage(this, uri)
            if (saved) {
                Toast.makeText(this, R.string.upload_saved, Toast.LENGTH_SHORT).show()
                refreshPreview()
            } else {
                Toast.makeText(this, R.string.upload_error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        previewImage = findViewById(R.id.previewImage)
        previewPlaceholder = findViewById(R.id.previewPlaceholder)
        removeButton = findViewById(R.id.removeImageButton)

        findViewById<Button>(R.id.pickImageButton).setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        removeButton.setOnClickListener {
            SecretImageStore.clearImage(this)
            Toast.makeText(this, R.string.upload_removed, Toast.LENGTH_SHORT).show()
            refreshPreview()
        }

        refreshPreview()
    }

    private fun refreshPreview() {
        val bitmap = SecretImageStore.loadImage(this)
        if (bitmap != null) {
            previewImage.setImageBitmap(bitmap)
            previewImage.visibility = View.VISIBLE
            previewPlaceholder.visibility = View.GONE
            removeButton.visibility = View.VISIBLE
        } else {
            previewImage.visibility = View.GONE
            previewPlaceholder.visibility = View.VISIBLE
            removeButton.visibility = View.GONE
        }
    }
}
