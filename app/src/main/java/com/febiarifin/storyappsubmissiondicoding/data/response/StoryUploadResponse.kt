package com.febiarifin.storyappsubmissiondicoding.data.response

import com.google.gson.annotations.SerializedName

data class StoryUploadResponse(
    @field:SerializedName("error")
    var error: Boolean,

    @field:SerializedName("message")
    var message: String
)
