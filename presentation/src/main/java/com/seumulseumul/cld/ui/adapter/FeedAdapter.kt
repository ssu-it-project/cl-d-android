package com.seumulseumul.cld.ui.adapter

import android.graphics.Bitmap
import android.media.AudioManager
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seumulseumul.cld.ClDApplication
import com.seumulseumul.cld.R
import com.seumulseumul.cld.databinding.ItemFeedBinding
import com.seumulseumul.cld.databinding.ItemHomeBadgeBinding
import com.seumulseumul.cld.util.VideoThumbnailUtil
import com.seumulseumul.domain.model.Record
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


class FeedAdapter: ListAdapter<Record, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<Record>() {
        override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
            return oldItem == newItem
        }
    }
) {
    enum class ViewType {
        BADGE, FEED
    }

    inner class BadgeViewHolder(
        private val binding: ItemHomeBadgeBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind() {

        }
    }

    inner class FeedViewHolder(
        private val binding: ItemFeedBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Record, position: Int) {
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

            val videoUrl = if (item.video.video480.isNullOrEmpty()) item.video.original
            else item.video.video480
            Log.d("TESTLOG", "videoUrl: $videoUrl")

            if (item.image.isNullOrEmpty()) {
                CoroutineScope(Dispatchers.Main).launch {
                    val getThumbnail = async(Dispatchers.IO) {
                        VideoThumbnailUtil().getWebVideoThumbnail(Uri.parse("$videoUrl"))
                    }
                    launch {
                        binding.ivFeedThumbnail.setImageBitmap(getThumbnail.await())
                    }
                }
            }

            binding.vvFeedVideo.setVideoURI(Uri.parse(videoUrl))
            binding.vvFeedVideo.setOnInfoListener { mp, what, extra ->
                if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
                    Log.d("TESTLOG", "MEDIA_INFO_VIDEO_RENDERING_START")
                    binding.pbLoadingVideo.visibility = View.GONE
                    binding.ivFeedThumbnail.visibility = View.GONE
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
                binding.vvFeedVideo.setOnCompletionListener {
                    binding.vvFeedVideo.start()
                }
                binding.vvFeedVideo.seekTo(0)
                binding.vvFeedVideo.start()
                binding.pbLoadingVideo.visibility = View.VISIBLE
                binding.vvFeedVideo.visibility = View.VISIBLE
                Log.d("TESTLOG", "vvFeedVideo.start()")
            }
        }

        fun stopVideo() {
            if(binding.vvFeedVideo.isPlaying) {
                binding.pbLoadingVideo.visibility = View.VISIBLE
                binding.vvFeedVideo.pause()
                binding.ivFeedThumbnail.visibility = View.VISIBLE
                binding.vvFeedVideo.visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            ViewType.BADGE.ordinal -> {
                return BadgeViewHolder(
                    ItemHomeBadgeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }

            ViewType.FEED.ordinal -> {
                return FeedViewHolder(
                    ItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }

            else -> {
                return FeedViewHolder(
                    ItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BadgeViewHolder -> holder.bind()
            is FeedViewHolder -> holder.bind(getItem(position), position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            ViewType.BADGE.ordinal
        } else {
            ViewType.FEED.ordinal
        }
    }

    /*override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        Log.d("TESTLOG", "onViewAttachedToWindow")
        holder.startVideo()
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        Log.d("TESTLOG", "onViewDetachedFromWindow")
        holder.stopVideo()
    }*/

    /*fun setCurrentPlayingPosition(position: Int) {
        if (currentPlayingPosition != position) {
            val previousPlayingPosition = currentPlayingPosition
            currentPlayingPosition = position
            notifyItemChanged(previousPlayingPosition)
            notifyItemChanged(position)
        }
    }*/
}