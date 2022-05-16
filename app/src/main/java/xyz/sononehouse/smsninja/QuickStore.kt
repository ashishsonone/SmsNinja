package xyz.sononehouse.smsninja

import android.content.Context
import android.content.SharedPreferences

object QuickStore {
    val SP_FILE = "smsninja"
    val sharedPrefs: SharedPreferences


    init {
        sharedPrefs = SmsNinjaApplication.getAppContext().getSharedPreferences(SP_FILE, Context.MODE_PRIVATE)
    }

    fun get(key: String) : String? {
        return get(key, null)
    }

    fun get(key: String, default: String?): String? {
        val x = this.sharedPrefs.getString(key, default)
        if (x.isNullOrEmpty()) {
            return default
        }
        else {
            return x
        }
    }

    fun set(key: String, value: String) {
        with (sharedPrefs.edit()) {
            putString(key, value)
            apply()
        }
    }
}