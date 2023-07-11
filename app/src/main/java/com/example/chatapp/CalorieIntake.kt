package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import com.example.chatapp.todayStatus.TodayStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.callbackFlow

class CalorieIntake : Fragment() {


    private lateinit var mySpinner: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var userId: String // Add a variable to hold the current user ID
    private lateinit var mealOption: String // Add a variable to hold the selected meal option
    private var caloriList = arrayListOf<Item>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calorie_intake, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mySpinner = view.findViewById(R.id.mySpinner)

        val options = arrayOf("Breakfast", "Lunch", "Dinner")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mySpinner.adapter = adapter

        // Set the user ID here
        val mAuth: String? = FirebaseAuth.getInstance().currentUser?.uid

        if (mAuth != null) {
            userId = mAuth
        }

        // Set the meal option listener
        mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mealOption = options[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Fetching API
        recyclerView = view.findViewById(R.id.recyclerView_searchFood)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val searchView = view.findViewById<SearchView>(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Perform search operation when the user submits the search query
                makeApiRequest(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Perform search operation as the user types
                makeApiRequest(newText)

                return true
            }
        })

        val showStatusButton = view.findViewById<Button>(R.id.see_your_current_status)
        showStatusButton.setOnClickListener {
            val intent = Intent(requireContext(), TodayStatus::class.java)
            intent.putExtra("myListKey", caloriList)
            Log.d("CaloriIntake","Calorilist $caloriList")
            startActivity(intent)
        }

    }

    private fun makeApiRequest(query1: String) {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://api.calorieninjas.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NutritionAPI::class.java)

        val x: String =
            "puri and tomato and chicken and paneer and mutton and rice and dal and ghee and fish and egg and snack"
        val query = query1.ifEmpty { x }
        val retrofitData = retrofitBuilder.getNutritionData(
            apiKey = "0Zx4zgOMOjNMW738YsRZwA==wDtQoihL7cmw9kSV",
            query = query
        )

        retrofitData.enqueue(object : Callback<khadya_Item> {
            override fun onResponse(call: Call<khadya_Item>, response: Response<khadya_Item>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        val productList = responseBody.items

                        // Create an instance of the ItemAdapter with the current user ID
                        val myAdapter = ItemAdapter(productList)

                        recyclerView.adapter = myAdapter
                        myAdapter.setItemClickListner(object : ItemAdapter.ItemClickListner {
                            override fun OnItemClick(position: Int) {
                                val item = productList[position]
                                // addFoodItemToFirestore(item, mealOption, userId)
                                Toast.makeText(requireContext(), "${item.name} with ${item.calories} is added", Toast.LENGTH_SHORT).show()
                                caloriList.add(item)

                            }
                        })
                    } else {
                        Log.d("CaloriIntake", "Response body is null")
                    }
                } else {
                    Log.d("CaloriIntake", "API Call Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<khadya_Item>, t: Throwable) {
                // If API call fails
                Log.d("Main Activity", "onFailure: " + t.message)
            }
        })

    }

//    private fun addFoodItemToFirestore(foodItem: Item, mealOption: String, userId: String) {
//        val firestore = FirebaseFirestore.getInstance()
//
//        // Get the current date
//        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
//
//        // Create a reference to the user's document
//        val userDocRef = firestore.collection("users").document(userId)
//
//        // Create a reference to the date document under the user's document
//
//        val dateDocRef = userDocRef.collection("dates").document(currentDate)
//
//        // Create a reference to the meal option document under the date document
//        val mealOptionDocRef = dateDocRef.collection("mealOptions").document(mealOption)
//
//        // Create a data object to represent the food item
//        val foodData = hashMapOf(
//            "name" to foodItem.name,
//            "calories" to foodItem.calories
//        )
//
//        // Add the food item to the respective meal option
//        mealOptionDocRef.collection("foods")
//            .add(foodData)
//            .addOnSuccessListener { documentReference ->
//                Log.d("CalorieIntake", "Food item added to Firestore successfully. Document ID: ${documentReference.id}")
//                Toast.makeText(requireContext(),"Calorie with food added successfully",Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener { e ->
//                Log.e("CalorieIntake", "Error adding food item to Firestore.", e)
//            }
//    }

}

private fun Intent.putStringArrayListExtra(s: String, caloriList: ArrayList<Item>) {

}
