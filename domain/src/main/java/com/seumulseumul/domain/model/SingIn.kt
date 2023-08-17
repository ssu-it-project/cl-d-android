package com.seumulseumul.domain.model

data class SignIn(
    val accessToken: String,

    val device: Device,

    val loginType: String
)
