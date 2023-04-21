package com.febiarifin.storyappsubmissiondicoding.ui.detail

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.febiarifin.storyappsubmissiondicoding.api.ApiConfig
import com.febiarifin.storyappsubmissiondicoding.data.response.DetailStoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel: ViewModel() {
    companion object{
        private const val TAG = "DetailViewModel"
    }

    val story = MutableLiveData<DetailStoryResponse>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun setStoryDetail(context: Context,id: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService(context).getStoryDetail(id)
        client.enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(call: Call<DetailStoryResponse>, response: Response<DetailStoryResponse>) {
                if (response.isSuccessful){
                    _isLoading.value = false
                    if (response != null){
                        story.postValue(response.body())
                    }
                }else{
                    _isLoading.value = false
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message.toString()
                Log.d(TAG, "Failure: ${t.message}")
            }

        })
    }

    fun getStoryDetail(): LiveData<DetailStoryResponse>{
        return story
    }
}