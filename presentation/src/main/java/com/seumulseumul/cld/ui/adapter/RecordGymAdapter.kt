package com.seumulseumul.cld.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.seumulseumul.cld.databinding.ItemRecordGymBinding
import com.seumulseumul.cld.ui.record.OnNextButtonClick
import com.seumulseumul.domain.model.ClimbingGym

class RecordGymAdapter(
    private val nextButtonClickListener: OnNextButtonClick
): ListAdapter<ClimbingGym, RecordGymAdapter.ViewHolder>(
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
        private val binding: ItemRecordGymBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ClimbingGym) {
            binding.tvGym.text = item.place.name
            binding.layoutRoot.setOnClickListener {
                nextButtonClickListener.onNextButtonClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemRecordGymBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}