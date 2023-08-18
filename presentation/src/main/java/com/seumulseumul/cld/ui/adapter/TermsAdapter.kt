package com.seumulseumul.cld.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.seumulseumul.cld.R
import com.seumulseumul.cld.databinding.ItemTermBinding
import com.seumulseumul.cld.ui.signup.OnItemClickListener
import com.seumulseumul.domain.model.Term

class TermsAdapter(
    private val onCheckAgreeClickListener: OnItemClickListener,
    private val onSendCallClickListener: OnItemClickListener
) : ListAdapter<Term, TermsAdapter.ViewHolder>(
    object: DiffUtil.ItemCallback<Term>() {
        override fun areItemsTheSame(oldItem: Term, newItem: Term): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Term, newItem: Term): Boolean {
            return oldItem == newItem
        }
    }
) {

    inner class ViewHolder(
        private val binding: ItemTermBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Term) {
            binding.term = item
            binding.checkAgreeClickListener = onCheckAgreeClickListener
            binding.showDetailClickListener = onSendCallClickListener
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_term, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}