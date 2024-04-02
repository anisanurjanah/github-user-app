package com.anisanurjanah.githubuserapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.anisanurjanah.githubuserapp.databinding.FragmentFollowBinding
import com.anisanurjanah.githubuserapp.helper.ViewModelFactory
import com.anisanurjanah.githubuserapp.ui.adapter.FollowAdapter
import com.anisanurjanah.githubuserapp.viewmodel.FollowViewModel

class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    private lateinit var followViewModel: FollowViewModel

    private var position: Int = 0
    private var username: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUsers.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(activity, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        followViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory.getInstance(requireActivity())
        )[FollowViewModel::class.java]

        followViewModel.githubUserFollow.observe(viewLifecycleOwner) { user ->
            val followAdapter = FollowAdapter(user)

            if (user.isNullOrEmpty()) {
                binding.rvUsers.visibility = View.GONE
                binding.userNotFound.visibility = View.VISIBLE
            } else {
                binding.rvUsers.visibility = View.VISIBLE
                binding.userNotFound.visibility = View.GONE

                binding.rvUsers.adapter = followAdapter
            }
        }
        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        followViewModel.setUserFollowPosition(username ?: "", position)
        binding.root.requestLayout()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARG_POSITION = "arg_position"
        const val ARG_USERNAME = "arg_username"
    }
}