package com.example.instagramclone.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.instagramclone.ListStoryItem
import com.example.instagramclone.paging.StoriesRepository
import com.example.instagramclone.utils.UserSharedPreferences

class PagingViewModel(application: Application, storiesRepository: StoriesRepository, token: String) : AndroidViewModel(application) {

    val stories: LiveData<PagingData<ListStoryItem>> =
        storiesRepository.getStories(token).cachedIn(viewModelScope)

    fun removeUserSession() {
        val userPreference = UserSharedPreferences(getApplication())
        userPreference.clearUser()
    }
}