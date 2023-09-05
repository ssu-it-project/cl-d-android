package com.seumulseumul.cld.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seumulseumul.cld.ClDApplication
import com.seumulseumul.cld.R
import com.seumulseumul.cld.databinding.ItemGymsBinding
import com.seumulseumul.domain.model.ClimbingGym

class GymsAdapter: ListAdapter<ClimbingGym, GymsAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<ClimbingGym>() {
        override fun areItemsTheSame(oldItem: ClimbingGym, newItem: ClimbingGym): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ClimbingGym, newItem: ClimbingGym): Boolean {
            return oldItem == newItem
        }
    }
) {

    inner class ViewHolder(
        private val binding: ItemGymsBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ClimbingGym) {
            binding.climeGym = item

            // 임시 썸네일 이미지
            val profileImage = AppCompatResources.getDrawable(ClDApplication.applicationContext(), R.drawable.gym_thumbnail_example)
            Glide.with(ClDApplication.applicationContext())
                .load(profileImage)
                .into(binding.ivGymThumbnail)

            if (item.place.parking) binding.ivParking.visibility = View.VISIBLE
            else binding.ivParking.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemGymsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}