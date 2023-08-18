package com.seumulseumul.cld.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.seumulseumul.cld.R
import com.seumulseumul.cld.databinding.ActivitySignUpBinding
import com.seumulseumul.cld.sharedpref.PrefData
import com.seumulseumul.cld.sharedpref.PrefKey
import com.seumulseumul.cld.ui.MainActivity
import com.seumulseumul.cld.ui.adapter.TermsAdapter
import com.seumulseumul.cld.ui.term.TermDetailActivity
import com.seumulseumul.domain.model.Term
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    private val signUpViewModel: SignUpViewModel by viewModels()

    lateinit var termsList: MutableList<Term>
    private var isAllCheck = false

    private val onCheckAgreeClickListener = object : OnItemClickListener {
        override fun onItemClick(term: Term) {
            for (index in termsList.indices) {
                if (term.id == termsList[index].id) {
                    termsList[index].agreed = !termsList[index].agreed
                    break
                }
            }
            isAllAgree()
        }
    }
    private val onSendCallClickListener = object : OnItemClickListener {
        override fun onItemClick(term: Term) {
            // webview로 약관상세보기
            val intent = Intent(this@SignUpActivity, TermDetailActivity::class.java)
            val url = term.pageUrl
            val termName = term.name
            intent.putExtra("url", url)
            intent.putExtra("termName", termName)
            startActivity(intent)
        }
    }
    private val adapter by lazy {
        TermsAdapter(onCheckAgreeClickListener, onSendCallClickListener)
    }
    private val layoutManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signUpViewModel.getTerms()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                signUpViewModel.termsSharedFlow.collect{
                    termsList = it.toMutableList()
                    if (it.isNotEmpty()) adapter.submitList(termsList)
                }
            }
        }

        binding.rvTerms.also {
            it.layoutManager = layoutManager
            it.adapter = adapter
        }

        binding.cbAgreeAll.setOnClickListener {
            for (index in termsList.indices) {
                termsList[index].agreed = binding.cbAgreeAll.isChecked
            }
            adapter.submitList(termsList)
            adapter.notifyDataSetChanged()
            isAllAgree()
        }

        binding.ivBack.setOnClickListener{
            finish()
        }

        binding.btnStart.setOnClickListener {
            if (isAllCheck) {
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

    private fun isAllAgree() {
        for (term in termsList) {
            if (!term.agreed) {
                binding.btnStart.setBackgroundResource(R.drawable.start_button_disabled)
                binding.cbAgreeAll.isChecked = false
                isAllCheck = false
                return
            }
        }

        binding.cbAgreeAll.isChecked = true
        binding.btnStart.setBackgroundResource(R.drawable.start_button_enabled)
        isAllCheck = true
    }
}

interface OnItemClickListener {
    fun onItemClick(term: Term)
}