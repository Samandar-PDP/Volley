package com.example.volleyexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.volleyexample.adapter.GithubAdapter
import com.example.volleyexample.databinding.ActivityGithubBinding
import com.example.volleyexample.model.GithubUser
import com.example.volleyexample.model.User
import com.example.volleyexample.util.NetworkUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import java.lang.reflect.Type

class GithubActivity : AppCompatActivity() {
    private val binding by lazy { ActivityGithubBinding.inflate(layoutInflater) }
    private val githubAdapter by lazy { GithubAdapter() }
    private lateinit var requestQueue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.rv.apply {
            adapter = githubAdapter
            layoutManager = LinearLayoutManager(this@GithubActivity)
        }
        val networkUtils = NetworkUtils(this)
        if (networkUtils.isNetworkConnected()) {
            requestQueue = Volley.newRequestQueue(this)
            loadUsers()
        }
    }

    private fun loadUsers() {
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            "https://api.github.com/users",
            null,
            object : Response.Listener<JSONArray> {
                override fun onResponse(response: JSONArray?) {
                    binding.progressBar.isVisible = false
                    val type: Type = object : TypeToken<List<GithubUser>>() {}.type
                    val userList = Gson().fromJson<List<GithubUser>>(response.toString(), type)
                    githubAdapter.submitList(userList)
                }
            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Log.d("@@@", error.toString())
                }
            }
        )
        requestQueue.add(jsonArrayRequest)
    }
}