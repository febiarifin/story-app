package com.febiarifin.storyappsubmissiondicoding.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.*
import com.febiarifin.storyappsubmissiondicoding.api.ApiConfig
import com.febiarifin.storyappsubmissiondicoding.api.ApiService
import com.febiarifin.storyappsubmissiondicoding.data.StoryResponse
import com.febiarifin.storyappsubmissiondicoding.data.model.Story
import com.febiarifin.storyappsubmissiondicoding.data.remotemediator.StoryRemoteMediator
import com.febiarifin.storyappsubmissiondicoding.data.repository.StoryRepository
import com.febiarifin.storyappsubmissiondicoding.data.response.StoryResponseItem
import com.febiarifin.storyappsubmissiondicoding.database.StoryDatabase
import com.febiarifin.storyappsubmissiondicoding.di.Injection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
) : ViewModel() {

//    companion object{
//        private const val TAG = "MainViewModel"
//    }

//    val listStories = MutableLiveData<ArrayList<Story>>()
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    private val _message = MutableLiveData<String>()
//    val message: LiveData<String> = _message

//    fun setStories(context: Context) {
//        _isLoading.value = true
//        val client = ApiConfig.getApiService(context).getStories()
//        client.enqueue(object : Callback<StoryResponse> {
//            override fun onResponse(
//                call: Call<StoryResponse>,
//                response: Response<StoryResponse>
//            ) {
//                if (response.isSuccessful){
//                    _isLoading.value = false
//                    if (response != null){
//                        listStories.postValue(response.body()?.listStories)
//                    }
//                }else{
//                    _isLoading.value = false
//                    _message.value = response.message()
//                }
//            }
//
//            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
//                _isLoading.value = false
//                _message.value = t.message
//                Log.d(TAG,"Failure: ${t.message}")
//            }
//        })
//    }

//    fun getStories(): LiveData<ArrayList<Story>>{
//        return listStories
//    }

    val story: LiveData<PagingData<StoryResponseItem>> by lazy{
        getData().cachedIn(viewModelScope)
    }

    private fun getData(): LiveData<PagingData<StoryResponseItem>>{
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            },
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService)
        ).liveData
    }
}

class ViewModelFactory(private val storyDatabase: StoryDatabase, private val apiService: ApiService) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(storyDatabase, apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

//class MainViewModel(storyRepository: StoryRepository) : ViewModel() {
//
//    val story: LiveData<PagingData<StoryResponseItem>> =
//        storyRepository.getStory().cachedIn(viewModelScope)
//
//}
//
//class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return MainViewModel(Injection.provideRepository(context)) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}