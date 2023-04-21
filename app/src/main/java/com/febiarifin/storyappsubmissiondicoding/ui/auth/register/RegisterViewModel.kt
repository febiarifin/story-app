package com.febiarifin.storyappsubmissiondicoding.ui.auth.register

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.febiarifin.storyappsubmissiondicoding.api.ApiConfig
import com.febiarifin.storyappsubmissiondicoding.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel: ViewModel() {

    companion object{
        const val SUCCESS_REGISTER = "Successfull Registration"
        const val FAILED_REGISTER = "Registration failed. Email is already taken"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    fun register(context: Context,name: String, email: String, password: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService(context).register(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful){
                    _isLoading.value = false
                    _message.value = SUCCESS_REGISTER
                    _isSuccess.value = true
                }else{
                    _isLoading.value = false
                    _message.value = FAILED_REGISTER
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = FAILED_REGISTER
            }
        })
    }

}