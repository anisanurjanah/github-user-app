package com.anisanurjanah.githubuserapp.ui.main

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import com.anisanurjanah.githubuserapp.R
import com.anisanurjanah.githubuserapp.data.local.entity.FavoriteUser
import com.anisanurjanah.githubuserapp.data.remote.response.DetailUserResponse
import com.anisanurjanah.githubuserapp.databinding.ActivityDetailUserBinding
import com.anisanurjanah.githubuserapp.helper.ViewModelFactory
import com.anisanurjanah.githubuserapp.ui.adapter.SectionsPagerAdapter
import com.anisanurjanah.githubuserapp.viewmodel.DetailViewModel
import com.anisanurjanah.githubuserapp.viewmodel.FavoriteViewModel
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel

    private lateinit var favoriteUser: FavoriteUser
    private var isFavorite = false

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                true
            } else {
                showToast("Notifications permission rejected")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        val username = intent.getStringExtra(EXTRA_USERNAME).toString()

        with(binding) {
            topAppBar.title = "@$username"

            topAppBar.inflateMenu(R.menu.other_menu)
            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_settings -> {
                        startActivity(Intent(this@DetailUserActivity, SettingActivity::class.java))
                        true
                    }
                    R.id.menu_share -> {
                        detailViewModel.githubUserDetail.observe(this@DetailUserActivity) { user ->
                            shareGithubUserDetail(user)
                        }
                        true
                    }
                    else -> false
                }
            }
            topAppBar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
            topAppBar.setNavigationOnClickListener {
                finish()
            }
        }

        detailViewModel = obtainViewModel(this@DetailUserActivity)
        if (username.isNotBlank()) {
            detailViewModel.findGithubUserDetail(username)
        }

        detailViewModel.githubUserDetail.observe(this) { user ->
            setGithubUserDetail(user)
            favoriteUser = FavoriteUser(
                user.login,
                user.avatarUrl
            )
        }
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username

        val tabs = binding.tabs
        val viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        favoriteViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this@DetailUserActivity)
        )[FavoriteViewModel::class.java]

        favoriteViewModel.isFavoriteUser(username).observe(this) {
            isFavorite = it != null
            setIcon()
        }
        favoriteViewModel.isFavoriteUser(username).observe(this) {
            isFavorite == true
            setIcon()
        }

        binding.fabFavorite.setOnClickListener {
            if (!isFavorite) {
                favoriteViewModel.insert(favoriteUser)
                sendNotification(favoriteUser.username)
//                showToast("Successfully added $username to Favorite!")
            } else {
                favoriteViewModel.delete(favoriteUser)
                showToast("Successfully deleted $username from favorite users.")
            }
        }
    }

    private fun setGithubUserDetail(items: DetailUserResponse) {
        binding.apply {
            if (items != null) {
                tvNameDetail.text = items.name ?: "No name available"
                tvBioDetail.text = items.bio?.toString() ?: "No bio available"
                tvFollowersNumber.text = items.followers?.toString() ?: "0"
                tvFollowingNumber.text = items.following?.toString() ?: "0"
                Glide.with(this@DetailUserActivity)
                    .load(items.avatarUrl)
                    .into(imgAvatarDetail)
            } else {
                tvNameDetail.text = "No name available"
                tvBioDetail.text = "No bio available"
                tvFollowersNumber.text = "0"
                tvFollowingNumber.text = "0"
                imgAvatarDetail.setImageResource(android.R.color.transparent)
            }
        }
    }

    private fun sendNotification(username: String) {
        val intent = Intent(this, FavoriteUserActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Add to Favorites")
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentText("Successfully added $username to favorite users. Tap this notification to see the details!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            builder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun shareGithubUserDetail(items: DetailUserResponse) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"

        val shareMessage = StringBuilder()
        shareMessage.append("Hi everyone!\n\n")
        shareMessage.append("${items.name} is on GitHub with id @${items.login}. ")
        shareMessage.append("Follow to find out the latest updates from ${items.name} by clicking on the link below.\n")
        shareMessage.append("Let's build wider relationships around the world!\n")
        shareMessage.append(items.htmlUrl)

        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage.toString())
        startActivity(Intent.createChooser(shareIntent, "Share GitHub User Detail"))
    }

    private fun setIcon() {
        if (isFavorite) {
            binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_27"
        private const val CHANNEL_NAME = "anisa channel"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}