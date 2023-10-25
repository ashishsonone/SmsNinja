package xyz.sononehouse.smsninja

import android.R.attr.capitalize
import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.util.Log


object Utility {
    val KEY_STATS = "stats"

    val LOGTAG = "``Utility"
    fun storeIntoClipboard(text: String) {
        Log.d(LOGTAG, "Copying ${text}")
        val clipboardManager = SmsNinjaApplication.getAppContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText("smsninja.clip", text))
    }

    fun getClipboardValue(): String? {
        val clipboardManager = SmsNinjaApplication.getAppContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val primaryClip = clipboardManager.primaryClip
        if (clipboardManager.hasPrimaryClip() && clipboardManager.primaryClipDescription!!.hasMimeType(MIMETYPE_TEXT_PLAIN)) {
            val item = clipboardManager.primaryClip?.getItemAt(0)
            return item?.text.toString()
        }
        return null
    }

    fun getDeviceName(): String? {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return "[$manufacturer:$model]"
    }
}