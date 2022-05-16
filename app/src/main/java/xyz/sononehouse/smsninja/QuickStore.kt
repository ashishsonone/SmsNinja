package xyz.sononehouse.smsninja

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson

object QuickStore {
    val LOGTAG = "``QuickStore"

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

    fun getForwardRule() : ForwardRule? {
        val gson = Gson()
        val storedValue = this.get("forwardRule", null)
        Log.d(LOGTAG, "getForwardRule ${storedValue}")
        if (storedValue.isNullOrEmpty()) {
            return null
        }

        val rule = gson.fromJson(storedValue!!, ForwardRule::class.java)
        return rule
    }

    fun storeForwardRule(rule: ForwardRule) {
        val gson = Gson()
        val jsonValue = gson.toJson(rule)
        Log.d(LOGTAG, "storeForwardRule ${jsonValue}")
        this.set("forwardRule", jsonValue)
    }
}