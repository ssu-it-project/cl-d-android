package com.seumulseumul.data.remote

import com.seumulseumul.data.BuildConfig
import com.seumulseumul.data.remote.cldapi.ClDApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Singleton
    @Provides
    fun getClDApiRetrofit(): ClDApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.CL_D_SERVER_BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(ClDApi::class.java)
    }
}