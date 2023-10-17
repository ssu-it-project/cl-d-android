package com.seumulseumul.cld.ui.record

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.seumulseumul.cld.R
import com.seumulseumul.cld.databinding.DialogRecordBinding
import com.seumulseumul.cld.ui.adapter.RecordFragmentAdapter
import com.seumulseumul.cld.ui.record.fragment.RecordGymFragment
import com.seumulseumul.cld.ui.record.fragment.RecordLevelFragment
import com.seumulseumul.cld.ui.record.fragment.RecordSectorFragment
import com.seumulseumul.cld.ui.record.fragment.RecordVideoFragment

class RecordDialog: BottomSheetDialogFragment() {

    private var _binding: DialogRecordBinding? = null
    private val binding get() = _binding!!

    private lateinit var recordFragmentAdapter: RecordFragmentAdapter
    private val recordGymFragment by lazy {
        RecordGymFragment()
    }
    private val recordSectorFragment by lazy {
        RecordSectorFragment()
    }
    private val recordLevelFragment by lazy {
        RecordLevelFragment()
    }
    private val recordVideoFragment by lazy {
        RecordVideoFragment()
    }
    private val tabLayoutOnPageChangeListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {}

        override fun onTabUnselected(tab: TabLayout.Tab?) {}

        override fun onTabSelected(tab: TabLayout.Tab?) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)

        recordFragmentAdapter = RecordFragmentAdapter(
            this,
            recordGymFragment, recordSectorFragment, recordLevelFragment, recordVideoFragment
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogRecordBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            setOnShowListener {
                window?.findViewById<View>(com.google.android.material.R.id.touch_outside)
                    ?.setOnClickListener(null)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPagerRecord.adapter = recordFragmentAdapter
        TabLayoutMediator(binding.tabLayoutRecord, binding.viewPagerRecord) {tab, position ->
            when (position) {
                0 -> {tab.text = getString(R.string.title_record_gym)}
                1 -> {tab.text = getString(R.string.title_record_sector)}
                2 -> {tab.text = getString(R.string.title_record_level)}
                3 -> {tab.text = getString(R.string.title_record_video)}
            }
        }.attach()
        binding.tabLayoutRecord.addOnTabSelectedListener(tabLayoutOnPageChangeListener)

        binding.viewPagerRecord.isUserInputEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}