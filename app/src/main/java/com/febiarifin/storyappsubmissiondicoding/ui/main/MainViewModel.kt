package com.febiarifin.storyappsubmissiondicoding.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.febiarifin.storyappsubmissiondicoding.api.ApiConfig
import com.febiarifin.storyappsubmissiondicoding.data.StoryResponse
import com.febiarifin.storyappsubmissiondicoding.data.model.Story
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {
    companion object{
        private const val TAG = "MainViewModel"
    }

    val listStories = MutableLiveData<ArrayList<Story>>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun setStories() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getStories()
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                if (response.isSuccessful){
                    _isError.value = false
                    _isLoading.value = false
                    if (response != null){
                        listStories.postValue(response.body()?.listStories)
                    }
                }else{
                    _isLoading.value = false
                    _isError.value = true
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

    fun getStories(): LiveData<ArrayList<Story>>{
        return listStories
    }
}