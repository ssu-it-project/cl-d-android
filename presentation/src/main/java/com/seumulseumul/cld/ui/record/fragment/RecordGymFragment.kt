package com.seumulseumul.cld.ui.record.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.seumulseumul.cld.databinding.FragmentRecordGymBinding

class RecordGymFragment: Fragment() {
    private var _binding: FragmentRecordGymBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordGymBinding.inflate(inflater, container, false)


        return binding.root
    }
}