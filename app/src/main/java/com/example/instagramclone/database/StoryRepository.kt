package com.example.instagramclone.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.*
import com.example.instagramclone.ListStoryItem
import com.example.instagramclone.network.ApiService
import com.example.instagramclone.paging.StoryRemoteMediator


class StoryRepository(
    private val database: StoryDatabase,
    private val apiServices: ApiService,
    private val context: Context
) {

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(database, apiServices, context),
            pagingSourceFactory = {
                database.storiesDao().getAllStories()
            }
        ).liveData
    }

    fun getStoriesLocation(): LiveData<List<ListStoryItem>> {
        return database.storiesDao().getStoriesLocation().asLiveData()
    }
}