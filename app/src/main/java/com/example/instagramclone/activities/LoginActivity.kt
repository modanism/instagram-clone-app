package com.example.instagramclone.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.instagramclone.LoginResponse
import com.example.instagramclone.LoginResult
import com.example.instagramclone.UserPreferences
import com.example.instagramclone.databinding.ActivityLoginBinding
import com.example.instagramclone.models.AuthViewModel
import com.example.instagramclone.network.ApiConfig
import com.example.instagramclone.utils.UserModel
import com.example.instagramclone.utils.UserSharedPreferences
import com.example.instagramclone.utils.ViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    companion object {
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreferences.getInstance(dataStore)
        val authViewModel = ViewModelProvider(this@LoginActivity, ViewModelFactory(pref)).get(
            AuthViewModel::class.java
        )

        authViewModel.getAuthSettings().observe(this) {
            if (it.token.isNotEmpty()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }


        binding.actionLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            login(email, password)
        }

        binding.tvToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)


    }

    private fun login(email : String, password: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().login(email,password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val pref = UserPreferences.getInstance(dataStore)
                    val authViewModel = ViewModelProvider(this@LoginActivity, ViewModelFactory(pref)).get(
                        AuthViewModel::class.java
                    )
                    authViewModel.saveAuthSetting(responseBody.loginResult.name!!, responseBody.loginResult.userId!!, responseBody.loginResult.token!!)

                    val userSharedPreference = UserSharedPreferences(application)
                    val user = UserModel(responseBody.loginResult.name!!, responseBody.loginResult.token!!,true)
                    userSharedPreference.setUser(user)

                    showLoading(false)
                    Toast.makeText(this@LoginActivity, "Login succeeded", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    showLoading(false)
                    binding.edLoginPassword.error = "Invalid username or password"
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@LoginActivity, t.message.toString(), Toast.LENGTH_SHORT).show()

                Log.e(TAG, t.message.toString())
            }

        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}

