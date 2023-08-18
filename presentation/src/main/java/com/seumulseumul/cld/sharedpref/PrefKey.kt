package com.seumulseumul.cld.sharedpref

object PrefKey {

    // 로그인 여부: Boolean
    const val isLogin = "isLogin"

    // OAuth Login Type(ex. kakao, google..): String
    const val loginType = "loginType"

    // OAuth access token
    const val oAuthAccessToken = "oAuthAccessToken"

    // JWT Token: String
    const val authToken = "authToken"

    // Refresh Token: String,
    const val refreshToken = "refreshToken"
}