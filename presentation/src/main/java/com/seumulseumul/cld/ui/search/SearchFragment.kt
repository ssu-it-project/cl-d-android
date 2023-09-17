package com.seumulseumul.cld.ui.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.seumulseumul.cld.R
import com.seumulseumul.cld.databinding.FragmentSearchBinding
import com.seumulseumul.cld.sharedpref.PrefData
import com.seumulseumul.cld.sharedpref.PrefKey
import com.seumulseumul.cld.ui.adapter.SearchFragmentAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import java.util.TimerTask

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by activityViewModels()

    private lateinit var searchFragmentAdapter: SearchFragmentAdapter
    private val searchNearGymsFragment by lazy {
        SearchNearGymsFragment()
    }
    private val searchFavoriteGymsFragment by lazy {
        SearchFavoriteGymsFragment()
    }
    private val tabLayoutOnPageChangeListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tabItem: TabLayout.Tab?) {}

        override fun onTabUnselected(tabItem: TabLayout.Tab?) {}

        override fun onTabSelected(tabItem: TabLayout.Tab?) {
            tabItem?.position?.let {
                binding.etSearchGym.setText("")
                viewModel.searchKeywordLiveData.value = ""
            }
        }
    }

    private var timer: Timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchFragmentAdapter = SearchFragmentAdapter(
            this,
            searchNearGymsFragment,
            searchFavoriteGymsFragment
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.viewPagerSearch.adapter = searchFragmentAdapter
        TabLayoutMediator(binding.tabLayoutSearch, binding.viewPagerSearch) {tab, position ->
            when (position) {
                0 -> { tab.text = getString(R.string.title_search_near_gyms) }
                1 -> { tab.text = getString(R.string.title_search_favorite_gyms) }
            }
        }.attach()
        binding.tabLayoutSearch.addOnTabSelectedListener(tabLayoutOnPageChangeListener)

        binding.etSearchGym.addTextChangedListener(object: TextWatcher {
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
                            viewModel.searchKeywordLiveData.value = p0.toString()
                        }
                    }
                }, 300)
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}