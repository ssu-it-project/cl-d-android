package com.seumulseumul.data.model.remote.request

import com.google.gson.annotations.SerializedName

data class RequestSignIn(
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("device")
    val device: Device,

    @SerializedName("login_type")
    val loginType: String
)

data class Device(
    @SerializedName("device_id")
    val deviceId: String,

    @SerializedName("device_info")
    val deviceInfo: String
)
