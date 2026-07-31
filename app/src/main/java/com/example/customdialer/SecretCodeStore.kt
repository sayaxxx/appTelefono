package com.example.customdialer

import android.content.Context

/**
 * Guarda y recupera el código secreto que abre la imagen (SECRET_CODE_VIEW),
 * para que el usuario pueda cambiarlo desde la propia app sin tocar código.
 * Se guarda en SharedPreferences privadas de la app.
 */
object SecretCodeStore {

    private const val PREFS_NAME = "secret_dialer_prefs"
    private const val KEY_VIEW_CODE = "view_code"

    /**
     * Devuelve el código guardado por el usuario, o [defaultCode] si todavía
     * no lo ha cambiado nunca.
     */
    fun getViewCode(context: Context, defaultCode: String): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_VIEW_CODE, defaultCode) ?: defaultCode
    }

    /** Guarda un nuevo código para abrir la imagen. */
    fun setViewCode(context: Context, newCode: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_VIEW_CODE, newCode).apply()
    }
}
