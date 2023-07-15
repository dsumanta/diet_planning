package com.example.chatapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.example.chatapp.profile.ProfileActivity
import com.example.chatapp.todayStatus.TodayStatus
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var btnOpenDrawer: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        btnOpenDrawer = findViewById(R.id.btn_open_drawer)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val headerView = navView.getHeaderView(0)
        val mobileName = headerView.findViewById<TextView>(R.id.mobile_name)
        val mobileEmail = headerView.findViewById<TextView>(R.id.mobile_email)



        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                     val intent=Intent(this,ProfileActivity::class.java)
                     startActivity(intent)
                   // Toast.makeText(applicationContext, "Clicked Profile", Toast.LENGTH_SHORT).show()
                }
                R.id.logout -> {
                    FirebaseAuth.getInstance().signOut()
                    // Redirect to login screen
                    val intent = Intent(this, LogIn::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.today_status ->{
                   val intent=Intent(this,TodayStatus::class.java)
                   startActivity(intent)
                    //Toast.makeText(applicationContext, "Open it on by clicking on see your home button in home screen", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }

        btnOpenDrawer.setOnClickListener {
            drawerLayout.openDrawer(navView)
            val userId= FirebaseAuth.getInstance().currentUser?.uid
            FirebaseFirestore.setLoggingEnabled(true)

            val db=FirebaseFirestore.getInstance()
            val ref = userId?.let { db.collection("users").document(it) }


            ref?.get()?.addOnSuccessListener {
                if(it!=null){
                    val name= it.data?.get("name").toString()
                    val email= it.data?.get("email").toString()
                    mobileName.text = name
                    mobileEmail.text = email
                }
            }
        }


        // Default Recepie Fragment


        val RecepieFragment = RecepieSuggetion()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container, RecepieFragment, "RecepieFragment") // The third parameter should be a string
        fragmentTransaction.commit()

        // Replacement of Calorie Fragment
        val caloriCounter: Button = findViewById(R.id.Calorie_counterBtn)
        caloriCounter.setOnClickListener {

            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            if (currentFragment !is CalorieCounter) {
                val fragment = CalorieCounter()
                with(supportFragmentManager.beginTransaction()) {
                    replace(R.id.fragment_container, fragment)
                    addToBackStack(null)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    commit()
                }
            }
        }


        // Replacement of Calorie Intake fragment

        val caloriIntake=findViewById<Button>(R.id.calorieIntake_Btn)
        caloriIntake.setOnClickListener{

            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            if (currentFragment !is CalorieIntake) {
                val fragment = CalorieIntake()
                with(supportFragmentManager.beginTransaction()) {
                    replace(R.id.fragment_container, fragment)
                    addToBackStack(null)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    commit()
                }
            }
        }
        // Replacement of water Intake fragment

        val waterIntake=findViewById<Button>(R.id.waterIntake_Btn)
        waterIntake.setOnClickListener{
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            if (currentFragment !is waterIntake) {
                val fragment = waterIntake()
                with(supportFragmentManager.beginTransaction()) {
                    replace(R.id.fragment_container, fragment)
                    addToBackStack(null)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    commit()
                }
            }
        }
    }




}