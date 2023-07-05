package com.example.chatapp
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private fun Any.enqueue(callback: Callback<FoodItem?>) {

}

class RecepieSuggetion : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recepie_suggetion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView_point)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIinterfaceFood::class.java)

        val retrofitData = retrofitBuilder.searchRecipesByNutrients(
            apiKey = "122b65e914df4116b8e55c2aa9c93fe7",
            number = 100,
            maxCalories = 800
        )
        retrofitData.enqueue(object : Callback<FoodItem?> {

            override fun onResponse(call: Call<FoodItem?>, response: Response<FoodItem?>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        val productList = responseBody ?: emptyList()
                        Log.d("RecepieSuggetion", "Product List Size: ${productList.size}")

                        val myAdapter = FoodAdapter(requireContext(), productList)
                        recyclerView.adapter = myAdapter
                    } else {
                        Log.d("RecepieSuggetion", "Response body is null")
                    }
                } else {
                    Log.d("RecepieSuggetion", "API Call Failed: ${response.message()}")
                }
            }



            override fun onFailure(call: Call<FoodItem?>, t: Throwable) {
                // if api call fails
                Log.d("Main Activity ", "onFailure: " + t.message)
            }
        })


    }

}




