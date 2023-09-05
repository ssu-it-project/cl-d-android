package com.seumulseumul.cld.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.seumulseumul.cld.ui.search.SearchFavoriteGymsFragment
import com.seumulseumul.cld.ui.search.SearchNearGymsFragment

class SearchFragmentAdapter(
    fragment: Fragment,
    private val searchNearGymsFragment: SearchNearGymsFragment,
    private val searchFavoriteGymsFragment: SearchFavoriteGymsFragment
): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> searchNearGymsFragment
            1 -> searchFavoriteGymsFragment
            else -> searchNearGymsFragment
        }
    }
}