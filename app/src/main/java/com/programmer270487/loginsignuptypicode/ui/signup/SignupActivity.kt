package com.programmer270487.loginsignuptypicode.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.programmer270487.loginsignuptypicode.data.model.Role
import com.programmer270487.loginsignuptypicode.databinding.ActivitySignupBinding
import com.programmer270487.loginsignuptypicode.ui.login.LoginViewModel
import com.programmer270487.loginsignuptypicode.utils.State
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {
    private val b: ActivitySignupBinding by lazy { ActivitySignupBinding.inflate(layoutInflater) }

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val valid = true
    val viewModel: LoginViewModel by viewModels()
//    private val viewModel: LoginViewModel by lazy {
//        ViewModelProvider(this)[LoginViewModel::class.java]
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        val getEmail = intent.getStringExtra("email")
        b.emailEditText.setText(getEmail)

//        observe(null)

        b.signupAdminButton.setOnClickListener {
//            signUp(true)
            observe(true)
        }
        b.signupButton.setOnClickListener {
//            signUp(false)
            observe(false)
        }

        b.textBackToLogin.setOnClickListener {
//            onBackPressedDispatcher.onBackPressed()
            finish()
        }
    }

    private fun observe(isAdmin: Boolean) {
        viewModel.register.observe(this) {
            when (it) {
                is State.Loading -> {

                }
                is State.Success -> {
                    val uname = b.usernameEditText.text.toString()
                    val email = b.emailEditText.text.toString()
                    val password = b.passwordEditText.text.toString()
                    viewModel.register(
                        email,
                        password,
                        isAdmin
                    )
                }
                is State.Failure -> {}
            }
        }
    }

    private fun signUp(adminRole: Boolean) {
        val uname = b.usernameEditText.text.toString()
        val email = b.emailEditText.text.toString()
        val password = b.passwordEditText.text.toString()

        if (!TextUtils.isEmpty(uname) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            // If no error is occured, then register, finish this register screen and back to login screen
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isComplete) {
                        Toast.makeText(this, "Please wait..", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnSuccessListener {
                    Toast.makeText(
                        this,
                        "Register successful, please login",
                        Toast.LENGTH_SHORT
                    ).show()

                    val user = firebaseAuth.currentUser
                    val documentReference = firestore.collection("Users").document(user!!.uid)
                    val userInfo = HashMap<String, Any>()
                    userInfo["username"] = uname
                    userInfo["email"] = email
                    userInfo["adminRole"] = adminRole
                    documentReference.set(userInfo)

                    val returnIntent = Intent().apply {
                        putExtra("SIGN UP", email)
                    }
                    setResult(RESULT_OK, returnIntent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Failed register (${it.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            Toast.makeText(this, "Please check all the fields ", Toast.LENGTH_SHORT).show()
        }
    }
}