package com.seumulseumul.domain.model

data class SignUp(
    val agreements: List<Agreement>,

    val auth: Auth,

    val profile: Profile
)

data class Agreement(
    val agreed: Boolean,

    val id: String,

    val timestamp: String
)

data class Auth(
    val accessToken: String,

    val device: Device,

    val loginType: String
)

data class Profile(
    val birthday:  String,

    val gender: Int,

    val image: String,

    val name: String,

    val nickname: String,
)