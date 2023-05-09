package com.febiarifin.storyappsubmissiondicoding.data.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.febiarifin.storyappsubmissiondicoding.api.ApiService
import com.febiarifin.storyappsubmissiondicoding.data.response.StoryResponseItem
import com.febiarifin.storyappsubmissiondicoding.database.StoryDatabase

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database: StoryDatabase,
    private val apiService: ApiService
): RemoteMediator<Int, StoryResponseItem>() {

    private companion object{
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    private val storyDao = database.storyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryResponseItem>
    ): MediatorResult {
        val page = INITIAL_PAGE_INDEX

        try {

            val responseData = apiService.getStoriesWithPagination(page, state.config.pageSize)

            val endOfPaginationReached = responseData.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    //database.storyDao().deleteAll()
                }
                storyDao.insertStory(responseData)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

}