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
import com.example.volleyexample.adapter.UserAdapter
import com.example.volleyexample.databinding.ActivitySecondBinding
import com.example.volleyexample.model.User
import com.example.volleyexample.util.NetworkUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import java.lang.reflect.Type

class SecondActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySecondBinding.inflate(layoutInflater) }
    private val userAdapter by lazy { UserAdapter() }
    private val url = "https://jsonplaceholder.typicode.com/posts"
    private lateinit var requestQueue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.rv.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@SecondActivity)
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
            url,
            null,
            object : Response.Listener<JSONArray> {
                override fun onResponse(response: JSONArray?) {
                    binding.progressBar.isVisible = false
                    val type: Type = object : TypeToken<List<User>>() {}.type
                    val userList = Gson().fromJson<List<User>>(response.toString(), type)
                    userAdapter.submitList(userList)
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    binding.progressBar.isVisible = false
                    Log.d("@@@", error.toString())
                }
            }
        )
        requestQueue.add(jsonArrayRequest)
    }
}