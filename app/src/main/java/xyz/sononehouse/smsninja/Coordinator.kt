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
            .baseUrl("http://sononehouse.giize.com:8666")
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

    suspend fun getK(key: String) = withContext(Dispatchers.IO) {
        val getDeferred = async {
            service.get(key).execute()
        }
        val getResponse = getDeferred.await()

        Log.d("``Coordinator", "get: Result body=${getResponse.body()}, code=" + getResponse.code())
        return@withContext getResponse.body()
    }
}