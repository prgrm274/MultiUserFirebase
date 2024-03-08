package com.programmer270487.loginsignuptypicode.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.programmer270487.loginsignuptypicode.databinding.ActivityHomeBinding
import com.programmer270487.loginsignuptypicode.service.ApiClient
import com.programmer270487.loginsignuptypicode.ui.login.AuthViewModel
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private val b: ActivityHomeBinding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private lateinit var adapter: HomeAdapter
    private var currentPage = 1
    private val pageSize = 10
    private var isLastPage = false
    private var isLoading = false
    val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        adapter = HomeAdapter(mutableListOf())
        val layoutManager = LinearLayoutManager(this)
        b.recyclerView.layoutManager = layoutManager
        b.recyclerView.adapter = adapter

        b.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= pageSize
                    ) {
                        loadMoreItems()
                    }
                }
            }
        })

        loadMoreItemsAndSort()

        b.logOutButton.setOnClickListener {
            viewModel.logOut(this) {
            }
        }
    }

    private fun loadMoreItemsAndSort() {
        isLoading = true
        // Assuming you have a coroutine scope, otherwise you might need to adjust this logic.
        lifecycleScope.launch {
            try {
                val response = ApiClient.api.getPhotos(currentPage, pageSize)
                if (response.isSuccessful && response.body() != null) {
                    val fetchedPhotos = response.body()!!

                    // Sort the data based on albumId
                    val sortedPhotos = fetchedPhotos.sortedBy { it.albumId }

                    adapter.add(sortedPhotos)
                    isLoading = false
                    currentPage++
                    if (fetchedPhotos.size < pageSize) {
                        isLastPage = true
                    }
                } else {
                    // Handle error
                    isLoading = false
                }
            } catch (e: Exception) {
                // Handle error
                isLoading = false
            }
        }
    }


    private fun loadMoreItems() {
        isLoading = true
        // Assuming you have a coroutine scope, otherwise you might need to adjust this logic.
        lifecycleScope.launch {
            try {
                val response = ApiClient.api.getPhotos(currentPage, pageSize)
                if (response.isSuccessful && response.body() != null) {
                    val newPhotos = response.body()!!
                    adapter.add(newPhotos)
                    isLoading = false
                    currentPage++
                    if (newPhotos.size < pageSize) {
                        isLastPage = true
                    }
                } else {
                    // Handle error
                    isLoading = false
                }
            } catch (e: Exception) {
                // Handle error
                isLoading = false
            }
        }
    }

    private fun logout() {
//        FirebaseAuth.getInstance().signOut()
//        startActivity(Intent(this, LoginActivity::class.java))
//        finish()
    }
}