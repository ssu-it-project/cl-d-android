package com.seumulseumul.data.model.remote.request

import com.google.gson.annotations.SerializedName

data class RequestSignUp(
    @SerializedName("agreements")
    val agreements: List<Agreement>,

    @SerializedName("auth")
    val auth: Auth,

    /*@SerializedName("profile")
    val profile: Profile*/
)

data class Agreement(
    @SerializedName("agreed")
    val agreed: Boolean,

    @SerializedName("id")
    val id: String,

    /*@SerializedName("timestamp")
    val timestamp: String*/
)

data class Auth(
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("device")
    val device: Device,

    @SerializedName("login_type")
    val loginType: String
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