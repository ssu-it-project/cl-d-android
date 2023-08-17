package com.seumulseumul.domain.model

data class AuthToken(
    val jwt: String,

    val refreshToken: String
)
