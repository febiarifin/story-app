package com.febiarifin.storyappsubmissiondicoding.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Login(
    var name: String? = null,
    var userId: String? = null,
    var token: String? = null,
): Parcelable