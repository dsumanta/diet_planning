package com.example.chatapp
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.content.Intent
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LogIn : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var loginBttn: Button
    private var db=FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        loginBttn = findViewById(R.id.button)
        val email: EditText = findViewById(R.id.email)
        val password: EditText = findViewById(R.id.login_Password)
        val createAccount: TextView = findViewById(R.id.create_account)
        FirebaseFirestore.setLoggingEnabled(true)



        email.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (!isValidEmail(email)) {
                    email.error = "Enter a valid Email"
                } else {
                    email.error = null
                }
            }
        }

        createAccount.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        loginBttn.setOnClickListener {
            val emailText = email.text.toString()
            val passwordText = password.text.toString()
            appLogin(emailText, passwordText)
        }
    }



    private fun isValidEmail(editText: EditText): Boolean {
        val email = editText.text.toString().trim()
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
    private fun appLogin(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    val exception = task.exception
                    if (exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Login failed: ${exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

}
