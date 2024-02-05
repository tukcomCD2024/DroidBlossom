package com.droidblossom.archive.presentation.ui.capsule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemImageBinding
import com.droidblossom.archive.domain.model.common.Dummy
import com.droidblossom.archive.domain.model.common.ImageUrl

class ImageUrlRVA(val onClick: () -> Unit, val flowData: (List<Dummy>) -> Unit) :
    ListAdapter<ImageUrl, ImageUrlRVA.ItemViewHolder>(differ) {

    inner class ItemViewHolder(
        private val binding: ItemImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ImageUrl) {
            binding.item =data
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
        val differ = object : DiffUtil.ItemCallback<ImageUrl>() {
            override fun areItemsTheSame(oldItem: ImageUrl, newItem: ImageUrl): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: ImageUrl, newItem: ImageUrl): Boolean {
                return oldItem == newItem
            }
        }
    }
}