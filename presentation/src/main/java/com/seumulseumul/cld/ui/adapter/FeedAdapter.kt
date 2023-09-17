package com.seumulseumul.cld.ui.adapter

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seumulseumul.cld.ClDApplication
import com.seumulseumul.cld.R
import com.seumulseumul.cld.databinding.ItemFeedBinding
import com.seumulseumul.domain.model.Record
import com.seumulseumul.domain.model.Term


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
    public interface OnVideoPlayListener {
        fun onVideoPlay(videoView: VideoView)
    }
    private lateinit var videoPlayerListener: OnVideoPlayListener

    public fun setOnVideoPlayListener(listener: OnVideoPlayListener) {
        videoPlayerListener = listener
    }


    inner class ViewHolder(
        private val binding: ItemFeedBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Record) {
            if (item.author.profileImageUrl.isNullOrEmpty()) {
                Glide.with(ClDApplication.applicationContext())
                    .load(AppCompatResources.getDrawable(ClDApplication.applicationContext(), R.drawable.profile_image_example))
                    .circleCrop()
                    .into(binding.ivUserProfile)
            } else {
                Glide.with(ClDApplication.applicationContext())
                    .load(item.author.profileImageUrl)
                    .circleCrop()
                    .into(binding.ivUserProfile)
            }
            binding.tvUserName.text = item.author.nickname

            binding.vvFeedVideo.setVideoURI(Uri.parse(item.video))
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
            binding.tvFeedCreatedDate.text = item.date.created.substring(0, 10)
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