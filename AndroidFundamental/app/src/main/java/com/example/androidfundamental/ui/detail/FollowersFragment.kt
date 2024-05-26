package com.example.androidfundamental.ui.detail

import android.os.Bundle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.androidfundamental.ui.main.UserAdapter
import com.example.androidfundamental.data.model.User
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidfundamental.R
import com.example.androidfundamental.api.MyApiConfig
import com.example.androidfundamental.databinding.FragmentBinding
import com.example.androidfundamental.ui.main.DetailCardActivity

class FollowersFragment : Fragment(R.layout.fragment) {

    private val TAG = "FollowersFragment"
    private lateinit var adapter: UserAdapter
    private lateinit var username: String
    private var _binding: FragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBinding.bind(view)

        val args = requireArguments()
        username = args.getString(DetailCardActivity.EXTRA_USERNAME).toString()

        setupRecyclerView()
        showLoading(true)
        findFollowers(username)
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter(emptyList()) { }
        binding.rvUser.adapter = adapter
        binding.rvUser.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUser.addItemDecoration(createDivider())
    }

    private fun createDivider(): RecyclerView.ItemDecoration {
        return DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
    }

    private fun findFollowers(username: String) {
        val client = MyApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                showLoading(false)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    setFollowersData(responseBody)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setFollowersData(userList: List<User>) {
        val filteredList = userList.filter { it.login != null }
        adapter.submitList(filteredList)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
