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
import androidx.recyclerview.widget.RecyclerView
import com.seumulseumul.cld.databinding.FragmentSearchGymBinding
import com.seumulseumul.cld.sharedpref.PrefData
import com.seumulseumul.cld.sharedpref.PrefKey
import com.seumulseumul.cld.ui.adapter.GymsAdapter
import com.seumulseumul.domain.model.ClimbingGym
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFavoriteGymsFragment: Fragment() {
    private var _binding: FragmentSearchGymBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by activityViewModels()

    private val adapter by lazy {
        GymsAdapter()
    }
    private val layoutManager by lazy {
        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
    private var page = 0
    private val limit = 10
    private var isRequired: Boolean = true
    private val climbingGyms: MutableList<ClimbingGym> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchGymBinding.inflate(inflater, container, false)

        initView()
        initViewModelStream()
        viewModel.getClimbingGymsBookmark(
            PrefData.getString(PrefKey.authToken, ""),
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
                viewModel.climbingGymsBookmarkSharedFlow.collect {
                    if (it.climbingGyms.isNotEmpty()) {
                        climbingGyms.addAll(it.climbingGyms)
                        adapter.submitList(climbingGyms.toMutableList())
                    }
                }
            }
        }

        viewModel.searchKeywordLiveData.observe(viewLifecycleOwner) {
            climbingGyms.clear()
            page = 0
            binding.rvGyms.smoothScrollToPosition(0)
            viewModel.getClimbingGymsBookmark(
                PrefData.getString(PrefKey.authToken, ""),
                it
            )
        }
    }

    private fun initView() {
        binding.rvGyms.also {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = adapter
            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    // 스크롤 시, 페이징 연속 적용 방지 처리
                    if (layoutManager.findLastVisibleItemPosition() > adapter.itemCount - 4 && isRequired) {
                        val searchKeyword = if (viewModel.searchKeywordLiveData.value.isNullOrEmpty()) ""
                        else viewModel.searchKeywordLiveData.value!!

                        viewModel.getClimbingGymsBookmark(
                            PrefData.getString(PrefKey.authToken, ""),
                            searchKeyword,
                            skip = ++page * limit
                        )
                        isRequired = false
                    } else if (layoutManager.findLastVisibleItemPosition() < adapter.itemCount - 4) {
                        isRequired = true
                    }
                }
            })
        }
    }
}