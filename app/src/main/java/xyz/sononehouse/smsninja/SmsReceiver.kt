package xyz.sononehouse.smsninja

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class SmsReceiver() : BroadcastReceiver() {
    companion object {
        val LOGTAG = "``SmsReceiver"
    }

    init {
        Log.d(LOGTAG, "constructor called")
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(LOGTAG, intent?.extras.toString())
    }
}