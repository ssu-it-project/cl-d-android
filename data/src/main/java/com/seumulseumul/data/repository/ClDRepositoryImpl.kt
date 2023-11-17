package com.seumulseumul.data.repository

import com.seumulseumul.data.mapper.RemoteMapper
import com.seumulseumul.data.repository.remote.ClDRemoteDataSource
import com.seumulseumul.data.util.FormDataUtil
import com.seumulseumul.domain.model.AuthToken
import com.seumulseumul.domain.model.ClimbingGym
import com.seumulseumul.domain.model.ClimeRecords
import com.seumulseumul.domain.model.Gyms
import com.seumulseumul.domain.model.RefreshToken
import com.seumulseumul.domain.model.SignIn
import com.seumulseumul.domain.model.SignUp
import com.seumulseumul.domain.model.Term
import com.seumulseumul.domain.repository.ClDRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class ClDRepositoryImpl @Inject constructor(
    private val clDRemoteDataSource: ClDRemoteDataSource,
    private val remoteMapper: RemoteMapper
): ClDRepository {

    override fun postAuthRefreshToken(refreshToken: RefreshToken): Flow<AuthToken> =
        clDRemoteDataSource.postAuthRefreshToken(
            remoteMapper.mapperToRequestAuthRefreshToken(refreshToken)
        ).flowOn(Dispatchers.IO)


    override fun postSignIn(signIn: SignIn): Flow<AuthToken> =
        clDRemoteDataSource.postSignIn(
            remoteMapper.mapperToRequestSignIn(signIn)
        ).flowOn(Dispatchers.IO)

    override fun postSignUp(signUp: SignUp): Flow<AuthToken> =
        clDRemoteDataSource.postSignUp(
            remoteMapper.mapperToRequestSignUp(signUp)
        ).flowOn(Dispatchers.IO)

    override fun getClimbingGyms(
        auth: String,
        x: Double,
        y: Double,
        limit: Int,
        skip: Int,
        keyword: String
    ): Flow<Gyms> =
        clDRemoteDataSource.getClimbingGyms(
            auth, x, y, limit, skip, keyword
        ).flowOn(Dispatchers.IO)

    override fun getClimbingGymDetail(
        auth: String,
        id: String
    ): Flow<ClimbingGym> =
        clDRemoteDataSource.getClimbingGymDetail(
            auth, id
        ).flowOn(Dispatchers.IO)

    override fun postClimbingGymBookmark(
        auth: String,
        id: String
    ): Flow<Any> =
        clDRemoteDataSource.postClimbingGymBookmark(
            auth, id
        ).flowOn(Dispatchers.IO)

    override fun deleteClimbingGymBookmark(
        auth: String,
        id: String
    ): Flow<Any> =
        clDRemoteDataSource.deleteClimbingGymBookmark(
        auth, id
        ).flowOn(Dispatchers.IO)

    override fun getClimbingGymBookmark(
        auth: String,
        keyword: String,
        limit: Int,
        skip: Int
    ): Flow<Gyms> =
        clDRemoteDataSource.getClimbingGymBookmark(
            auth, keyword, limit, skip
        ).flowOn(Dispatchers.IO)

    override fun getClimbingGymRecords(
        auth: String,
        id: String,
        keyword: String,
        limit: Int,
        skip: Int
    ): Flow<ClimeRecords> =
        clDRemoteDataSource.getClimbingGymRecords(
            auth, id, keyword, limit, skip
        ).flowOn(Dispatchers.IO)

    override fun postClimeRecord(
        auth: String,
        climbingGymId: String,
        content: String,
        sector: String,
        level: String,
        resolution: String,
        video: File,
    ): Flow<Any> =
        clDRemoteDataSource.postClimeRecord(
            auth,
            FormDataUtil.getBody("climbing_gym_id", climbingGymId),
            FormDataUtil.getBody("content", content),
            FormDataUtil.getBody("sector", sector),
            FormDataUtil.getBody("level", level),
            FormDataUtil.getBody("resolution", resolution),
            FormDataUtil.getVideoBody("video", video)
        ).flowOn(Dispatchers.IO)

    override fun getClimeRecord(
        auth: String,
        limit: Int,
        skip: Int
    ): Flow<ClimeRecords> =
        clDRemoteDataSource.getClimeRecord(
            auth, limit, skip
        ).flowOn(Dispatchers.IO)

    override fun getTerms(): Flow<List<Term>> =
        clDRemoteDataSource.getTerms().flowOn(Dispatchers.IO)
}