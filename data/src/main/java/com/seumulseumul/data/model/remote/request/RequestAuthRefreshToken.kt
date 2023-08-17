package com.seumulseumul.data.model.remote.request

import com.google.gson.annotations.SerializedName

data class RequestAuthRefreshToken(
    @SerializedName("device_id")
    val deviceId: String,

    @SerializedName("refresh_token")
    val refreshToken: String
)
