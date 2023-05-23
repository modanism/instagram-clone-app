package com.example.instagramclone.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.instagramclone.ListStoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface StoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(quote: List<ListStoryItem>)

    @Query("SELECT * FROM stories")
    fun getAllStories(): PagingSource<Int, ListStoryItem>

    @Query("SELECT * from stories WHERE lat <> 'null' and lon <> 'null'")
    fun getStoriesLocation(): Flow<List<ListStoryItem>>

//    @Query("DELETE FROM stories")
//    suspend fun deleteAll()
}