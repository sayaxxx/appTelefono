package com.example.customdialer

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ChangeCodeActivity : AppCompatActivity() {

    // Mismo valor por defecto usado en MainActivity mientras el usuario
    // no haya guardado nunca un código propio.
    private val DEFAULT_SECRET_CODE_VIEW = "*1234#"

    // Solo se permiten dígitos, * y #, igual que en el teclado real.
    private val VALID_CODE_REGEX = Regex("^[0-9*#]+$")

    private lateinit var currentCodeText: TextView
    private lateinit var newCodeInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_code)

        currentCodeText = findViewById(R.id.currentCodeText)
        newCodeInput = findViewById(R.id.newCodeInput)

        refreshCurrentCode()

        findViewById<Button>(R.id.saveCodeButton).setOnClickListener {
            val newCode = newCodeInput.text.toString().trim()

            if (newCode.isEmpty() || !VALID_CODE_REGEX.matches(newCode)) {
                Toast.makeText(this, R.string.change_code_invalid, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            SecretCodeStore.setViewCode(this, newCode)
            Toast.makeText(this, R.string.change_code_saved, Toast.LENGTH_SHORT).show()
            newCodeInput.text.clear()
            refreshCurrentCode()
        }
    }

    private fun refreshCurrentCode() {
        currentCodeText.text = SecretCodeStore.getViewCode(this, DEFAULT_SECRET_CODE_VIEW)
    }
}
