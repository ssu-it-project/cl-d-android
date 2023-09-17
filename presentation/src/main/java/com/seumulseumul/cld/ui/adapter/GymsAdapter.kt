package com.seumulseumul.cld.ui.adapter

import android.content.Context
import android.content.Intent
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
import com.seumulseumul.cld.ui.gym.GymDetailActivity
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

    private lateinit var context: Context

    inner class ViewHolder(
        private val binding: ItemGymsBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ClimbingGym) {
            binding.climeGym = item

            val profileImage = item.place.imageUrl.ifEmpty {
                AppCompatResources.getDrawable(ClDApplication.applicationContext(), R.drawable.gym_thumbnail_example)
            }
            Glide.with(ClDApplication.applicationContext())
                .load(profileImage)
                .into(binding.ivGymThumbnail)

            if (item.place.parking) binding.ivParking.visibility = View.VISIBLE
            else binding.ivParking.visibility = View.GONE

            if (item.location == null) {
                binding.tvGymDistance.visibility = View.GONE
            } else {
                val distance = item.location!!.distance.toInt()
                var distanceString = ""

                distanceString =
                    if (distance < 1000) "${distance}m"
                    else "${distance/1000}.${(distance%1000).toString().padStart(2, '0').substring(0, 2)}km"
                binding.tvGymDistance.text = distanceString
            }

            binding.layoutGym.setOnClickListener {
                val intent = Intent(context, GymDetailActivity::class.java)
                intent.putExtra("id", item.id)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
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