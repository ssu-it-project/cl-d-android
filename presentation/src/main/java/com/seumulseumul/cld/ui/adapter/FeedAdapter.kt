package com.seumulseumul.cld.ui.adapter

import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnInfoListener
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seumulseumul.cld.BuildConfig
import com.seumulseumul.cld.ClDApplication
import com.seumulseumul.cld.R
import com.seumulseumul.cld.databinding.ItemFeedBinding
import com.seumulseumul.domain.model.Record


class FeedAdapter: ListAdapter<Record, FeedAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<Record>() {
        override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
            return oldItem == newItem
        }
    }
) {
    inner class ViewHolder(
        private val binding: ItemFeedBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Record) {
            val profileImage =
                item.author.profileImageUrl.ifEmpty {
                    AppCompatResources.getDrawable(ClDApplication.applicationContext(), R.drawable.profile_image_example)
                }

            Glide.with(ClDApplication.applicationContext())
                .load(profileImage)
                .circleCrop()
                .into(binding.ivUserProfile)
            binding.tvUserName.text = item.author.nickname

            val videoUrl = BuildConfig.CL_D_VIDEO_SERVER_BASE_URL + item.id + "/" + item.video
            binding.vvFeedVideo.setVideoURI(Uri.parse(videoUrl))
            binding.vvFeedVideo.setOnInfoListener { mp, what, extra ->
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

            binding.tvFeedTitle.text = item.content
            val infoText = "${item.climbingGymInfo.name} | ${item.sector} | ${item.level}"
            binding.tvFeedInfo.text = infoText
            binding.tvFeedCreatedDate.text = item.date.created
        }

        fun startVideo() {
            if (!binding.vvFeedVideo.isPlaying) {
                binding.vvFeedVideo.setAudioFocusRequest(AudioManager.AUDIOFOCUS_NONE)
                binding.vvFeedVideo.start()
                binding.pbLoadingVideo.visibility = View.VISIBLE
                Log.d("TESTLOG", "vvFeedVideo.start()")
            }
        }

        fun stopVideo() {
            if(binding.vvFeedVideo.isPlaying) {
                binding.pbLoadingVideo.visibility = View.VISIBLE
                binding.vvFeedVideo.pause()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        Log.d("TESTLOG", "onViewAttachedToWindow")
        holder.startVideo()
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        Log.d("TESTLOG", "onViewDetachedFromWindow")
        holder.stopVideo()
    }
}