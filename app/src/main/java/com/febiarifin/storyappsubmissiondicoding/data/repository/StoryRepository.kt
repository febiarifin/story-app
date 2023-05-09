package com.febiarifin.storyappsubmissiondicoding.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.febiarifin.storyappsubmissiondicoding.api.ApiService
import com.febiarifin.storyappsubmissiondicoding.data.remotemediator.StoryRemoteMediator
import com.febiarifin.storyappsubmissiondicoding.data.response.StoryResponseItem
import com.febiarifin.storyappsubmissiondicoding.database.StoryDatabase

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiService) {

    fun getStory(): LiveData<PagingData<StoryResponseItem>>{
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }
}