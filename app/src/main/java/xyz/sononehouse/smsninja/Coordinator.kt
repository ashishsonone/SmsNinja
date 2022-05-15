package xyz.sononehouse.smsninja

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Coordinator {
    companion object {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:10000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: SnippetsAPI = retrofit.create(SnippetsAPI::class.java)
    }

    suspend fun storeKV(key: String, value: String) = withContext(Dispatchers.IO) {
        val storeDeferred = async {
            service.store("k1", "v1").execute()
        }

        val storeResponse = storeDeferred.await()
        Log.d("``FirstFragment", "Result body=${storeResponse.body()?.success}, code=" + storeResponse.code())
    }
}