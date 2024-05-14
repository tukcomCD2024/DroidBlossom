package com.droidblossom.archive.presentation.ui.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemMyPageStoryBinding
import com.droidblossom.archive.presentation.model.mypage.StoryData

class StoryRVA(

) : ListAdapter<StoryData, StoryRVA.ItemViewHolder>(differ) {

    inner class ItemViewHolder(
        private val binding: ItemMyPageStoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoryData) {
            binding.data = data
            binding.root.setOnClickListener {


            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemMyPageStoryBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<StoryData>() {
            override fun areItemsTheSame(oldItem: StoryData, newItem: StoryData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: StoryData, newItem: StoryData): Boolean {
                return oldItem == newItem
            }
        }
    }
}