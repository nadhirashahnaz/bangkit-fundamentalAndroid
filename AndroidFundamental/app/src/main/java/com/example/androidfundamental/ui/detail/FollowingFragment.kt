package com.example.androidfundamental.ui.detail

import android.os.Bundle
import com.example.androidfundamental.R
import com.example.androidfundamental.api.MyApiConfig
import com.example.androidfundamental.data.model.User
import com.example.androidfundamental.databinding.FragmentBinding
import com.example.androidfundamental.ui.main.DetailCardActivity
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidfundamental.ui.main.UserAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingFragment : Fragment(R.layout.fragment) {

    companion object {
        private const val TAG = "FollowingFragment"
    }

    private lateinit var adapter: UserAdapter
    private lateinit var username: String
    private var _binding: FragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentBinding.bind(view)

        val args = arguments
        username = args?.getString(DetailCardActivity.EXTRA_USERNAME).toString()

        adapter = UserAdapter(listOf()) { selectedItem: User -> }
        binding.rvUser.adapter = adapter

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)
        showLoading(true)
        findFollowing(username)
    }

    private fun findFollowing(username: String) {
        showLoading(true)
        val client = MyApiConfig.getApiService().getFollowing(username)

        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        setFollowingData(it)
                    } ?: Log.e(TAG, "onResponse: response body is null")
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

    private fun setFollowingData(userList: List<User>) {
        val listUser = userList.filter { it.login != null }
        adapter.submitList(listUser)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
