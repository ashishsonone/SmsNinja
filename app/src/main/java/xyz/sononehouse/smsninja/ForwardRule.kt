package xyz.sononehouse.smsninja

import android.util.Log
import java.util.*
import java.util.regex.Pattern

class ForwardRule(val ruleName: String, val senderPattern: String, val bodyPattern: String, val locationKey: String, val base64SecretKey: String) {
    suspend fun invoke(sender: String, body: String): Boolean {
        Log.d("``ForwardRule", "Running rule $ruleName")
        Log.d("``ForwardRule", "Running rule sender=$sender, body=$body")

        if (checkIsMatch(senderPattern, sender) && checkIsMatch(bodyPattern, body)) {
            Log.d("``ForwardRule", "match success, forwarding...")
            val plainText = "${Date()} \n $sender \n $body"
            val encryptedPayload = EncryptionUtils.genBase64EncryptedPayload(plainText, base64SecretKey)
            Coordinator().storeKV(locationKey, encryptedPayload, QuickStore.get("clientId") !!)

            return true
        }
        else {
            Log.d("``ForwardRule", "Not matched")
        }

        return false
    }

    fun checkIsMatch(pattern: String, text: String) : Boolean {
        val p = Pattern.compile(pattern)
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