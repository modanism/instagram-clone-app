package com.example.instagramclone.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagramclone.ListStoryItem
import com.example.instagramclone.UserPreferences
import com.example.instagramclone.adapters.ListStoriesAdapter
import com.example.instagramclone.adapters.LoadingStateAdapter
import com.example.instagramclone.adapters.StoryAdapter
import com.example.instagramclone.databinding.ActivityMainBinding
import com.example.instagramclone.models.AuthViewModel
import com.example.instagramclone.models.MainViewModel
import com.example.instagramclone.models.PagingViewModel
import com.example.instagramclone.utils.UserModel
import com.example.instagramclone.utils.UserSharedPreferences
import com.example.instagramclone.utils.ViewModelFactory
import java.util.regex.Pattern

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mUserPreference: UserSharedPreferences
    private lateinit var userData: UserModel
    private lateinit var pagingViewModel: PagingViewModel
    private val adapter: ListStoriesAdapter by lazy { ListStoriesAdapter() }



    companion object {
        const val EXTRA_USER = "extra_user"
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        val pref = UserPreferences.getInstance(dataStore)
        val authViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            AuthViewModel::class.java
        )

        authViewModel.getAuthSettings().observe(this) {
            if (it.token.isEmpty()) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        authViewModel.getAuthSettings().observe(this) { authData ->
            mainViewModel.findStories(authData.token)
//            mainViewModel.listStory.observe(this) {
//                setStoryData(it)
//            }
        }

        mUserPreference = UserSharedPreferences(this)
        userData = mUserPreference.getUser()

        pagingViewModel = ViewModelProvider(this,ViewModelFactory( application = application, context = this, token = userData.token ))[PagingViewModel::class.java]

        binding.apply {
            rvStory.layoutManager = LinearLayoutManager(this@MainActivity)
            rvStory.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
        }

        pagingViewModel.stories.observe(this) {
            adapter.submitData(lifecycle,it)
        }

        adapter.setOnItemClickCallback(object : ListStoriesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem) {
                Intent(this@MainActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_IMAGE, data.photoUrl)
                    it.putExtra(DetailActivity.EXTRA_USERNAME, data.name)
                    it.putExtra(DetailActivity.EXTRA_DESCRIPTION, data.description)
                    it.putExtra(DetailActivity.EXTRA_DATE, data.createdAt)
                    startActivity(it)
                }
            }
        })

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

//        val layoutManager = LinearLayoutManager(this)
//        binding.rvStory.layoutManager = layoutManager

        binding.myFloatingButton.setOnClickListener {
            val postIntent = Intent(this, PostActivity::class.java)
            startActivity(postIntent)
            finish()
        }

        binding.actionMap.setOnClickListener {
            val mapIntent = Intent(this, MapsActivity::class.java)
            mainViewModel.listStory.observe(this) {
                mapIntent.putParcelableArrayListExtra("extra_story", ArrayList(it))
            }
            startActivity(mapIntent)

        }

        binding.actionProfile.setOnClickListener {
            val profileIntent = Intent(this,ProfileActivity::class.java)
            profileIntent.putExtra(ProfileActivity.EXTRA_USERNAME, userData.name)
            startActivity(profileIntent)
        }







        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        }

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




















