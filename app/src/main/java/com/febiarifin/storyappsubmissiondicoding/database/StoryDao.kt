package com.febiarifin.storyappsubmissiondicoding.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.febiarifin.storyappsubmissiondicoding.data.response.StoryResponseItem

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<StoryResponseItem>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, StoryResponseItem>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}