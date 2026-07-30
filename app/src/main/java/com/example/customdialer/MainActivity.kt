package com.example.customdialer

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // ---------------------------------------------------------------
    // CÓDIGOS SECRETOS.
    // ---------------------------------------------------------------

    // Código que muestra la imagen guardada
    private val SECRET_CODE_VIEW = "*#06#"

    // Código que abre la pantalla para elegir/cambiar la imagen
    private val SECRET_CODE_UPLOAD = "*9999#"

    private lateinit var dialedNumber: TextView
    private lateinit var backspaceButton: ImageButton
    private lateinit var contactsCard: LinearLayout

    private val currentInput = StringBuilder()

    // Contactos "Sugeridos"
    private val contacts = listOf(
        Contact("Madre", "+573208475211", "M", "#B39DDB"),
        Contact("Numero Unico de Emergencias", "123", "N", "#EF9A9A"),
        Contact("Papá", "+57314215748", "P", "#F9C74F"),
        Contact("Hermana", "+573157154521", "H", "#CBB6F5")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dialedNumber = findViewById(R.id.dialedNumber)
        backspaceButton = findViewById(R.id.backspaceButton)
        contactsCard = findViewById(R.id.contactsCard)

        buildContactsList()
        setupDialpad()
        setupCallButton()
        setupBackspace()
    }

    private fun buildContactsList() {
        val inflater = LayoutInflater.from(this)
        contacts.forEachIndexed { index, contact ->
            val itemView = inflater.inflate(R.layout.item_contact, contactsCard, false)

            val avatar = itemView.findViewById<TextView>(R.id.avatarInitial)
            val name = itemView.findViewById<TextView>(R.id.contactName)
            val phone = itemView.findViewById<TextView>(R.id.contactPhone)
            val callIcon = itemView.findViewById<View>(R.id.callIcon)

            avatar.text = contact.initial
            avatar.background.setColorFilter(Color.parseColor(contact.avatarColorHex), PorterDuff.Mode.SRC_IN)
            name.text = contact.name
            phone.text = "${getString(R.string.telefono_label)} ${contact.phone}"

            val callAction: (View) -> Unit = { dialNumber(contact.phone) }
            itemView.setOnClickListener(callAction)
            callIcon.setOnClickListener(callAction)

            contactsCard.addView(itemView)

            // Línea divisora entre contactos (excepto el último)
            if (index < contacts.size - 1) {
                val divider = View(this)
                divider.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 1
                ).apply { leftMargin = 16; rightMargin = 16 }
                divider.setBackgroundColor(Color.parseColor("#E0E0E5"))
                contactsCard.addView(divider)
            }
        }
    }

    // Mapa: id de la tecla -> Pair(dígito mostrado, letras mostradas)
    private fun setupDialpad() {
        val keys = listOf(
            Triple(R.id.key1, "1", ""),
            Triple(R.id.key2, "2", "ABC"),
            Triple(R.id.key3, "3", "DEF"),
            Triple(R.id.key4, "4", "GHI"),
            Triple(R.id.key5, "5", "JKL"),
            Triple(R.id.key6, "6", "MNO"),
            Triple(R.id.key7, "7", "PQRS"),
            Triple(R.id.key8, "8", "TUV"),
            Triple(R.id.key9, "9", "WXYZ"),
            Triple(R.id.keyStar, "*", ""),
            Triple(R.id.key0, "0", "+"),
            Triple(R.id.keyHash, "#", "")
        )

        keys.forEach { (containerId, digit, letters) ->
            val container = findViewById<View>(containerId)
            container.findViewById<TextView>(R.id.digitText).text = digit
            container.findViewById<TextView>(R.id.lettersText).text = letters

            container.setOnClickListener { appendDigit(digit) }

            // Mantener presionado el 0 para insertar "+"
            if (containerId == R.id.key0) {
                container.setOnLongClickListener {
                    appendDigit("+")
                    true
                }
            }
        }
    }

    private fun appendDigit(digit: String) {
        currentInput.append(digit)
        refreshDisplay()
        checkSecretCode()
    }

    private fun checkSecretCode() {
        val entered = currentInput.toString()
        when (entered) {
            SECRET_CODE_VIEW -> {
                startActivity(Intent(this, SecretActivity::class.java))
                currentInput.clear()
                refreshDisplay()
            }
            SECRET_CODE_UPLOAD -> {
                startActivity(Intent(this, UploadImageActivity::class.java))
                currentInput.clear()
                refreshDisplay()
            }
        }
    }

    private fun refreshDisplay() {
        dialedNumber.text = currentInput.toString()
        backspaceButton.visibility = if (currentInput.isEmpty()) View.INVISIBLE else View.VISIBLE
    }

    private fun setupBackspace() {
        backspaceButton.setOnClickListener {
            if (currentInput.isNotEmpty()) {
                currentInput.deleteCharAt(currentInput.length - 1)
                refreshDisplay()
            }
        }
        backspaceButton.setOnLongClickListener {
            currentInput.clear()
            refreshDisplay()
            true
        }
    }

    private fun setupCallButton() {
        val callButton = findViewById<View>(R.id.callButton)
        callButton.setOnClickListener {
            val entered = currentInput.toString()
            if (entered.isEmpty()) return@setOnClickListener
            dialNumber(entered)
        }
    }

    private fun dialNumber(number: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        startActivity(intent)
    }
}
