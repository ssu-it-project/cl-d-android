package com.seumulseumul.cld.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }
    }
}