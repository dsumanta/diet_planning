package com.example.chatapp

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.properties.Delegates

class CalorieCounter : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var EditAge: TextView
    private lateinit var EditHeight: TextView
    private lateinit var EditWeight: TextView
    private lateinit var finalcalori: TextView
    private lateinit var showBtn: Button
    private var femaleCalculation: Double = 0.0
    private var maleCalculation: Double = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        finalcalori = view.findViewById(R.id.finalCalorie_calorie_counter)
        EditWeight = view.findViewById(R.id.weight_calorie_counter)
        EditHeight =view.findViewById(R.id.height_calorie_counter)
        EditAge = view.findViewById(R.id.age_calorie_counter)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val userId = mAuth.currentUser?.uid
        FirebaseFirestore.setLoggingEnabled(true)
        Log.d("caloriecounter", "User ID: $userId")

        val ref = userId?.let { db.collection("users").document(it) }
        ref?.get()?.addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot != null) {
                val Chage = documentSnapshot.data?.get("age") as? String
                var Chweight = documentSnapshot.data?.get("weight") as? String
                val Chheight = documentSnapshot.data?.get("height") as? String
                val gender = documentSnapshot.data?.get("gender") as? String
               val age= Chage?.toDouble()
                val weight= Chweight?.toDouble()
                val height= Chheight?.toDouble()

                Log.d("caloriecounter", "Retrieved user data: $age, $weight, $height, $gender")

                Log.d("caloriecounter", "Height from Firestore: $height")
                EditHeight.text = height?.toString() ?: "N/A"
                val age2= age?.toInt()
                EditAge.text= age2.toString()
                EditWeight.text=weight.toString()
                if (gender == "male" && age != null && weight != null && height != null) {
                    maleCalculation = 66.5 + (13.75 * weight) + (5.003 * height) - (6.75 * age)
                } else if (gender == "female" && age != null && weight != null && height != null) {
                    femaleCalculation = 655.1 + (9.563 * weight) + (1.850 * height) - (6.75 * age)
                }

                Log.d("caloriecounter", "Male Calculation: $maleCalculation")
                Log.d("Caloriecouner", "Female Calculation: $femaleCalculation")


                    if (gender == "male") {
                        finalcalori.text = maleCalculation.toString()
                    } else {
                        finalcalori.text = femaleCalculation.toString()
                    }

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calorie_counter, container, false)
    }
}
