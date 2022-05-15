package xyz.sononehouse.smsninja

import android.content.Context
import android.content.SharedPreferences

class QuickStore(context: Context) {
    val SP_FILE = "smsninja"
    val sharedPrefs: SharedPreferences

    init {
        sharedPrefs = context.getSharedPreferences(SP_FILE, Context.MODE_PRIVATE)
    }

    fun get(key: String): String? {
        return this.sharedPrefs.getString(key, null)
    }

    fun set(key: String, value: String) {
        with (sharedPrefs.edit()) {
            putString(key, value)
            apply()
        }
    }
}