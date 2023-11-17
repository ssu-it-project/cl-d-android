package com.seumulseumul.cld.ui.record

import android.animation.Animator
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.seumulseumul.cld.R
import com.seumulseumul.cld.databinding.DialogRecordBinding
import com.seumulseumul.cld.sharedpref.PrefData
import com.seumulseumul.cld.sharedpref.PrefKey
import com.seumulseumul.cld.ui.adapter.RecordFragmentAdapter
import com.seumulseumul.cld.ui.record.fragment.RecordGymFragment
import com.seumulseumul.cld.ui.record.fragment.RecordLevelFragment
import com.seumulseumul.cld.ui.record.fragment.RecordSectorFragment
import com.seumulseumul.cld.ui.record.fragment.RecordVideoFragment
import com.seumulseumul.domain.model.ClimbingGym
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File


@AndroidEntryPoint
class RecordDialog: BottomSheetDialogFragment() {

    private var _binding: DialogRecordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecordViewModel by activityViewModels()

    private var gymId = ""
    private var gymName = ""
    private var sector = ""
    private var level = ""
    private lateinit var videoUri: Uri

    private val recordGymListener by lazy {
        object : OnNextButtonClick {
            override fun onNextButtonClick(gymData: Any) {
                nextButtonClick()
                gymId = (gymData as ClimbingGym).id
                gymName = (gymData as ClimbingGym).place.name
            }
        }
    }

    private val recordSectorListener by lazy {
        object : OnNextButtonClick {
            override fun onNextButtonClick(data: Any) {
                nextButtonClick()
                sector = (data as String)
            }
        }
    }

    private val recordLevelListener by lazy {
        object : OnNextButtonClick {
            override fun onNextButtonClick(data: Any) {
                nextButtonClick()
                level = (data as String)
            }
        }
    }

    private val recordVideoListener by lazy {
        object : OnNextButtonClick {
            override fun onNextButtonClick(data: Any) {
                videoUri = (data as Uri)
                nextButtonClick()
            }
        }
    }

    private lateinit var recordFragmentAdapter: RecordFragmentAdapter
    private val recordGymFragment by lazy {
        RecordGymFragment(recordGymListener)
    }
    private val recordSectorFragment by lazy {
        RecordSectorFragment(recordSectorListener)
    }
    private val recordLevelFragment by lazy {
        RecordLevelFragment(recordLevelListener)
    }
    private val recordVideoFragment by lazy {
        RecordVideoFragment(recordVideoListener)
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

        initViewModelStream()

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

        val tabStrip = binding.tabLayoutRecord.getChildAt(0) as LinearLayout
        tabStrip.isEnabled = false
        for (i in 0 until tabStrip.childCount) {
            tabStrip.getChildAt(i).isClickable = false
        }

        binding.lottieClD.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
                Toast.makeText(
                    requireContext(),
                    "기록이 완료되었어요!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onAnimationEnd(p0: Animator) {
                dialog?.dismiss()
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }
        })

        binding.btnRecord.setOnClickListener {
            binding.pbRecord.visibility = View.VISIBLE

            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            requireContext().contentResolver.takePersistableUriPermission(videoUri, flag)

            Log.d("TESTLOG", "videoUri: $videoUri")
            val videoFile = uri2path(requireContext(), videoUri)
            Log.d("TESTLOG", "videoFile: $videoFile")


            if (!videoFile.isNullOrEmpty()) {
                Log.d(
                    "TESTLOG",
                    "getVideoResolution(videoFile!!): ${getVideoResolution(videoFile!!)}"
                )
                val resolution = getVideoResolution(videoFile!!)

                viewModel.postClimeRecord(
                    PrefData.getString(PrefKey.authToken, ""),
                    gymId,
                    binding.etRecordContent.text.toString(),
                    sector,
                    level,
                    resolution,
                    File(videoFile)
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun nextButtonClick() {
        val currentTab = binding.tabLayoutRecord.selectedTabPosition
        if (currentTab <binding.tabLayoutRecord.tabCount - 1) {
            binding.viewPagerRecord.currentItem = currentTab + 1

            val tabStrip = binding.tabLayoutRecord.getChildAt(0) as LinearLayout
            tabStrip.isEnabled = true
            for (i in 0 .. currentTab) {
                tabStrip.getChildAt(i).isClickable = true
            }
        } else {
            // 마지막 탭
            binding.tabLayoutRecord.visibility = View.GONE
            binding.viewPagerRecord.visibility = View.GONE
            binding.layoutRecord.visibility = View.VISIBLE

            binding.vvRecordVideo.setVideoURI(videoUri)
            binding.vvRecordVideo.setAudioFocusRequest(AudioManager.AUDIOFOCUS_NONE)
            binding.vvRecordVideo.setOnCompletionListener {
                binding.vvRecordVideo.start()
            }
            binding.vvRecordVideo.seekTo(0)
            binding.vvRecordVideo.start()

            binding.tvRecordGym.text = gymName
            binding.tvRecordSector.text = sector
            binding.tvRecordLevel.text = level

            when (level) {
                "흰색" -> binding.ivRecordLevel.setImageResource(R.drawable.level_white)
                "회색" -> binding.ivRecordLevel.setImageResource(R.drawable.level_grey)
                "검정" -> binding.ivRecordLevel.setImageResource(R.drawable.level_black)
                "파랑" -> binding.ivRecordLevel.setImageResource(R.drawable.level_blue)
                "빨강" -> binding.ivRecordLevel.setImageResource(R.drawable.level_red)
                "갈색" -> binding.ivRecordLevel.setImageResource(R.drawable.level_brown)
                "핑크" -> binding.ivRecordLevel.setImageResource(R.drawable.level_pink)
                "초록" -> binding.ivRecordLevel.setImageResource(R.drawable.level_green)
                "보라" -> binding.ivRecordLevel.setImageResource(R.drawable.level_purple)
                "주황" -> binding.ivRecordLevel.setImageResource(R.drawable.level_orange)
                "노랑" -> binding.ivRecordLevel.setImageResource(R.drawable.level_yellow)
            }
        }
    }

    private fun initViewModelStream() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.climeRecordSharedFlow.collect {
                    if (it) {
                        binding.pbRecord.visibility = View.GONE

                        binding.vvRecordVideo.stopPlayback()
                        binding.vvRecordVideo.visibility = View.GONE
                        binding.layoutRecord.visibility = View.GONE

                        binding.layoutAnimation.visibility = View.VISIBLE
                        binding.lottieClD.playAnimation()
                    }
                }
            }
        }
    }

    private fun uri2path(context: Context, contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
        cursor!!.moveToNext()
        try {
            val path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
            //val uri = Uri.fromFile(File(path))
            cursor.close()
            return path
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun getVideoResolution(path: String): String {
        val mRetriever = MediaMetadataRetriever()
        mRetriever.setDataSource(path)
        val frame = mRetriever.frameAtTime

        val width = frame!!.width
        val height = frame!!.height

        return "$width*$height"
    }
}

interface OnNextButtonClick {
    fun onNextButtonClick(data: Any)
}