package com.example.instagramclone.paging

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.instagramclone.ListStoryItem
import com.example.instagramclone.network.ApiService

class StoriesRepository(private val apiService: ApiService) {
    fun getStories(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoriesPagingSource(apiService, token)
            }
        ).liveData
    }
}