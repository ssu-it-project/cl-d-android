package com.seumulseumul.cld.ui.term

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.seumulseumul.cld.databinding.ActivityTermDetailBinding

class TermDetailActivity: AppCompatActivity() {
    companion object {
        private val TAG: String = TermDetailActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityTermDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra("url")
        val termName = intent.getStringExtra("termName")

        binding.termsName = termName

        binding.clBack.setOnClickListener {
            finish()
        }

        with(binding.wvTermDetial) {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
        }

        if (url != null) binding.wvTermDetial.loadUrl(url) // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작
    }
}