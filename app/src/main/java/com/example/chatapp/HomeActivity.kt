package com.example.chatapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
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

                    Toast.makeText(applicationContext, "Clicked Profile", Toast.LENGTH_SHORT).show()
                }
                R.id.logout -> Toast.makeText(applicationContext, "Clicked Logout", Toast.LENGTH_SHORT).show()
                R.id.today_status -> Toast.makeText(applicationContext, "Clicked Today's Status", Toast.LENGTH_SHORT).show()
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
        val RecepieFragment = RecepieSuggetion()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container, RecepieFragment, "RecepieFragment") // The third parameter should be a string
        fragmentTransaction.commit()
    }


}