package com.seumulseumul.data.repository.remote

import android.util.Log
import com.seumulseumul.data.mapper.RemoteMapper
import com.seumulseumul.data.model.remote.request.RequestAuthRefreshToken
import com.seumulseumul.data.model.remote.request.RequestSignIn
import com.seumulseumul.data.model.remote.request.RequestSignUp
import com.seumulseumul.data.remote.cldapi.ClDApi
import com.seumulseumul.domain.model.AuthToken
import com.seumulseumul.domain.model.ClimeRecords
import com.seumulseumul.domain.model.Gyms
import com.seumulseumul.domain.model.Term
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import java.io.IOException
import javax.inject.Inject

class ClDRemoteDataSourceImpl @Inject constructor(
    private val clDApi: ClDApi,
    private val remoteMapper: RemoteMapper
): ClDRemoteDataSource {

    override fun postAuthRefreshToken(
        refreshToken: RequestAuthRefreshToken
    ): Flow<AuthToken> = flow {
        try {
            val response = clDApi.postAuthRefreshToken(refreshToken)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(remoteMapper.mapperToAuthToken(it))
                }
            } else {
                try {
                    Log.d("ClDRemoteDataSourceImpl", "[postAuthRefreshToken] fail: ${response.errorBody()!!.string()}")
                    //emit(ApiState.Error(response.errorBody()!!.string()))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            Log.d("ClDRemoteDataSourceImpl", "[postAuthRefreshToken] fail: $e")
            //emit(ApiState.Error(e.message ?: ""))
        } as Unit
    }

    override fun postSignIn(
        signIn: RequestSignIn
    ): Flow<AuthToken> = flow {
        try {
            val response = clDApi.postSignIn(signIn)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(remoteMapper.mapperToAuthToken(it))
                }
            } else {
                try {
                    Log.d("ClDRemoteDataSourceImpl", "[postSignIn] fail: ${response.errorBody()!!.string()}")
                    //emit(ApiState.Error(response.errorBody()!!.string()))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            Log.d("ClDRemoteDataSourceImpl", "[postSignIn] fail: $e")
            //emit(ApiState.Error(e.message ?: ""))
        } as Unit
    }

    override fun postSignUp(
        signUp: RequestSignUp
    ): Flow<AuthToken> = flow {
        try {
            val response = clDApi.postSignUp(signUp)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(remoteMapper.mapperToAuthToken(it))
                }
            } else {
                try {
                    Log.d("ClDRemoteDataSourceImpl", "[postSignUp] fail: ${response.errorBody()!!.string()}")
                    //emit(ApiState.Error(response.errorBody()!!.string()))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            Log.d("ClDRemoteDataSourceImpl", "[postSignUp] fail: $e")
            //emit(ApiState.Error(e.message ?: ""))
        } as Unit
    }

    override fun getClimbingGyms(
        auth: String,
        limit: Int,
        skip: Int,
        keyword: String
    ): Flow<Gyms> = flow {
        try {
            val response = clDApi.getClimbingGyms(auth, limit, skip, keyword)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(remoteMapper.mapperToGyms(it))
                }
            } else {
                try {
                    Log.d("ClDRemoteDataSourceImpl", "[getClimbingGyms] fail: ${response.errorBody()!!.string()}")
                    //emit(ApiState.Error(response.errorBody()!!.string()))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            Log.d("ClDRemoteDataSourceImpl", "[getClimbingGyms] fail: $e")
            //emit(ApiState.Error(e.message ?: ""))
        } as Unit
    }

    override fun postClimeRecord(
        auth: String,
        climbingGymId: MultipartBody.Part,
        content: MultipartBody.Part,
        sector: MultipartBody.Part,
        level: MultipartBody.Part,
        video: MultipartBody.Part
    ): Flow<Any> = flow {
        try {
            val response = clDApi.postClimeRecord(auth, climbingGymId, content, sector, level, video)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(it)
                }
            } else {
                try {
                    Log.d("ClDRemoteDataSourceImpl", "[postClimeRecord] fail: ${response.errorBody()!!.string()}")
                    //emit(ApiState.Error(response.errorBody()!!.string()))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            Log.d("ClDRemoteDataSourceImpl", "[postClimeRecord] fail: $e")
            //emit(ApiState.Error(e.message ?: ""))
        } as Unit
    }

    override fun getClimeRecord(
        auth: String,
        limit: Int,
        skip: Int
    ): Flow<ClimeRecords> = flow {
        try {
            val response = clDApi.getClimeRecord(auth, limit, skip)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(remoteMapper.mapperToClimeRecords(it))
                }
            } else {
                try {
                    Log.d("ClDRemoteDataSourceImpl", "[getClimeRecord] fail: ${response.errorBody()!!.string()}")
                    //emit(ApiState.Error(response.errorBody()!!.string()))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            Log.d("ClDRemoteDataSourceImpl", "[getClimeRecord] fail: $e")
            //emit(ApiState.Error(e.message ?: ""))
        } as Unit
    }

    override fun getTerms(): Flow<List<Term>> = flow {
        try {
            val response = clDApi.getTerms()
            if (response.isSuccessful) {
                response.body()?.let {
                    val termList = mutableListOf<Term>()
                    it.terms.forEach { term ->
                        termList.add(remoteMapper.mapperToTerm(term))
                    }
                    emit(termList)
                }
            } else {
                try {
                    Log.d("ClDRemoteDataSourceImpl", "[getTerms] fail: ${response.errorBody()!!.string()}")
                    //emit(ApiState.Error(response.errorBody()!!.string()))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            Log.d("ClDRemoteDataSourceImpl", "[getTerms] fail: $e")
            //emit(ApiState.Error(e.message ?: ""))
        } as Unit
    }
}