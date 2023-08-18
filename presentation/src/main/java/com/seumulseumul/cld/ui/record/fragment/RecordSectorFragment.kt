package com.seumulseumul.cld.ui.record.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.seumulseumul.cld.databinding.FragmentRecordSectorBinding

class RecordSectorFragment: Fragment() {
    private var _binding: FragmentRecordSectorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecordSectorBinding.inflate(inflater, container, false)

        return binding.root
    }
}