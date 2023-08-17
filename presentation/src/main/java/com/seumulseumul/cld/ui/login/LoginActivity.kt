package com.seumulseumul.cld.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.seumulseumul.cld.databinding.ActivityLoginBinding
import com.seumulseumul.cld.sharedpref.PrefData
import com.seumulseumul.cld.sharedpref.PrefKey
import com.seumulseumul.cld.ui.MainActivity
import com.seumulseumul.cld.ui.signup.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        if (PrefData.getBoolean(PrefKey.isLogin, false)) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.layoutKakaoLogin.setOnClickListener {
            loginWithKakao()
            //startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }
    }

    private fun loginWithKakao() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("LoginActivity", "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i("LoginActivity", "카카오계정으로 로그인 성공 ${token.accessToken}")
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e("LoginActivity", "카카오톡으로 로그인 실패", error)

                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    Log.i("LoginActivity", "카카오톡으로 로그인 성공 ${token.accessToken}")
                    startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("LoginActivity", "사용자 정보 요청 실패 $error")
            } else if (user != null) {
                Log.e("LoginActivity", "사용자 정보 요청 성공 : $user")
            }
        }
    }
}