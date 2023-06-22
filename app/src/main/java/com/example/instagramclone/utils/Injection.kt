package com.example.instagramclone.utils

import android.content.Context
import com.example.instagramclone.network.ApiConfig
import com.example.instagramclone.paging.StoriesRepository

object Injection {
    fun provideRepository(context: Context): StoriesRepository {
        val apiService = ApiConfig.getApiService()
        return StoriesRepository(apiService)
    }
}