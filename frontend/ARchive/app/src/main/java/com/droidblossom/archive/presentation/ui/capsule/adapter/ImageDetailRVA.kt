package com.droidblossom.archive.presentation.ui.capsule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemDetailImageBinding
import com.droidblossom.archive.domain.model.common.ContentUrl

class ImageDetailRVA() :
    ListAdapter<ContentUrl, ImageDetailRVA.ItemViewHolder>(differ) {

    inner class ItemViewHolder(
        private val binding: ItemDetailImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ContentUrl) {
            binding.item = data
        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemDetailImageBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<ContentUrl>() {
            override fun areItemsTheSame(oldItem: ContentUrl, newItem: ContentUrl): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: ContentUrl, newItem: ContentUrl): Boolean {
                return oldItem == newItem
            }
        }
    }
}