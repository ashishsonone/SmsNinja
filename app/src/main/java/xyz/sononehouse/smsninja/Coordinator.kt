package xyz.sononehouse.smsninja

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


class Coordinator {
    companion object {
        val retrofit = Retrofit.Builder()
            //.baseUrl("http://10.0.2.2:10000")
            .baseUrl("https://iot.tunnel.sononehouse.xyz")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: SnippetsAPI = retrofit.create(SnippetsAPI::class.java)
    }

    suspend fun generateKey(clientId: String)  = withContext(Dispatchers.IO) {
        val genKeyDeferred = async {
            service.generateKey(GenKeyRequest(clientId)).execute()
        }

        val genKeyResponse = genKeyDeferred.await()
        val genKeyResponseObj = genKeyResponse.body()
        Log.d("``Coordinator", "generateKey: clientId=${clientId}, response locationKey=${genKeyResponseObj?.key}, code=" + genKeyResponse.code())

        return@withContext genKeyResponseObj!!.key
    }

    suspend fun storeKV(key: String, value: String, clientId: String) = withContext(Dispatchers.IO) {
        val storeDeferred = async {
            service.store(StoreRequest(key, value, clientId)).execute()
        }

        val storeResponse = storeDeferred.await()
        Log.d("``Coordinator", "store: key=${key}, response body=${storeResponse.body()?.message}, code=" + storeResponse.code())
    }

    suspend fun logEvent(event: String, data: Any) = withContext(Dispatchers.IO) {
        val storeDeferred = async {
            val device = Utility.getDeviceName() + QuickStore.get("clientId")!!
            service.sendEvent(EventRequest(device, "sms-ninja", event, data)).execute()
        }

        val storeResponse = storeDeferred.await()
        Log.d("``Coordinator", "logEvent: event=${event}, response body=${storeResponse.body()?.message}, code=" + storeResponse.code())
    }



    suspend fun getK(key: String) = withContext(Dispatchers.IO) {
        val getDeferred = async {
            service.get(key).execute()
        }
        val getResponse = getDeferred.await()

        Log.d("``Coordinator", "get: Result body=${getResponse.body()}, code=" + getResponse.code())
        return@withContext getResponse.body()
    }
}