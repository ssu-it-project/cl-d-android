package com.seumulseumul.cld.ui.record.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.seumulseumul.cld.R
import com.seumulseumul.cld.databinding.FragmentRecordLevelBinding
import com.seumulseumul.cld.ui.adapter.RecordLevelAdapter
import com.seumulseumul.cld.ui.record.OnNextButtonClick
import com.seumulseumul.domain.model.ClimbingGym

class RecordLevelFragment(
    private val nextButtonClickListener: OnNextButtonClick
): Fragment() {
    private var _binding: FragmentRecordLevelBinding? = null
    private val binding get() = _binding!!

    private var levelData = ""

    private val levelArray = arrayOf("흰색", "회색", "검정", "파랑", "빨강", "갈색", "핑크", "초록", "보라", "주황", "노랑")
    private val nextButtonClickListenerToAdapter by lazy {
        object : OnNextButtonClick {
            override fun onNextButtonClick(data: Any) {
                levelData = data as String
                binding.btnRecordSectorNext.background = ContextCompat.getDrawable(requireContext(), R.drawable.start_button_enabled)
            }
        }
    }

    private val adapter by lazy {
        RecordLevelAdapter(nextButtonClickListenerToAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordLevelBinding.inflate(inflater, container, false)

        initView()

        return  binding.root
    }

    private fun initView() {
        adapter.submitList(levelArray.toMutableList())
        binding.rvLevel.also {
            it.layoutManager = GridLayoutManager(requireContext(), 4)
            it.adapter = adapter
        }

        binding.btnRecordSectorNext.setOnClickListener {
            if (levelData.isNotEmpty()) {
                nextButtonClickListener.onNextButtonClick(levelData)
            }
        }
    }
}