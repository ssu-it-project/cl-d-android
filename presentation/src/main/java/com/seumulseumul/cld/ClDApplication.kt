package com.seumulseumul.cld

import android.app.Application
import android.content.Context
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ClDApplication: Application() {
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)

        /*var keyHash = Utility.getKeyHash(this)
        Log.d("TESTLOG", "keyhash : $keyHash")*/
    }

    companion object {
        lateinit var instance: ClDApplication
        fun applicationContext(): Context {
            return instance.applicationContext
        }
    }
}