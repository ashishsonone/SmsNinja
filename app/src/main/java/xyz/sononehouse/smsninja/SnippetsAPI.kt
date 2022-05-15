package xyz.sononehouse.smsninja

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SnippetsAPI {
    @GET("/api/snippets/store")
    fun store(@Query("key") key: String, @Query("value") value: String): Call<SuccessResponse>

    @GET("/api/snippets/get")
    fun get(@Query("key") key: String): Call<String>
}