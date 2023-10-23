package xyz.sononehouse.smsninja

import android.util.Log
import java.util.*
import java.util.regex.Pattern

class ForwardRule(val ruleName: String, var senderPattern: String, var bodyPattern: String, val locationKey: String, val base64SecretKey: String) {
    suspend fun invoke(sender: String, body: String): Boolean {
        Log.d("``ForwardRule", "Running rule $ruleName")
        Log.d("``ForwardRule", "Running rule sender=$sender, body=$body")

        if (checkInList(senderPattern, sender) && checkIsMatch(bodyPattern, body)) {
            Log.d("``ForwardRule", "match success, forwarding...")
            val plainText = "${Date()} \n $sender \n $body"
            val encryptedPayload = EncryptionUtils.genBase64EncryptedPayload(plainText, base64SecretKey)
            Coordinator().storeKV(locationKey, encryptedPayload, QuickStore.get("clientId") !!)

            val stats = QuickStore.getStats()
            stats.uploadSuccess += 1
            QuickStore.storeStats(stats)

            return true
        }
        else {
            Log.d("``ForwardRule", "Not matched")
        }

        return false
    }

    fun checkInList(tokenList: String, text: String) : Boolean {
        val tokens = tokenList.split(',').map{it.trim()}.filter { it.length >= 6 }
        for (t in tokens) {
            if (text.contains(t, ignoreCase = true)) return true
        }
        return false
    }

    fun checkIsMatch(pattern: String, text: String) : Boolean {
        val p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)
        val m = p.matcher(text)
        if (m.find()) {
            Log.d("``ForwardRule", "m.group()=" + m.group())
            if (m.group().length >= 3) {
                // atleast should match 3 characters, otherwise it's risky that it matches by mistake
                return true
            }
        }
        return false
    }
}