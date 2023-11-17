package com.seumulseumul.cld.ui.record.fragment

import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.seumulseumul.cld.R
import com.seumulseumul.cld.databinding.FragmentRecordVideoBinding
import com.seumulseumul.cld.ui.record.OnNextButtonClick

class RecordVideoFragment(
    private val nextButtonClickListener: OnNextButtonClick
): Fragment() {
    private var _binding: FragmentRecordVideoBinding? = null
    private val binding get() = _binding!!

    private var videoUri: Uri? = null

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            videoUri = uri
            binding.btnRecordVideoNext.background = ContextCompat.getDrawable(requireContext(), R.drawable.start_button_enabled)
            setVideo()
        } else {
            Log.d("PhotoPicker", "No media selected")
            binding.vvRecordVideo.visibility = View.GONE
            binding.btnRecordVideoNext.background = ContextCompat.getDrawable(requireContext(), R.drawable.start_button_disabled)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordVideoBinding.inflate(inflater,container, false)

        initView()
        selectVideo()

        return binding.root
    }

    private fun initView() {
        binding.tvSelectVideo.setOnClickListener {
            selectVideo()
        }

        binding.btnRecordVideoNext.setOnClickListener {
            if (videoUri != null) nextButtonClickListener.onNextButtonClick(videoUri!!)
        }
    }

    private fun selectVideo() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
    }

    private fun setVideo() {
        binding.vvRecordVideo.visibility = View.VISIBLE
        binding.vvRecordVideo.setVideoURI(videoUri)
        binding.vvRecordVideo.setAudioFocusRequest(AudioManager.AUDIOFOCUS_NONE)
        binding.vvRecordVideo.setOnCompletionListener {
            binding.vvRecordVideo.start()
        }
        binding.vvRecordVideo.seekTo(0)
        binding.vvRecordVideo.start()
    }

    override fun onStop() {
        super.onStop()
        binding.vvRecordVideo.stopPlayback()
    }
}