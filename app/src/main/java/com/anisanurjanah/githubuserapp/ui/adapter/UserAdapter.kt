package com.anisanurjanah.githubuserapp.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anisanurjanah.githubuserapp.data.remote.response.ItemsItem
import com.anisanurjanah.githubuserapp.databinding.ItemUserBinding
import com.anisanurjanah.githubuserapp.ui.main.DetailUserActivity
import com.bumptech.glide.Glide

class UserAdapter : ListAdapter<ItemsItem, UserAdapter.ListViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val githubUser = getItem(position)
        holder.bind(githubUser)
    }

    class ListViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(items: ItemsItem) {
            with(binding) {
                tvUsername.text = items.login
                Glide.with(itemView.context)
                    .load(items.avatarUrl)
                    .into(imgAvatar)
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.EXTRA_USERNAME, items.login)
                itemView.context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}