package com.seumulseumul.data.repository.remote

import com.seumulseumul.data.model.remote.request.RequestAuthRefreshToken
import com.seumulseumul.data.model.remote.request.RequestSignIn
import com.seumulseumul.data.model.remote.request.RequestSignUp
import com.seumulseumul.domain.model.AuthToken
import com.seumulseumul.domain.model.ClimeRecords
import com.seumulseumul.domain.model.Gyms
import com.seumulseumul.domain.model.Term
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface ClDRemoteDataSource {
    fun postAuthRefreshToken(
        refreshToken: RequestAuthRefreshToken
    ): Flow<AuthToken>

    fun postSignIn(
        signIn: RequestSignIn
    ): Flow<AuthToken>

    fun postSignUp(
        signUp: RequestSignUp
    ): Flow<AuthToken>

    fun getClimbingGyms(
        auth: String,
        limit: Int,
        skip: Int,
        keyword: String
    ): Flow<Gyms>

    fun postClimeRecord(
        auth: String,
        climbingGymId: MultipartBody.Part,
        content: MultipartBody.Part,
        sector: MultipartBody.Part,
        level: MultipartBody.Part,
        video: MultipartBody.Part
    ): Flow<Any>

    fun getClimeRecord(
        auth: String,
        limit: Int,
        skip: Int
    ): Flow<ClimeRecords>

    fun getTerms(): Flow<List<Term>>
}