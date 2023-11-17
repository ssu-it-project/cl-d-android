package com.seumulseumul.cld.ui.record.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.seumulseumul.cld.R
import com.seumulseumul.cld.databinding.FragmentRecordSectorBinding
import com.seumulseumul.cld.sharedpref.PrefData
import com.seumulseumul.cld.sharedpref.PrefKey
import com.seumulseumul.cld.ui.record.OnNextButtonClick
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import java.util.TimerTask

@AndroidEntryPoint
class RecordSectorFragment(
    private val nextButtonClickListener: OnNextButtonClick
): Fragment() {
    private var _binding: FragmentRecordSectorBinding? = null
    private val binding get() = _binding!!

    private var isBtnEnable = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordSectorBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    private fun initView() {
        binding.etRecordSector.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty()) {
                    isBtnEnable = true
                    binding.btnRecordSectorNext.background = ContextCompat.getDrawable(requireContext(), R.drawable.start_button_enabled)

                } else {
                    isBtnEnable = false
                    binding.btnRecordSectorNext.background = ContextCompat.getDrawable(requireContext(), R.drawable.start_button_disabled)
                }
            }
        })

        binding.btnRecordSectorNext.setOnClickListener{
            if (isBtnEnable) nextButtonClickListener.onNextButtonClick(binding.etRecordSector.text.toString())
        }
    }
}