package com.seumulseumul.cld.ui.home

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
import com.seumulseumul.cld.databinding.FragmentHomeBinding
import com.seumulseumul.cld.sharedpref.PrefData
import com.seumulseumul.cld.sharedpref.PrefKey
import com.seumulseumul.cld.ui.adapter.FeedAdapter
import com.seumulseumul.domain.model.ClimeRecords
import com.seumulseumul.domain.model.Record
import com.seumulseumul.domain.model.Term
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()

    private val adapter by lazy {
        FeedAdapter()
    }
    private var page = 0
    private val limit = 5
    private var isRequired: Boolean = true
    private val feedList: MutableList<Record> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initView()

        initViewModelStream()
        viewModel.getClimeRecords(
            PrefData.getString(PrefKey.authToken, "")
        )

        return root
    }

    private fun initViewModelStream() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recordsSharedFlow.collect {
                    if (it.records.isNotEmpty()) {
                        feedList.addAll(it.records)
                        adapter.submitList(feedList.toMutableList())
                    }
                }
            }
        }
    }

    private fun initView() {
        binding.rvFeed.also {
            val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            it.layoutManager = layoutManager
            it.adapter = adapter
            /*it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    // 스크롤 시, 페이징 연속 적용 방지 처리
                    if (layoutManager.findLastVisibleItemPosition() > adapter.itemCount - 2 && isRequired) {
                        viewModel.getClimeRecords(
                            PrefData.getString(PrefKey.authToken, ""),
                            skip = ++page * limit
                        )
                        isRequired = false
                    } else if (layoutManager.findLastVisibleItemPosition() < adapter.itemCount - 2) {
                        isRequired = true
                    }
                }
            })*/
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}