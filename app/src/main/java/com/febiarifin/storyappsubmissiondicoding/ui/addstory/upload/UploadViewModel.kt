package com.febiarifin.storyappsubmissiondicoding.ui.addstory.upload

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.febiarifin.storyappsubmissiondicoding.api.ApiConfig
import com.febiarifin.storyappsubmissiondicoding.data.response.StoryUploadResponse
import com.febiarifin.storyappsubmissiondicoding.ui.main.MainActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class UploadViewModel:ViewModel() {

    companion object{
        private const val MAXIMAL_SIZE = 1000000
    }

    val _imageFromCamera = MutableLiveData<File?>()
    val imageFromCamera: LiveData<File?> = _imageFromCamera

    val _imageFromGallery = MutableLiveData<Uri?>()
    val imageFromGallery: LiveData<Uri?> = _imageFromGallery

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun uploadImage(context: Context,file: File?, description: String) {
        _isLoading.value = true
        if (file == null) {
            _isLoading.value = false
            _message.value = "Please capture or select image!"
        }else if(description.isEmpty()){
            _isLoading.value = false
            _isError.value = true
        } else {
            val reducedFile = reduceFileImage(file)

            val description = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = reducedFile.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                reducedFile.name,
                requestImageFile
            )
            val client = ApiConfig.getApiService(context ).uploadImage(imageMultipart, description)
            client.enqueue(object : Callback<StoryUploadResponse> {
                override fun onResponse(
                    call: Call<StoryUploadResponse>,
                    response: Response<StoryUploadResponse>
                ) {
                    if (response.isSuccessful) {
                        _isLoading.value = false
                        _isError.value = false
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            _message.value = responseBody.message
                        }
                    } else {
                        _isLoading.value = false
                        _isError.value = false
                        _message.value = response.message()
                    }
                }

                override fun onFailure(call: Call<StoryUploadResponse>, t: Throwable) {
                    _isLoading.value = false
                    _isError.value = false
                    _message.value = t.message
                }
            })
        }
    }

    fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > MAXIMAL_SIZE)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    fun setImageFormCamera(file: File){
        _imageFromCamera.value = file
    }

    fun setImageFormGallery(uri: Uri){
        _imageFromGallery.value = uri
    }
}