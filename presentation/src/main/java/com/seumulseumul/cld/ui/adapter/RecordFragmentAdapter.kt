package com.seumulseumul.cld.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.seumulseumul.cld.ui.record.fragment.RecordGymFragment
import com.seumulseumul.cld.ui.record.fragment.RecordSectorFragment

class RecordFragmentAdapter(
    fragment: Fragment,
    private val recordGymFragment: RecordGymFragment,
    private val recordSectorFragment: RecordSectorFragment
): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> recordGymFragment
            1 -> recordSectorFragment
            //2 ->
            else -> recordGymFragment
        }
    }
}