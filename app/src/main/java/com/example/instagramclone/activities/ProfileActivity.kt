package com.example.instagramclone.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.instagramclone.R
import com.example.instagramclone.UserPreferences
import com.example.instagramclone.databinding.ActivityProfileBinding
import com.example.instagramclone.models.AuthViewModel
import com.example.instagramclone.utils.ViewModelFactory


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreferences.getInstance(dataStore)
        val authViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            AuthViewModel::class.java
        )

        binding.actionBack.setOnClickListener {
            finish()
        }

        binding.actionLogout.setOnClickListener {
            authViewModel.removeAuthSetting()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val username = intent.getStringExtra(EXTRA_USERNAME)
        binding.tvName.text = username
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"

    }
}