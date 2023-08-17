package com.seumulseumul.data.model.remote.response

import com.google.gson.annotations.SerializedName

data class Date(
    @SerializedName("created")
    val created: String,

    @SerializedName("modified")
    val modified: String
)