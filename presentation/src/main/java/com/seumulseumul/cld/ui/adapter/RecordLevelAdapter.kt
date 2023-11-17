package com.seumulseumul.cld.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.seumulseumul.cld.R
import com.seumulseumul.cld.databinding.ItemRecordLevelBinding
import com.seumulseumul.cld.ui.record.OnNextButtonClick

class RecordLevelAdapter(
    private val buttonClickListener: OnNextButtonClick
): ListAdapter<String, RecordLevelAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<String>() {
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
) {
    private var selectedLevel = -1

    inner class ViewHolder(
        private val binding: ItemRecordLevelBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String, position: Int) {
            when (item) {
                "흰색" -> binding.ivLevel.setImageResource(R.drawable.level_white)
                "회색" -> binding.ivLevel.setImageResource(R.drawable.level_grey)
                "검정" -> binding.ivLevel.setImageResource(R.drawable.level_black)
                "파랑" -> binding.ivLevel.setImageResource(R.drawable.level_blue)
                "빨강" -> binding.ivLevel.setImageResource(R.drawable.level_red)
                "갈색" -> binding.ivLevel.setImageResource(R.drawable.level_brown)
                "핑크" -> binding.ivLevel.setImageResource(R.drawable.level_pink)
                "초록" -> binding.ivLevel.setImageResource(R.drawable.level_green)
                "보라" -> binding.ivLevel.setImageResource(R.drawable.level_purple)
                "주황" -> binding.ivLevel.setImageResource(R.drawable.level_orange)
                "노랑" -> binding.ivLevel.setImageResource(R.drawable.level_yellow)
            }

            binding.tvLevel.text = item

            binding.layoutLevel.setOnClickListener {
                selectedLevel = position
                buttonClickListener.onNextButtonClick(item)
                notifyDataSetChanged()
            }

            if (position == selectedLevel) {
                binding.layoutLevel.setBackgroundResource(R.drawable.layout_level_enable)
            } else {
                binding.layoutLevel.setBackgroundResource(R.drawable.layout_level_disable)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRecordLevelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}