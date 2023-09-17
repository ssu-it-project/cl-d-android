package com.seumulseumul.data.repository.remote

import android.util.Log
import com.seumulseumul.data.mapper.RemoteMapper
import com.seumulseumul.data.model.remote.request.RequestAuthRefreshToken
import com.seumulseumul.data.model.remote.request.RequestSignIn
import com.seumulseumul.data.model.remote.request.RequestSignUp
import com.seumulseumul.data.remote.cldapi.ClDApi
import com.seumulseumul.domain.model.AuthToken
import com.seumulseumul.domain.model.ClimbingGym
import com.seumulseumul.domain.model.ClimeRecords
import com.seumulseumul.domain.model.Gyms
import com.seumulseumul.domain.model.Term
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import retrofit2.Response
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
                    //if (response.code() == 409 && response.errorBody()!!.string().contains("User already exists")) {
                    if (response.code() == 409) {
                        Log.d("ClDRemoteDataSourceImpl", "[postSignUp] fail User already exists")
                        val signIn = RequestSignIn(signUp.auth.accessToken, signUp.auth.device, signUp.auth.loginType)
                        postSignIn(signIn).flowOn(Dispatchers.IO).collect{
                            emit(it)
                        }
                    } else {
                        //emit(ApiState.Error(response.errorBody()!!.string()))
                    }
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
        x: Double,
        y: Double,
        limit: Int,
        skip: Int,
        keyword: String
    ): Flow<Gyms> = flow {
        try {
            val response = clDApi.getClimbingGyms(auth, x, y, limit, skip, keyword)
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

    override fun getClimbingGymDetail(
        auth: String,
        id: String
    ): Flow<ClimbingGym> = flow {
        try {
            val response = clDApi.getClimbingGymDetail(auth, id)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(remoteMapper.mapperToClimbingGym(it))
                }
            } else {
                try {
                    Log.d("ClDRemoteDataSourceImpl", "[getClimbingGymDetail] fail: ${response.errorBody()!!.string()}")
                    //emit(ApiState.Error(response.errorBody()!!.string()))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            Log.d("ClDRemoteDataSourceImpl", "[getClimbingGymDetail] fail: $e")
            //emit(ApiState.Error(e.message ?: ""))
        } as Unit
    }

    override fun postClimbingGymBookmark(
        auth: String,
        id: String
    ): Flow<Any> = flow {
        try {
            val response = clDApi.postClimbingGymBookmark(auth, id)
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

    override fun deleteClimbingGymBookmark(
        auth: String,
        id: String
    ): Flow<Any> = flow {
        try {
            val response = clDApi.deleteClimbingGymBookmark(auth, id)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(it)
                }
            } else {
                try {
                    Log.d("ClDRemoteDataSourceImpl", "[deleteClimbingGymBookmark] fail: ${response.errorBody()!!.string()}")
                    //emit(ApiState.Error(response.errorBody()!!.string()))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            Log.d("ClDRemoteDataSourceImpl", "[deleteClimbingGymBookmark] fail: $e")
            //emit(ApiState.Error(e.message ?: ""))
        } as Unit
    }

    override fun getClimbingGymBookmark(
        auth: String,
        keyword: String,
        limit: Int,
        skip: Int
    ): Flow<Gyms> = flow {
        try {
            val response = clDApi.getClimbingGymBookmark(auth, keyword, limit, skip)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(remoteMapper.mapperToGyms(it))
                }
            } else {
                try {
                    Log.d("ClDRemoteDataSourceImpl", "[getClimbingGymBookmark] fail: ${response.errorBody()!!.string()}")
                    //emit(ApiState.Error(response.errorBody()!!.string()))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            Log.d("ClDRemoteDataSourceImpl", "[getClimbingGymBookmark] fail: $e")
            //emit(ApiState.Error(e.message ?: ""))
        } as Unit
    }

    override fun getClimbingGymRecords(
        auth: String,
        id: String,
        keyword: String,
        limit: Int,
        skip: Int
    ): Flow<ClimeRecords> = flow {
        try {
            val response = clDApi.getClimbingGymRecords(auth, id, keyword, limit, skip)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(remoteMapper.mapperToClimeRecords(it))
                }
            } else {
                try {
                    Log.d("ClDRemoteDataSourceImpl", "[ge   tClimbingGymRecords] fail: ${response.errorBody()!!.string()}")
                    //emit(ApiState.Error(response.errorBody()!!.string()))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            Log.d("ClDRemoteDataSourceImpl", "[getClimbingGymRecords] fail: $e")
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