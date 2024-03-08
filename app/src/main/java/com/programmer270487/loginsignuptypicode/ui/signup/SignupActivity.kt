package com.programmer270487.loginsignuptypicode.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.programmer270487.loginsignuptypicode.databinding.ActivitySignupBinding
import com.programmer270487.loginsignuptypicode.ui.login.AuthViewModel
import com.programmer270487.loginsignuptypicode.utils.State
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {
    private val b: ActivitySignupBinding by lazy { ActivitySignupBinding.inflate(layoutInflater) }
    val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        val getEmail = intent.getStringExtra("email")
        b.emailEditText.setText(getEmail)

        observe()

        b.signupAdminButton.setOnClickListener {
            val uname = b.usernameEditText.text.toString()
            val email = b.emailEditText.text.toString()
            val password = b.passwordEditText.text.toString()
            viewModel.register(
                uname,
                email,
                password,
                true
            )
        }
        b.signupButton.setOnClickListener {
            val uname = b.usernameEditText.text.toString()
            val email = b.emailEditText.text.toString()
            val password = b.passwordEditText.text.toString()
            viewModel.register(
                uname,
                email,
                password,
                false
            )
        }

        b.textBackToLogin.setOnClickListener {
            finish()
        }
    }

    private fun observe() {
        viewModel.register.observe(this) {
            when (it) {
                is State.Loading -> {
                    b.textPleaseWait.visibility = View.VISIBLE
                }
                is State.Success -> {
                    b.textPleaseWait.visibility = View.GONE
                    Toast.makeText(this, it.data, LENGTH_SHORT).show()
                    finish()
                }
                is State.Failure -> {
                    b.textPleaseWait.visibility = View.GONE
                    Toast.makeText(this, it.error, LENGTH_SHORT).show()
                }
            }
        }
    }
}