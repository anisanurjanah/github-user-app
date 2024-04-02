package com.anisanurjanah.githubuserapp.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isEmpty
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.anisanurjanah.githubuserapp.R
import com.anisanurjanah.githubuserapp.data.remote.response.ItemsItem
import com.anisanurjanah.githubuserapp.databinding.ActivityMainBinding
import com.anisanurjanah.githubuserapp.helper.SettingPreferences
import com.anisanurjanah.githubuserapp.helper.SettingViewModelFactory
import com.anisanurjanah.githubuserapp.helper.ViewModelFactory
import com.anisanurjanah.githubuserapp.ui.adapter.UserAdapter
import com.anisanurjanah.githubuserapp.viewmodel.MainViewModel
import com.anisanurjanah.githubuserapp.viewmodel.SettingViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var mainViewModel: MainViewModel

    private lateinit var settingPreferences: SettingPreferences
    private lateinit var settingViewModel: SettingViewModel

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(SETTING)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingPreferences = SettingPreferences.getInstance(dataStore)
        settingViewModel = ViewModelProvider(this, SettingViewModelFactory(settingPreferences))[SettingViewModel::class.java]
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            themeSettings(isDarkModeActive)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        mainViewModel = obtainViewModel(this@MainActivity)
        mainViewModel.githubUser.observe(this) { user ->
            if (user.isNullOrEmpty()) {
                binding.rvUsers.visibility = View.GONE
                binding.userNotFound.visibility = View.VISIBLE
            } else {
                binding.rvUsers.visibility = View.VISIBLE
                binding.userNotFound.visibility = View.GONE
                setGithubUser(user)
            }
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)

            searchBar.inflateMenu(R.menu.option_menu)
            searchBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_favorite -> {
                        startActivity(Intent(this@MainActivity, FavoriteUserActivity::class.java))
                        true
                    }
                    R.id.menu_settings -> {
                        startActivity(Intent(this@MainActivity, SettingActivity::class.java))
                        true
                    }
                    else -> false
                }
            }

            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val query= searchView.text
                searchBar.setText(searchView.text)
                searchView.hide()

                if (searchView.isEmpty()) {
                    showToast("Input user!")
                } else {
                    mainViewModel.findUserByUsername(query.toString())
                }
                false
            }
        }
    }

    private fun setGithubUser(items: List<ItemsItem>) {
        val userAdapter = UserAdapter()
        userAdapter.submitList(items)
        binding.rvUsers.adapter = userAdapter
    }

    private fun themeSettings(isDarkModeActive: Boolean) {
        if (isDarkModeActive) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[MainViewModel::class.java]
    }

    companion object {
        const val SETTING = "settings"
    }
}