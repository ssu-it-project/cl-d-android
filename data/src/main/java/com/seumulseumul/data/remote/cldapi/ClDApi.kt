package com.seumulseumul.data.remote.cldapi

import com.seumulseumul.data.model.remote.request.RequestAuthRefreshToken
import com.seumulseumul.data.model.remote.request.RequestSignIn
import com.seumulseumul.data.model.remote.request.RequestSignUp
import com.seumulseumul.data.model.remote.response.ResponseAuthToken
import com.seumulseumul.data.model.remote.response.ResponseClimeGyms
import com.seumulseumul.data.model.remote.response.ResponseClimeRecords
import com.seumulseumul.data.model.remote.response.ResponseTerms
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ClDApi {
    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST(BaseUrl.CL_D_API_POST_REFRESH_TOKEN)
    suspend fun postAuthRefreshToken(
        @Body authRefreshToken: RequestAuthRefreshToken
    ): Response<ResponseAuthToken>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST(BaseUrl.CL_D_API_POST_SIGN_IN)
    suspend fun postSignIn(
        @Body signIn: RequestSignIn
    ): Response<ResponseAuthToken>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST(BaseUrl.CL_D_API_GET_POST_USER)
    suspend fun postSignUp(
        @Body signUp: RequestSignUp
    ): Response<ResponseAuthToken>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET(BaseUrl.CL_D_API_GET_GYMS)
    suspend fun getClimbingGyms(
        @Header("Authorization") auth: String,
        @Query("limit") limit: Int,
        @Query("skip") skip: Int,
        @Query("keyword") keyword: String
    ): Response<ResponseClimeGyms>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @Multipart
    @POST(BaseUrl.CL_D_API_GET_POST_RECORD)
    suspend fun postClimeRecord(
        @Header("Authorization") auth: String,
        @Part climbingGymId: MultipartBody.Part,
        @Part content: MultipartBody.Part,
        @Part sector: MultipartBody.Part,
        @Part level: MultipartBody.Part,
        @Part video: MultipartBody.Part
    ): Response<Any>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET(BaseUrl.CL_D_API_GET_POST_RECORD)
    suspend fun getClimeRecord(
        @Header("Authorization") auth: String,
        @Query("limit") limit: Int,
        @Query("skip") skip: Int,
    ): Response<ResponseClimeRecords>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET(BaseUrl.CL_D_API_GET_TERMS)
    suspend fun getTerms(): Response<ResponseTerms>
}