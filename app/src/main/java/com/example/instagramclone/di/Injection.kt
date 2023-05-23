package com.example.instagramclone.di

import android.content.Context
import com.example.instagramclone.database.StoryDatabase
import com.example.instagramclone.database.StoryRepository
import com.example.instagramclone.network.ApiConfig

object Injection {
    fun provideRepository(context: Context) : StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService, context)
    }

}