package com.seumulseumul.cld.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.seumulseumul.cld.ui.record.fragment.RecordGymFragment
import com.seumulseumul.cld.ui.record.fragment.RecordLevelFragment
import com.seumulseumul.cld.ui.record.fragment.RecordSectorFragment
import com.seumulseumul.cld.ui.record.fragment.RecordVideoFragment

class RecordFragmentAdapter(
    fragment: Fragment,
    private val recordGymFragment: RecordGymFragment,
    private val recordSectorFragment: RecordSectorFragment,
    private val recordLevelFragment: RecordLevelFragment,
    private val recordVideoFragment: RecordVideoFragment
): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> recordVideoFragment
            1 -> recordGymFragment
            2 -> recordSectorFragment
            3 -> recordLevelFragment
            else -> recordGymFragment
        }
    }
}