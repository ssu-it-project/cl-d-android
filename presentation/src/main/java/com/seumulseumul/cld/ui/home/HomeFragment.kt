package com.seumulseumul.cld.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.seumulseumul.cld.databinding.FragmentHomeBinding
import com.seumulseumul.cld.sharedpref.PrefData
import com.seumulseumul.cld.sharedpref.PrefKey
import com.seumulseumul.cld.ui.adapter.FeedAdapter
import com.seumulseumul.cld.ui.adapter.TermsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by activityViewModels()

    private val adapter by lazy {
        FeedAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initView()

        initViewModelStream()
        homeViewModel.getClimeRecords(
            PrefData.getString(PrefKey.authToken, "")
        )

        return root
    }

    private fun initViewModelStream() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.recordsSharedFlow.collect {
                    if (it.records.isNotEmpty()) adapter.submitList(it.records.toMutableList())
                }
            }
        }
    }

    private fun initView() {
        binding.rvFeed.also {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}