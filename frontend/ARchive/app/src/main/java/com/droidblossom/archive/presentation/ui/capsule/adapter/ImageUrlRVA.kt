package com.droidblossom.archive.presentation.ui.capsule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ItemImageBinding
import com.droidblossom.archive.domain.model.common.ContentType
import com.droidblossom.archive.domain.model.common.ContentUrl
import com.droidblossom.archive.util.setUrlImg

class ImageUrlRVA(val onClick: (Int, List<ContentUrl>) -> Unit, val onVideoClick: (String) -> Unit) :
    ListAdapter<ContentUrl, ImageUrlRVA.ItemViewHolder>(differ) {

    inner class ItemViewHolder(
        private val binding: ItemImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ContentUrl) {
            binding.item = data
            if (data.contentType == ContentType.IMAGE) {
                binding.playBtn.isGone = true
                binding.root.setOnClickListener {
                    onClick(
                        absoluteAdapterPosition,
                        currentList.filter { it.contentType == ContentType.IMAGE })
                }
            }else{
                binding.playBtn.isVisible = true
                binding.root.setOnClickListener {
                    onVideoClick(data.url)
                }
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemImageBinding.inflate(
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