package com.seumulseumul.domain.repository

import com.seumulseumul.domain.model.AuthToken
import com.seumulseumul.domain.model.ClimeRecords
import com.seumulseumul.domain.model.Gyms
import com.seumulseumul.domain.model.RefreshToken
import com.seumulseumul.domain.model.SignIn
import com.seumulseumul.domain.model.SignUp
import com.seumulseumul.domain.model.Term
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ClDRepository {
    fun postAuthRefreshToken(
        refreshToken: RefreshToken
    ): Flow<AuthToken>

    fun postSignIn(
        signIn: SignIn
    ): Flow<AuthToken>

    fun postSignUp(
        signUp: SignUp
    ): Flow<AuthToken>

    fun getClimbingGyms(
        auth: String,
        x: Double,
        y: Double,
        limit: Int,
        skip: Int,
        keyword: String
    ): Flow<Gyms>

    fun postClimeRecord(
        auth: String,
        climbingGymId: String,
        content: String,
        sector: String,
        level: String,
        video: File
    ): Flow<Any>

    fun getClimeRecord(
        auth: String,
        limit: Int,
        skip: Int
    ): Flow<ClimeRecords>

    fun getTerms(): Flow<List<Term>>

}