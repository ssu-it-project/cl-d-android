package com.seumulseumul.cld.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.seumulseumul.cld.R
import com.seumulseumul.cld.databinding.FragmentSearchBinding
import com.seumulseumul.cld.ui.adapter.SearchFragmentAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchFragmentAdapter: SearchFragmentAdapter
    private val searchNearGymsFragment by lazy {
        SearchNearGymsFragment()
    }
    private val searchFavoriteGymsFragment by lazy {
        SearchFavoriteGymsFragment()
    }

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

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}