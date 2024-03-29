package com.febiarifin.storyappsubmissiondicoding.ui.maps

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.febiarifin.storyappsubmissiondicoding.api.ApiConfig
import com.febiarifin.storyappsubmissiondicoding.data.StoryResponse
import com.febiarifin.storyappsubmissiondicoding.data.model.Story
import com.febiarifin.storyappsubmissiondicoding.data.response.StoryResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel: ViewModel() {

    companion object{
        private const val TAG = "MapsActivity"
        private const val PAGE_OF_STORY = 1
        private const val SIZE_OF_STORY = 10
        private const val LOCATION_OF_STORY = 1
    }

    val listStories = MutableLiveData<List<StoryResponseItem>>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun setStories(context: Context) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(context).getStories(PAGE_OF_STORY, SIZE_OF_STORY,
            LOCATION_OF_STORY)
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                if (response.isSuccessful){
                    _isLoading.value = false
                    if (response != null){
                        listStories.postValue(response.body()?.listStories)
                    }
                }else{
                    _isLoading.value = false
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message
                Log.d(TAG,"Failure: ${t.message}")
            }
        })
    }

    fun getStories(): LiveData<List<StoryResponseItem>> {
        return listStories
    }
}