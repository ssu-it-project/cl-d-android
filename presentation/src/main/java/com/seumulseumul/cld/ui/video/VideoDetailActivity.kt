package com.seumulseumul.cld.ui.video

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.seumulseumul.cld.ClDApplication
import com.seumulseumul.cld.R
import com.seumulseumul.cld.databinding.ActivityVideoDetailBinding
import com.seumulseumul.domain.model.Record
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoDetailActivity: AppCompatActivity() {
    companion object {
        private val TAG: String = VideoDetailActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityVideoDetailBinding
    private val recordInfo by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("record", Record::class.java)
        } else {
            intent.getSerializableExtra("record") as Record
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        if (recordInfo?.author?.profileImageUrl.isNullOrEmpty()) {
            Glide.with(ClDApplication.applicationContext())
                .load(AppCompatResources.getDrawable(ClDApplication.applicationContext(), R.drawable.profile_image_example))
                .circleCrop()
                .into(binding.ivUserProfile)
        } else {
            Glide.with(ClDApplication.applicationContext())
                .load(recordInfo?.author?.profileImageUrl)
                .circleCrop()
                .into(binding.ivUserProfile)
        }
        binding.tvUserName.text = recordInfo?.author?.nickname
        binding.tvRecordTitle.text = recordInfo?.content
        val infoText = "${recordInfo?.climbingGymInfo?.name} | ${recordInfo?.sector} | ${recordInfo?.level}"
        binding.tvRecordInfo.text = infoText
        binding.tvRecordCreatedDate.text = recordInfo?.date?.created?.substring(0, 10)

        binding.vvRecord.setVideoURI(Uri.parse(recordInfo?.video?.original))
        binding.vvRecord.setOnInfoListener { mp, what, extra ->
            if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
                Log.d("TESTLOG", "MEDIA_INFO_VIDEO_RENDERING_START")
                binding.pbLoadingVideo.visibility = View.GONE
            }
            if (MediaPlayer.MEDIA_INFO_BUFFERING_START == what) {
                Log.d("TESTLOG", "MEDIA_INFO_BUFFERING_START")
                binding.pbLoadingVideo.visibility = View.VISIBLE
            }
            if (MediaPlayer.MEDIA_INFO_BUFFERING_END == what) {
                Log.d("TESTLOG", "MEDIA_INFO_BUFFERING_END")
                binding.pbLoadingVideo.visibility = View.GONE
            }
            false
        }
        binding.vvRecord.setAudioFocusRequest(AudioManager.AUDIOFOCUS_NONE)
        binding.vvRecord.start()
    }
}