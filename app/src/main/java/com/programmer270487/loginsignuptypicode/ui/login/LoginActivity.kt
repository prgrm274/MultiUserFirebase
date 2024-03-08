package com.programmer270487.loginsignuptypicode.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.programmer270487.loginsignuptypicode.databinding.ActivityMainBinding
import com.programmer270487.loginsignuptypicode.ui.home.HomeActivity
import com.programmer270487.loginsignuptypicode.ui.homeAdmin.HomeAdminActivity
import com.programmer270487.loginsignuptypicode.ui.signup.SignupActivity
import com.programmer270487.loginsignuptypicode.utils.State
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val b: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        observe()

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
                viewModel.login(email, password)

            } else {
                Toast.makeText(this, "Please check both email and password", Toast.LENGTH_SHORT).show()
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

    private fun observe() {
        viewModel.login.observe(this) {
            when (it) {
                is State.Loading -> {
                    b.textPleaseWait.visibility = View.VISIBLE
                }
                is State.Success -> {
                    b.textPleaseWait.visibility = View.GONE
                    Toast.makeText(this, it.data, Toast.LENGTH_SHORT).show()

                    viewModel.handleOnStart(this) { isLoggedIn, isAdmin ->
                        if (isLoggedIn) {
                            if (isAdmin) {
                                startActivity(Intent(applicationContext, HomeAdminActivity::class.java))
                                finish()
                            } else {
                                startActivity(Intent(applicationContext, HomeActivity::class.java))
                                finish()
                            }
                        }
                    }
                }
                is State.Failure -> {
                    b.textPleaseWait.visibility = View.GONE
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SIGN_UP && resultCode == RESULT_OK) {
            val message = data?.getStringExtra("SIGN UP")
            Toast.makeText(this, "After sign up", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        // If previously logged in just redirect to home screen
        viewModel.handleOnStart(this) { isLoggedIn, isAdmin ->
            if (isLoggedIn) {
                if (isAdmin) {
                    startActivity(Intent(applicationContext, HomeAdminActivity::class.java))
                } else {
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                }
            } /*else {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }*/
//            finish()
        }
    }

    companion object {
        private const val REQUEST_SIGN_UP = 1
    }
}