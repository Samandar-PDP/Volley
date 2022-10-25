package com.example.volleyexample

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.volleyexample.databinding.ActivityMainBinding
import com.example.volleyexample.util.NetworkUtils
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var requestQueue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val networkUtils = NetworkUtils(this)
        if (networkUtils.isNetworkConnected()) {
            requestQueue = Volley.newRequestQueue(this)
            imageLoad()
            objectLoad()
        } else {
            Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun objectLoad() {
        val jsonObjectRequest =
            JsonObjectRequest(
                Request.Method.GET,
                "http://ip.jsontest.com/",
                null,
                object : Response.Listener<JSONObject> {
                    override fun onResponse(response: JSONObject?) {
                        val myIp = response?.getString("ip")
                        binding.tv.text = myIp
                    }
                },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError?) {
                        Toast.makeText(
                            this@MainActivity,
                            "${error?.networkResponse?.statusCode}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        requestQueue.add(jsonObjectRequest)
    }

    private fun imageLoad() {
        val imageRequest = ImageRequest("https://i.imgur.com/Nwk25LA.jpg", { bitmap ->
            binding.imageView.setImageBitmap(bitmap)
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888) { error ->
            Toast.makeText(this, "${error.networkResponse.statusCode}", Toast.LENGTH_SHORT).show()
        }
        requestQueue.add(imageRequest)
    }
}