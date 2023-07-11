package com.example.chatapp
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
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
    private lateinit var weight:EditText
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var radioGroupGender:RadioGroup
    private lateinit var selectedGender:String
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
        weight=findViewById(R.id.Sign_up_Weight)
        signUpButton = findViewById(R.id.Signup_Button)
        radioGroupGender=findViewById(R.id.radioGroupGender)


        radioGroupGender.setOnCheckedChangeListener{ _,checkedId->
            selectedGender=when(checkedId){
                R.id.radioButtonMale->"male"
                R.id.radioButtonFemale->"female"
                else->""
            }

        }
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
            val weightTxt = weight.text.toString()
            val gender=selectedGender.toString()
            if (passwordTxt.length < 6) {
                password.error = "Password should be at least 6 characters long"
            } else {
                password.error = null
                appSignUp(nameTxt, ageTxt, emailTxt, heightTxt, passwordTxt,weightTxt,gender)
            }
        }
    }

    private fun appSignUp(
        name: String,
        age: String,
        email: String,
        height: String,
        password: String,
        weight : String,
        gender :String
    ) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // User signed up successfully, store the user data in Firestore
                    val currentUser = mAuth.currentUser
                    val userId = currentUser?.uid
                    val user = hashMapOf(
                        "name" to name,
                        "age" to age,
                        "email" to email,
                        "height" to height,
                        "weight" to weight,
                        "gender" to gender
                    )

                    if (userId != null) {
                        firestore.collection("users")
                            .document(userId)
                            .set(user)
                            .addOnSuccessListener {
                                val intent = Intent(this, HomeActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                // Failed to store data
                                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
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