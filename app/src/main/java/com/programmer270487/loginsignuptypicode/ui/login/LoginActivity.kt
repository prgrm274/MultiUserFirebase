package com.programmer270487.loginsignuptypicode.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.programmer270487.loginsignuptypicode.databinding.ActivityMainBinding
import com.programmer270487.loginsignuptypicode.ui.home.HomeActivity
import com.programmer270487.loginsignuptypicode.ui.home.HomeAdminActivity
import com.programmer270487.loginsignuptypicode.ui.signup.SignupActivity

class LoginActivity : AppCompatActivity() {
    private val b: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    val firebaseAuth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

//        if (b.emailInputLayout.isFocused) {
//            b.emailInputLayout.error
//        }

        var emailHasError = false
        var passwordHasError = false

        b.emailEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && emailHasError) {
                emailHasError = false
                b.emailInputLayout.error = null
            }
        }

        b.passwordEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && passwordHasError) {
                passwordHasError = false
                b.passwordInputLayout.error = null
            }
        }

        b.loginButton.setOnClickListener {
            val email = b.emailEditText.text.toString()
            val password = b.passwordEditText.text.toString()

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
//                emailHasError = false
//                passwordHasError = false
                // If no error is occured, then login, go to home screen and finish this login screen
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                        checkRole(it.user!!.uid)

                        /*val intent = Intent(this, HomeActivity::class.java)
                        intent.putExtra("email", b.emailEditText.text.toString())
                        intent.putExtra("password", b.passwordEditText.text.toString())
                        startActivity(intent)
                        finish()*/
                    }
                    /*.addOnCanceledListener {
                        Toast.makeText(this, "Login cancelled", Toast.LENGTH_SHORT).show()
                    }*/
                    .addOnFailureListener {
                        emailHasError = true
                        b.emailInputLayout.error = "Enter your registered email"

                        /*when (it) {
                            is FirebaseAuthInvalidUserException -> {
                                b.emailInputLayout.error =
                                    "You have not registered yet. Sign up or re-check your email."
                                emailHasError = true
                            }
                            is FirebaseAuthInvalidCredentialsException -> {
                                b.passwordInputLayout.error = "Incorrect password."
                                passwordHasError = true
                            }
                            else -> {
                                Toast.makeText(
                                    this,
                                    "Authentication failed \"${it.localizedMessage}\"",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }*/
                    }
            } else {
                Toast.makeText(this, "Please check both email and password", Toast.LENGTH_SHORT).show()

//                emailHasError = true
//                b.emailInputLayout.error = "Please enter valid email"
//                passwordHasError = true
//                b.passwordInputLayout.error = "Please enter password"
                /*if (emailHasError) {
                    emailHasError = true
                    b.emailInputLayout.error = "Please enter valid email"
                } else if (passwordHasError) {
                    passwordHasError = true
                    b.passwordInputLayout.error = "Please enter password"
                }*/
            }
        }

        b.textToRegisterScreen.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java).apply {
                putExtra("email", b.emailEditText.text.toString())
            }
            startActivity(intent)
            b.emailEditText.setText("")
            b.passwordEditText.setText("")
            b.emailInputLayout.error = "Enter correct email"
            b.passwordInputLayout.error = "Enter correct password"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SIGN_UP && resultCode == RESULT_OK) {
            val message = data?.getStringExtra("SIGN UP")

        }
    }

    override fun onStart() {
        super.onStart()
        // If previously logged in just redirect to home screen
        if (firebaseAuth.currentUser != null) {
            val documentReference = firestore.collection("Users")
                .document(firebaseAuth.currentUser!!.uid)
            documentReference.get()
                .addOnSuccessListener {
                    if (it.getBoolean("adminRole") == true) {
                        startActivity(Intent(applicationContext, HomeAdminActivity::class.java))
                        finish()
                    } else {
                        startActivity(Intent(applicationContext, HomeActivity::class.java))
                        finish()
                    }
                }
                .addOnFailureListener {
                    firebaseAuth.signOut()
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                    finish()
                }
        }
    }

    private fun checkRole(uid: String) {
        val documentReference = firestore.collection("Users").document(uid)
        documentReference.get().addOnSuccessListener {
            if (it.getBoolean("adminRole")!!) { // != null
                startActivity(Intent(this, HomeAdminActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
    }

    companion object {
        private const val REQUEST_SIGN_UP = 1
    }
}