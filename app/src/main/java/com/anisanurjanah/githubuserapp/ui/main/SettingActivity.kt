package com.anisanurjanah.githubuserapp.ui.main

import android.content.Context
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.anisanurjanah.githubuserapp.R
import com.anisanurjanah.githubuserapp.databinding.ActivitySettingBinding
import com.anisanurjanah.githubuserapp.helper.SettingPreferences
import com.anisanurjanah.githubuserapp.helper.SettingViewModelFactory
import com.anisanurjanah.githubuserapp.viewmodel.SettingViewModel

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            topAppBar.title = getString(R.string.setting_bar)

            topAppBar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
            topAppBar.setNavigationOnClickListener {
                finish()
            }
        }

        val switchTheme = binding.switchTheme

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }
    }
}