package com.droidblossom.archive.presentation.ui.mypage.setting.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ItemAgreeBinding
import com.droidblossom.archive.domain.model.setting.Agree


class AgreeRVA :
    ListAdapter<Agree, AgreeRVA.ItemViewHolder>(differ) {

    inner class ItemViewHolder(
        private val binding: ItemAgreeBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun bind(data: Agree) {
            binding.item = data
            binding.moreBtn.setOnClickListener {
                if (data.isOpen) {
                    data.isOpen = false
                    binding.contentSV.isGone = true
                    binding.moreBtn.setImageResource(R.drawable.ic_plus_24)
                } else {
                    data.isOpen = true
                    binding.contentSV.isVisible = true
                    binding.moreBtn.setImageResource(R.drawable.ic_minus_24)
                }
            }
        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemAgreeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val differ = object : DiffUtil.ItemCallback<Agree>() {
            override fun areItemsTheSame(oldItem: Agree, newItem: Agree): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Agree, newItem: Agree): Boolean {
                return oldItem == newItem
            }
        }
    }
}
