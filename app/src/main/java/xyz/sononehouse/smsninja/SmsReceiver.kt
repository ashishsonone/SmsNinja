package xyz.sononehouse.smsninja

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class SmsReceiver() : BroadcastReceiver(), CoroutineScope by MainScope() {
    companion object {
        val LOGTAG = "``SmsReceiver"
        val SMS_BUNDLE = "pdus"
    }

    init {
        Log.d(LOGTAG, "constructor called")
    }

    fun extractSms(intent: Intent) : SmsEntity {
        val intentExtras = intent.extras!!

        val sms = intentExtras.get(SMS_BUNDLE) as Array<Any>

        var body = ""
        if (sms != null && sms.size >= 1) {
            for (i in sms.indices) {
                var smsMessage: SmsMessage
                smsMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val format: String = intentExtras.getString("format") !!
                    SmsMessage.createFromPdu(sms[i] as ByteArray, format)
                } else {
                    SmsMessage.createFromPdu(sms[i] as ByteArray)
                }
                //Log.d(LOGTAG, "smsMessage.getIndexOnIcc()=" + smsMessage.getIndexOnIcc());
                body += smsMessage.messageBody
            }
        }

        val smsMessage: SmsMessage

        smsMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val format: String = intentExtras.getString("format")!!
            SmsMessage.createFromPdu(sms.get(0) as ByteArray, format)
        } else {
            SmsMessage.createFromPdu(sms.get(0) as ByteArray)
        }

        val dateSent = smsMessage.timestampMillis
        val address = smsMessage.originatingAddress

        Log.d(LOGTAG, "ts=${Date(dateSent)}, address=${address}, body=${body}")

        return SmsEntity(dateSent, address!!, body)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent!!.action

        Log.d(LOGTAG, "action=$action")

        val stats = QuickStore.getStats()
        stats.received += 1
        QuickStore.storeStats(stats)

        // todo extract senderId, body, timestamp & create SmsEntity
        try {
            val sms = extractSms(intent)

            val rule = QuickStore.getForwardRule()
            if (rule == null) {
                Log.d(LOGTAG, "No rule present")
            }
            else {
                launch {
                    rule.invoke(sms.date, sms.sender, sms.body)
                }
            }
            val stats = QuickStore.getStats()
            stats.processSuccess += 1
            QuickStore.storeStats(stats)
        }
        catch (e: Exception) {
            val stats = QuickStore.getStats()
            stats.processError += 1
            QuickStore.storeStats(stats)

            e.printStackTrace()
            Log.d(LOGTAG, "error in onReceive(): ${e.message}")
        }

    }
}