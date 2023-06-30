package com.example.chatapp
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.chatapp.HomeActivity
import com.example.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore

class SignUp : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var age: EditText
    private lateinit var email: EditText
    private lateinit var height: EditText
    private lateinit var password: EditText
    private lateinit var signUpButton: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private fun isValidEmail(editText: EditText): Boolean {
        val email = editText.text.toString().trim()
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        name = findViewById(R.id.Sign_up_name)
        age = findViewById(R.id.Sign_up_age)
        email = findViewById(R.id.Sign_up_mail)
        height = findViewById(R.id.Sign_up_Height)
        password = findViewById(R.id.Sign_up_Password)
        signUpButton = findViewById(R.id.Signup_Button)
        mAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        email.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (!isValidEmail(email)) {
                    email.error = "Enter a valid Email"
                } else {
                    email.error = null
                }
            }
        }

        signUpButton.setOnClickListener {
            val nameTxt = name.text.toString()
            val ageTxt = age.text.toString()
            val emailTxt = email.text.toString()
            val heightTxt = height.text.toString()
            val passwordTxt = password.text.toString()

            if (passwordTxt.length < 6) {
                password.error = "Password should be at least 6 characters long"
            } else {
                password.error = null
                appSignUp(nameTxt, ageTxt, emailTxt, heightTxt, passwordTxt)
            }
        }
    }

    private fun appSignUp(
        name: String,
        age: String,
        email: String,
        height: String,
        password: String
    ) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // User signed up successfully, store the user data in Firestore
                    val user = hashMapOf(
                        "name" to name,
                        "age" to age,
                        "email" to email,
                        "height" to height
                    )

                    firestore.collection("users")
                        .document(email)
                        .set(user)
                        .addOnSuccessListener {
                            // Data stored successfully
                            val intent = Intent(this, HomeActivity::class.java)
                            intent.putExtra("name", name)
                            intent.putExtra("email", email)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            // Failed to store data
                            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        // Account already exists with the entered email
                        Toast.makeText(this, "An account with this email already exists", Toast.LENGTH_SHORT).show()
                    } else {
                        // Other error occurred
                        Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}
