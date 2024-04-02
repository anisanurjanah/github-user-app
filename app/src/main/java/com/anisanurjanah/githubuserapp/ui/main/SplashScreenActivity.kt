package com.anisanurjanah.githubuserapp.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.anisanurjanah.githubuserapp.R

class SplashScreenActivity : AppCompatActivity() {

    private var DELAY_MILLIS: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        setContentView(R.layout.activity_splash_screen)

        window.decorView.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, DELAY_MILLIS)
    }
}