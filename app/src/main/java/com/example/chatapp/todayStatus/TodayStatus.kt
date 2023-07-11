package com.example.chatapp.todayStatus

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.Item
import com.example.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TodayStatus : AppCompatActivity() {
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var gridLayout: GridLayout
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var femaleCalculation: Double = 0.0
    private var maleCalculation: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today_status)

        // Initialize TextViews
        val intent = getIntent()
        val caloriList = intent.getSerializableExtra("myListKey") as ArrayList<Item>?
        var Calorie: Double = 0.0
        Log.d("TodayStatus", "caloriList $caloriList")
        if (caloriList != null) {
            gridLayout = findViewById(R.id.grid_layout)
            for (item in caloriList) {
                val textView = TextView(this)
                val st: String = item.name.toString() + "           ${item.calories}kcl"
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
                textView.text = st
                Calorie += item.calories
                // Customize the appearance and attributes of the TextView as needed
                gridLayout.addView(textView)
            }
        }
        val formattedNumber = String.format("%.2f", Calorie)

        val totalcalori = findViewById<TextView>(R.id.total_calorie)
        totalcalori.text = formattedNumber + "kcl"


        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val userId = mAuth.currentUser?.uid
        FirebaseFirestore.setLoggingEnabled(true)

        val ref = userId?.let { db.collection("users").document(it) }
        ref?.get()?.addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot != null) {
                val Chage = documentSnapshot.data?.get("age") as? String
                var Chweight = documentSnapshot.data?.get("weight") as? String
                val Chheight = documentSnapshot.data?.get("height") as? String
                val gender = documentSnapshot.data?.get("gender") as? String
                val age = Chage?.toDouble()
                val weight = Chweight?.toDouble()
                val height = Chheight?.toDouble()

                val age2 = age?.toInt()
                val reslutText=findViewById<TextView>(R.id.result_view)
                if (gender == "male" && age != null && weight != null && height != null) {
                    maleCalculation = 66.5 + (13.75 * weight) + (5.003 * height) - (6.75 * age)
                } else if (gender == "female" && age != null && weight != null && height != null) {
                    femaleCalculation = 65.51 + (9.563 * weight) + (1.850 * height) - (6.75 * age)
                }

                Log.d("caloriecounter", "Male Calculation: $maleCalculation")
                Log.d("Caloriecouner", "Female Calculation: $femaleCalculation")


                if (gender == "male") {

                    if(maleCalculation<=Calorie){
                        val diff=Calorie-maleCalculation
                        val formattedNumber = String.format("%.2f", diff)

                        val st:String="Congratulation! You have achieved your daily recommended calorie and you have taken " +
                                "more $formattedNumber kcl calorie than required calorie for toady.Have a nice day"
                        reslutText.text=st
                    }
                    else{
                        val diff=maleCalculation-Calorie
                        val formattedNumber = String.format("%.2f", diff)
                        val st:String="Sorry ! you have not reached your daily required calorie you need to consume more "+
                                "and you need to consume $formattedNumber kcl to achieve your today recommended calorie "
                        reslutText.text=st
                    }

                } else {
                    reslutText.text = femaleCalculation.toString()
                }

            }
            // Get the current date
            //        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            //        Log.d("TodayStatus", "Retrieving meal options from Firestore $currentDate")
            //
            //        // Create a reference to the user's document
            //        val userId = FirebaseAuth.getInstance().currentUser?.uid // Replace with the actual user ID
            //        if (userId != null) {
            //            val userDocRef = firestore.collection("users").document(userId)
            //
            //            Log.d("TodayStatus", "userId userDocRef $userDocRef")
            //
            //            // Create a reference to the date document under the user's document
            //            val ref = userDocRef.collection("dates")
            //            ref.get().addOnSuccessListener { snapshot ->
            //                Log.d("TodayStatus", "Dates $ref ${snapshot.documents} isempty:${snapshot.isEmpty}")
            //            }.addOnFailureListener { exception ->
            //                Log.d("TodayStatus", "Exception $exception")
            //            }
            //
            //            val dateDocRef = userDocRef.collection("dates").document(currentDate)
            //            Log.d("TodayStatus", "Retrieving meal options from Firestore $dateDocRef")
            //
            //            // Retrieve the meal options from Firestore
            //            val mealOptionRef = dateDocRef.collection("mealOptions")
            //            mealOptionRef.get().addOnSuccessListener { snapshot ->
            //                Log.d("TodayStatus", "mealOption ${snapshot.documents}")
            //                val list = ArrayList<String>()
            //                for (document in snapshot) {
            //                    val mealType = document.toString()
            //                    list.add(mealType)
            //                    val foodRef = mealOptionRef.document(mealType)
            //                }
            //                Log.d("todayStatus", "food are $list")
            //            }
            //        }

        }
    }
}

