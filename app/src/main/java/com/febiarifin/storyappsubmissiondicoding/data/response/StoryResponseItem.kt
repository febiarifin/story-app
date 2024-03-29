package com.febiarifin.storyappsubmissiondicoding.data.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "story")
data class StoryResponseItem(

    @PrimaryKey
    val id: String,
    val name: String? = null,
    val photoUrl: String? = null,
    val createdAt: String? = null,
    val description: String? = null,
    val lat: Double? = null,
    val lon: Double? = null,
)
