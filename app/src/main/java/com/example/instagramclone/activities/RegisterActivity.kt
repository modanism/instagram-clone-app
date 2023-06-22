package com.example.instagramclone.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.instagramclone.databinding.ActivityRegisterBinding
import com.example.instagramclone.network.ApiConfig
import com.example.instagramclone.network.responses.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    companion object {
        private const val TAG = "RegisterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            if (edRegisterName.text.isNullOrEmpty()) {
                edRegisterName.error = "Username must be at least 1 character long"
            }

            if (edRegisterEmail.text.isNullOrEmpty()) {
                edRegisterEmail.error = "Invalid email format"
            }

            if (edRegisterPassword.text.isNullOrEmpty()) {
                edRegisterPassword.error = "Password must be at least 8 characters"
            }

            actionRegister.setOnClickListener {
                val name = edRegisterName.text.toString()
                val email = edRegisterEmail.text.toString()
                val password = edRegisterPassword.text.toString()

                register(name, email, password)
            }

            tvToLogin.setOnClickListener {
                finish()
            }

            edRegisterName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    val username = s.toString().trim()
                    if (username.isEmpty()) {
                        edRegisterName.error = "Username must be at least 1 character long"
                    } else {
                        edRegisterEmail.error = null
                    }
                }
            })
        }



    }

    private fun register(name: String, email: String, password: String) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || binding.edRegisterEmail.error != null || binding.edRegisterName.error != null|| binding.edRegisterPassword.error != null) {
            Toast.makeText(this@RegisterActivity,"Invalid credentials", Toast.LENGTH_SHORT)
                .show()
        } else {
            showLoading(true)
            val client = ApiConfig.getApiService().register(name, email, password)
            client.enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null && !responseBody.error) {
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(this@RegisterActivity, "User created", Toast.LENGTH_SHORT).show()
                    } else {
                        showLoading(false)
                        Toast.makeText(this@RegisterActivity, response.message(), Toast.LENGTH_SHORT)
                            .show()
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    showLoading(false)
                    Toast.makeText(this@RegisterActivity, t.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                    Log.e(TAG, t.message.toString())
                }

            })
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}
