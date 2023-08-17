package com.seumulseumul.data.model.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseAuthToken(
    @SerializedName("jwt")
    val jwt: String,

    @SerializedName("refresh_token")
    val refreshToken: String
)
