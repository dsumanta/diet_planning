package com.example.chatapp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIinterfaceFood {
    @GET("recipes/findByNutrients")
    fun searchRecipesByNutrients(
        @Query("apiKey") apiKey: String,
        @Query("number") number: Int,
        @Query("maxCalories") maxCalories: Int
    ): Call<FoodItem>
}
