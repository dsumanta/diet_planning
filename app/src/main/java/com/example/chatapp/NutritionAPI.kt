package com.example.chatapp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NutritionAPI {
    @GET("nutrition")
    fun getNutritionData(
        @Header("X-Api-Key") apiKey: String,
        @Query("query") query: String
    ): Call<khadya_Item>
}
