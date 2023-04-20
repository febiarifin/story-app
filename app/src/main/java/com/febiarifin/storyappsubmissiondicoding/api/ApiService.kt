package com.febiarifin.storyappsubmissiondicoding.api

import com.febiarifin.storyappsubmissiondicoding.data.StoryResponse
import com.febiarifin.storyappsubmissiondicoding.data.model.Story
import com.febiarifin.storyappsubmissiondicoding.data.response.DetailStoryResponse
import com.febiarifin.storyappsubmissiondicoding.data.response.StoryUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("stories")
    fun getStories(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int = 0,
    ): Call<StoryResponse>

    @GET("stories/{id}")
    fun getStoryDetail(
        @Path("id") id: String
    ): Call<DetailStoryResponse>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<StoryUploadResponse>
}