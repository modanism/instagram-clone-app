package com.example.instagramclone.utils

import com.example.instagramclone.ListStoryItem

object DataDummy {
    fun generateDummyStoriesEntity(): List<ListStoryItem> {
        val storyList = ArrayList<ListStoryItem>()
        for (i in 0..50) {
            val story = ListStoryItem(
                "photo_url_$i",
                "2022-02-22T22:22:22Z",
                "name_$i",
                "description_$i",
                "id_$i",
                -69.69,
                -69.69
            )
            storyList.add(story)
        }

        return storyList
    }
}