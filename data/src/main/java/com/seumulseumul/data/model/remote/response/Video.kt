package com.seumulseumul.data.model.remote.response

import com.google.gson.annotations.SerializedName

data class Video(
    @SerializedName("original")
    val original: String,

    @SerializedName("video_480")
    val video480: String?,

    @SerializedName("resolution")
    val resolution: String
)