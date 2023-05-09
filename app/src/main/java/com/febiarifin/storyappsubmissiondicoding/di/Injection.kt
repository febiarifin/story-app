package com.febiarifin.storyappsubmissiondicoding.di

import android.content.Context
import com.febiarifin.storyappsubmissiondicoding.api.ApiConfig
import com.febiarifin.storyappsubmissiondicoding.data.repository.StoryRepository
import com.febiarifin.storyappsubmissiondicoding.database.StoryDatabase

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val storyDatabase = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService(context)
        return StoryRepository(storyDatabase, apiService)
    }
}