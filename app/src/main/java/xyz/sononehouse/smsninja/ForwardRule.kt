package xyz.sononehouse.smsninja

import android.util.Log
import java.util.regex.Pattern

class ForwardRule(val senderPattern: String, val bodyPattern: String, forwardUrl: String) {
    fun isMatch(sender: String, body: String): Boolean {
        val p = Pattern.compile(senderPattern)
        val m = p.matcher(sender)
        if (m.find()) {
            Log.d("``ForwardRule", "m.group()=" + m.group())
            return true
        }
        return false
    }
}