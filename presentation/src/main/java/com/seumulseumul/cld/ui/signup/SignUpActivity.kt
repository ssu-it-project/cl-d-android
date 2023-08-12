package com.seumulseumul.cld.ui.signup

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.seumulseumul.cld.databinding.ActivitySignUpBinding
import com.seumulseumul.cld.sharedpref.PrefData
import com.seumulseumul.cld.sharedpref.PrefKey
import com.seumulseumul.cld.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener{
            finish()
        }

        binding.btnStart.setOnClickListener {
            if (binding.cbAgreeAll.isChecked) {
                PrefData.put(true to PrefKey.isLogin)

                val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    this@SignUpActivity,
                    "약관에 동의해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}