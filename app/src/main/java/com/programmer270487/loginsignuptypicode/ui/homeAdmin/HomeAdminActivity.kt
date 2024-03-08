package com.programmer270487.loginsignuptypicode.ui.homeAdmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.programmer270487.loginsignuptypicode.data.model.Role
import com.programmer270487.loginsignuptypicode.databinding.ActivityHomeAdminBinding
import com.programmer270487.loginsignuptypicode.ui.login.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeAdminActivity : AppCompatActivity() {
    private val b: ActivityHomeAdminBinding by lazy { ActivityHomeAdminBinding.inflate(layoutInflater) }
    private lateinit var adapter: HomeAdminAdapter
    val firestore = FirebaseFirestore.getInstance()
    val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        adapter = HomeAdminAdapter(mutableListOf())
        b.recyclerView.layoutManager = LinearLayoutManager(this)
        b.recyclerView.adapter = adapter

        // Set up firestore query and snapshot listener
        val query = firestore.collection("Users")
        query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Handle the error
                return@addSnapshotListener
            }

            if (snapshot != null) {
                var list = mutableListOf<Role>()
                for (item in snapshot.documents.toList()) {
                    val role = Role(
                        item.id,
                        item.getString("username")!!,
                        item.getString("email")!!,
                        item.getBoolean("adminRole")!!,
                    )
                    list.add(role)
                }
                // Pass the initial snapshot data to the adapter
                adapter.add(list)
            }
        }

        b.logOutButton.setOnClickListener {
            viewModel.logOut(this) {
            }
        }
    }
}