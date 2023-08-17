package com.seumulseumul.data.model.remote.request

import com.google.gson.annotations.SerializedName

data class RequestSingUp(
    @SerializedName("agreements")
    val agreements: List<Agreement>,

    @SerializedName("auth")
    val auth: RequestSignIn,

    @SerializedName("profile")
    val profile: Profile
)

data class Agreement(
    @SerializedName("agreed")
    val agreed: Boolean,

    @SerializedName("id")
    val id: String,

    @SerializedName("timestamp")
    val timestamp: String
)

data class Profile(
    @SerializedName("birthday")
    val birthday:  String,

    @SerializedName("gender")
    val gender: Int,

    @SerializedName("image")
    val image: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("nickname")
    val nickname: String,
)