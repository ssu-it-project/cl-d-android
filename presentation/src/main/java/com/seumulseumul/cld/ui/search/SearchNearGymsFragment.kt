package com.seumulseumul.cld.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.seumulseumul.cld.databinding.FragmentSearchGymBinding
import com.seumulseumul.cld.sharedpref.PrefData
import com.seumulseumul.cld.sharedpref.PrefKey
import com.seumulseumul.cld.ui.adapter.GymsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchNearGymsFragment: Fragment() {
    private var _binding: FragmentSearchGymBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by activityViewModels()

    private val adapter by lazy {
        GymsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchGymBinding.inflate(inflater, container, false)

        initView()
        initViewModelStream()
        viewModel.getClimbingGyms(
            PrefData.getString(PrefKey.authToken, ""),
            126.95061461817554,
            37.50299567151356,
            ""
        )

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViewModelStream() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.climbingGymsSharedFlow.collect {
                    if (it.climbingGyms.isNotEmpty()) adapter.submitList(it.climbingGyms.toMutableList())
                }
            }
        }
    }

    private fun initView() {
        binding.rvGyms.also {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = adapter
        }
    }
}