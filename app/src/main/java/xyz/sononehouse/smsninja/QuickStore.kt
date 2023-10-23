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

    fun remove(key: String) {
        with (sharedPrefs.edit()) {
            remove(key)
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

    fun deleteForwardRule() {
        Log.d(LOGTAG, "deleteForwardRule invoked")
        remove("forwardRule")
    }

    fun getStats() : Stats {
        val gson = Gson()
        val storedValue = this.get("stats", null)
        Log.d(LOGTAG, "getStats ${storedValue}")
        if (storedValue.isNullOrEmpty()) {
            return Stats(0, 0, 0, 0)
        }

        val stats = gson.fromJson(storedValue!!, Stats::class.java)
        return stats
    }

    fun storeStats(stats: Stats) {
        val gson = Gson()
        val jsonValue = gson.toJson(stats)
        Log.d(LOGTAG, "storeStats ${jsonValue}")
        this.set("stats", jsonValue)
    }
}