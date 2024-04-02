package com.anisanurjanah.githubuserapp.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.anisanurjanah.githubuserapp.R
import com.anisanurjanah.githubuserapp.data.remote.response.ItemsItem
import com.anisanurjanah.githubuserapp.databinding.ActivityFavoriteUserBinding
import com.anisanurjanah.githubuserapp.helper.ViewModelFactory
import com.anisanurjanah.githubuserapp.ui.adapter.UserAdapter
import com.anisanurjanah.githubuserapp.viewmodel.FavoriteViewModel

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            topAppBar.title = getString(R.string.favorite_bar)

            topAppBar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
            topAppBar.setNavigationOnClickListener {
                finish()
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavoriteUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFavoriteUsers.addItemDecoration(itemDecoration)

        val favoriteViewModel = obtainViewModel(this@FavoriteUserActivity)

        showLoading(true)
        favoriteViewModel.getAllFavorite().observe(this) { users ->
            val userAdapter = UserAdapter()
            val items = arrayListOf<ItemsItem>()
            users.map {
                val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl.toString())
                items.add(item)
            }
            userAdapter.submitList(items)

            if (items.isEmpty()) {
                binding.rvFavoriteUsers.visibility = View.GONE
                binding.userNotFound.visibility = View.VISIBLE
                showLoading(false)
            } else {
                binding.rvFavoriteUsers.visibility = View.VISIBLE
                binding.userNotFound.visibility = View.GONE

                binding.rvFavoriteUsers.adapter = userAdapter
                showLoading(false)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }
}