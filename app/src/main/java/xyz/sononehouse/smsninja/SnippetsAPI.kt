package xyz.sononehouse.smsninja

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SnippetsAPI {
    @POST("/api/snippets/store")
    fun store(@Body body: StoreRequest): Call<SuccessResponse>

    @GET("/api/snippets/get")
    fun get(@Query("key") key: String): Call<String>
}