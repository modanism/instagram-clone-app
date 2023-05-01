package com.example.instagramclone

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagramclone.databinding.ActivityMainBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        val pref = UserPreferences.getInstance(dataStore)
        val authViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            AuthViewModel::class.java
        )

        authViewModel.getAuthSettings().observe(this) { authData ->
            mainViewModel.findStories(authData.token)
            mainViewModel.listStory.observe(this) {
                setStoryData(it)
            }
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

        binding.myFloatingButton.setOnClickListener {
            val postIntent = Intent(this,PostActivity::class.java)
            startActivity(postIntent)
        }



        // Create a new instance of OnBackPressedCallback
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Finish all activities and close the app
                finishAffinity()
            }
        }

        // Add the callback to the OnBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

    }

    private fun setStoryData(listStory : List<ListStoryItem>) {
        val adapter = StoryAdapter(listStory)
        binding.rvStory.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }



}



















