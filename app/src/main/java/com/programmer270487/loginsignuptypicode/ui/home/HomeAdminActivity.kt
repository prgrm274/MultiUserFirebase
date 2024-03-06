package com.programmer270487.loginsignuptypicode.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.programmer270487.loginsignuptypicode.R
import com.programmer270487.loginsignuptypicode.databinding.ActivityHomeAdminBinding
import com.programmer270487.loginsignuptypicode.ui.login.LoginActivity

/**
 * good@good.com
 */
class HomeAdminActivity : AppCompatActivity() {
    private val b: ActivityHomeAdminBinding by lazy { ActivityHomeAdminBinding.inflate(layoutInflater) }
    val firebaseAuth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        firestore.collection("Users").get()
            .addOnSuccessListener {
                for (document in it) {
                    Log.i("HomeAdmin", "${document.id} ${document.data}")
                    // KZXLgckB2VS3LaAjtDgsEIEwqsI3 {adminRole=false, email=user1@user1.com, username=User 1}
                    // wLsmIeTz3yUq2uR0M1ArNLRMFR33 {adminRole=true, email=good@good.com, username=good admin}
                }
            }
    }

    override fun onStop() {
        super.onStop()
        logout()
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}