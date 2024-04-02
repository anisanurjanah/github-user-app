package com.anisanurjanah.githubuserapp.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anisanurjanah.githubuserapp.data.remote.response.ItemsItem
import com.anisanurjanah.githubuserapp.databinding.ItemFollowBinding
import com.anisanurjanah.githubuserapp.ui.main.DetailUserActivity
import com.bumptech.glide.Glide

class FollowAdapter(private val listGithubUserFollow: List<ItemsItem>): RecyclerView.Adapter<FollowAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemFollowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemFollowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listGithubUserFollow.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val photo = listGithubUserFollow[position].avatarUrl
        val name = listGithubUserFollow[position].login

        with(holder) {
            val username = binding.tvUsername
            username.text = name
            Glide.with(itemView.context)
                .load(photo)
                .into(binding.imgAvatar)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.EXTRA_USERNAME, name)
                itemView.context.startActivity(intent)
            }
        }
    }
}