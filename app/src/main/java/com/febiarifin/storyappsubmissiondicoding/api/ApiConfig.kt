package com.febiarifin.storyappsubmissiondicoding.api

import android.content.Context
import com.febiarifin.storyappsubmissiondicoding.utils.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {

    companion object{
        private lateinit var apiService: ApiService
        fun getApiService(context: Context): ApiService {
            if (!::apiService.isInitialized) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://story-api.dicoding.dev/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okhttpClient(context))
                    .build()

                apiService = retrofit.create(ApiService::class.java)
            }

            return apiService
        }

        private fun okhttpClient(context: Context): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context))
                .build()
        }
    }
}