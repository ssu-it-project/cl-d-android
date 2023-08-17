package com.seumulseumul.data.remote.cldapi

import com.seumulseumul.data.model.remote.request.RequestAuthRefreshToken
import com.seumulseumul.data.model.remote.request.RequestSignIn
import com.seumulseumul.data.model.remote.request.RequestSingUp
import com.seumulseumul.data.model.remote.response.ResponseAuthToken
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ClDApi {
    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST(BaseUrl.CL_D_API_POST_REFRESH_TOKEN)
    fun postAuthRefreshToken(
        @Body authRefreshToken: RequestAuthRefreshToken
    ): Single<ResponseAuthToken>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST(BaseUrl.CL_D_API_POST_SIGN_IN)
    fun postSignIn(
        @Body signIn: RequestSignIn
    ): Single<ResponseAuthToken>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST(BaseUrl.CL_D_API_GET_POST_USER)
    fun postSignUp(
        @Body signUp: RequestSingUp
    ): Single<ResponseAuthToken>
}