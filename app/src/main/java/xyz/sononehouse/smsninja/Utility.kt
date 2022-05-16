package xyz.sononehouse.smsninja

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log

object Utility {
    val LOGTAG = "``Utility"
    fun storeIntoClipboard(text: String) {
        Log.d(LOGTAG, "Copying ${text}")
        val clipboardManager = SmsNinjaApplication.getAppContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText("smsninja.clip", text))
    }
}