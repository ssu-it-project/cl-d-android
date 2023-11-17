package com.seumulseumul.cld.ui.home

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.view.WindowMetrics
import androidx.core.view.ViewCompat
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
import com.seumulseumul.domain.model.Record
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

    private val displayMetrics = DisplayMetrics()

    private fun getVideoStartPosition(recyclerView: RecyclerView): Int {
        // 첫 번째 콘텐츠 취득
        val firstVisibleItemPosition =
            (recyclerView.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()

        // 마지막 콘텐츠의 취득
        val lastVisibleItemPosition =
            (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastVisibleItemPosition()

        // 콘텐츠의 크기에 따라 화면에 표시되는 콘텐츠의 수는 달라질 수 있다.
        if (lastVisibleItemPosition - firstVisibleItemPosition == 0) {
            return firstVisibleItemPosition
        }

        // 화면에 표시되는 처음 및 마지막 아이템의 position의 차이가 1 이상일 때, 표시할 콘텐츠 하나를 특정할 필요가 있다.
        var ratio = 0.0f
        var startPosition = firstVisibleItemPosition

        // 콘텐츠만큼 for문을 돈다.
        for (i in 0 until lastVisibleItemPosition - firstVisibleItemPosition + 1) {

            // position의 adapter를 얻는다
            val holder = recyclerView.findViewHolderForAdapterPosition(firstVisibleItemPosition + i)
                ?: continue

            val wm = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val height = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val windowMetrics = wm.currentWindowMetrics
                val insets = windowMetrics.windowInsets
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
                windowMetrics.bounds.height() - insets.bottom - insets.top
            } else {
                val displayMetrics = DisplayMetrics()
                wm.defaultDisplay.getMetrics(displayMetrics)
                displayMetrics.heightPixels
            }

            // 각 콘텐츠의 Top, Bottom 좌표를 얻는다
            val topPosition = if (holder.itemView.top < 0) 0 else holder.itemView.top
            val bottomPosition =
                if (holder.itemView.bottom > height) height else holder.itemView.bottom

            // 화면 세로 비율을 얻어 콘텐츠마다 점유 면적을 비교.
            val pointedRatio = ((bottomPosition - topPosition).toFloat() / height.toFloat())
            if (pointedRatio > ratio) {
                ratio = pointedRatio
                startPosition = firstVisibleItemPosition + i
            }
        }
        return startPosition
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
            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (layoutManager.findFirstVisibleItemPosition() == 0)
                        adapter.setCurrentPlayingPosition(1)
                    else adapter.setCurrentPlayingPosition(getVideoStartPosition(it))

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
            })
        }
    }

    override fun onResume() {
        super.onResume()
        binding.rvFeed.scrollToPosition(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}