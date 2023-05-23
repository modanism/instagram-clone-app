package com.example.instagramclone.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "stories")
data class StoryItem(
    @PrimaryKey @field:SerializedName("id") val id: String,
    val photoUrl: String,
    val createdAt: String,
    val name: String,
    val description: String,
    val lat: Double,
    val lon: Double
)