package com.example.chatapp.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    private lateinit var Name:TextView
    private lateinit var Age:TextView
    private lateinit var Gender:TextView
    private lateinit var Weight:TextView
    private lateinit var height:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        Name=findViewById(R.id.name_user_profile)
        Age=findViewById(R.id.age_user_profile)
        Gender=findViewById(R.id.gender_user_profile)
        Weight=findViewById(R.id.weight_user_profile)
        height=findViewById(R.id.height_user_profile)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val userId = mAuth.currentUser?.uid
        FirebaseFirestore.setLoggingEnabled(true)

        val ref= userId?.let { db.collection("users").document(it) }
        ref?.get()?.addOnSuccessListener {
            if(it!=null){
                Name.text= it.data?.get("name") as String
                Age.text= it.data?.get("age") as String
                Gender.text= it.data?.get("gender") as String
                Weight.text= it.data?.get("weight") as String
                height.text= it.data?.get("height") as String

            }
        }

    }
}