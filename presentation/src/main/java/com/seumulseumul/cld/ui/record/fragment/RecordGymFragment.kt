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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.seumulseumul.cld.R
import com.seumulseumul.cld.databinding.FragmentRecordGymBinding
import com.seumulseumul.cld.sharedpref.PrefData
import com.seumulseumul.cld.sharedpref.PrefKey
import com.seumulseumul.cld.ui.adapter.RecordGymAdapter
import com.seumulseumul.cld.ui.record.OnNextButtonClick
import com.seumulseumul.domain.model.ClimbingGym
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

@AndroidEntryPoint
class RecordGymFragment(
    private val nextButtonClickListener: OnNextButtonClick
): Fragment() {
    private var _binding: FragmentRecordGymBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecordGymViewModel by activityViewModels()

    private val nextButtonClickListenerToAdapter by lazy {
        object : OnNextButtonClick {
            override fun onNextButtonClick(data: Any) {
                val itemData = data as ClimbingGym
                binding.etRecordGym.setText(itemData.place.name)
                nextButtonClickListener.onNextButtonClick(itemData)
            }
        }
    }

    private val adapter by lazy {
        RecordGymAdapter(nextButtonClickListenerToAdapter)
    }

    private var timer: Timer = Timer()

    private var isBtnEnable = false

    private var currentLat = 37.49609318770147
    private var currentLon = 126.95428024925512

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordGymBinding.inflate(inflater, container, false)

        initView()
        initViewModelStream()

        return binding.root
    }

    private fun initView() {
        binding.etRecordGym.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                timer.cancel()
            }

            override fun afterTextChanged(p0: Editable?) {
                timer = Timer()
                timer.schedule(object: TimerTask() {
                    override fun run() {
                        Handler(Looper.getMainLooper()).post {
                            viewModel.getClimbingGyms(
                                PrefData.getString(PrefKey.authToken, ""),
                                currentLon,
                                currentLat,
                                p0.toString()
                            )
                        }
                    }
                }, 300)
                if (p0.toString().isNotEmpty()) {
                    isBtnEnable = true
                    binding.btnRecordGymNext.background = ContextCompat.getDrawable(requireContext(), R.drawable.start_button_enabled)
                } else {
                    isBtnEnable = false
                    binding.btnRecordGymNext.background = ContextCompat.getDrawable(requireContext(), R.drawable.start_button_disabled)
                }
            }
        })

        binding.rvRecordGymList.also {
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            it.adapter = adapter
        }

        /*binding.btnRecordGymNext.setOnClickListener{
            if (isBtnEnable) nextButtonClickListener.onNextButtonClick()
        }*/
    }

    private fun initViewModelStream() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.climbingGymsSharedFlow.collect {
                    if (it.climbingGyms.isNotEmpty()) {
                        binding.rvRecordGymList.visibility = View.VISIBLE
                        adapter.submitList(it.climbingGyms.toMutableList())
                    } else {
                        binding.rvRecordGymList.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

}