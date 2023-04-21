package com.febiarifin.storyappsubmissiondicoding.ui.auth.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.febiarifin.storyappsubmissiondicoding.api.ApiConfig
import com.febiarifin.storyappsubmissiondicoding.data.model.Login
import com.febiarifin.storyappsubmissiondicoding.data.response.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel: ViewModel() {

    companion object{
        const val LOGIN_FAILURE = "Invalid email or password"
    }

    val userLogin = MutableLiveData<Login>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    fun login(context: Context, email: String, password: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService(context).login(email, password)
        client.enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    _isLoading.value = false
                    val login = Login()
                    login.name = response.body()?.loginResult?.name
                    login.userId = response.body()?.loginResult?.token
                    login.token = response.body()?.loginResult?.token
                    userLogin.postValue(login)
                }else{
                    _isLoading.value = false
                    _message.value = LOGIN_FAILURE
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message
            }
        })
    }

    fun getUserLogin(): LiveData<Login>{
        return userLogin
    }
}