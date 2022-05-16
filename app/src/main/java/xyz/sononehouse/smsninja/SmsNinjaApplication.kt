package xyz.sononehouse.smsninja

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import org.json.JSONObject

class SmsNinjaApplication  : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null
        fun getAppContext(): Context {
            return context !!
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}