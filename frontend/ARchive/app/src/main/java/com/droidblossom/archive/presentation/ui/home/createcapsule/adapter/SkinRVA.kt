package com.droidblossom.archive.presentation.ui.home.createcapsule.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.datastore.preferences.core.preferencesOf
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemSkinBinding
import com.droidblossom.archive.domain.model.common.Skin

class SkinRVA( val SkinFlow: (Skin) -> Unit) :
    ListAdapter<Skin, SkinRVA.ItemViewHolder>(differ) {

    inner class ItemViewHolder(
        private val binding: ItemSkinBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind(data: Skin) {
            binding.item = data
            binding.root.setOnClickListener {
                SkinFlow(data)
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemSkinBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<Skin>() {
            override fun areItemsTheSame(oldItem: Skin, newItem: Skin): Boolean {
                return oldItem.skinId == newItem.skinId
            }

            override fun areContentsTheSame(oldItem: Skin, newItem: Skin): Boolean {
                return oldItem == newItem
            }
        }
    }
}