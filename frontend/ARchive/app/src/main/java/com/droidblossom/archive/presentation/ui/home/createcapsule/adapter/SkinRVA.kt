package com.droidblossom.archive.presentation.ui.home.createcapsule.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.datastore.preferences.core.preferencesOf
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemSkinBinding
import com.droidblossom.archive.domain.model.common.CapsuleSkinSummary
import com.droidblossom.archive.domain.model.common.Skin

class SkinRVA( val SkinFlow: (CapsuleSkinSummary) -> Unit) :
    ListAdapter<CapsuleSkinSummary, SkinRVA.ItemViewHolder>(differ) {

    inner class ItemViewHolder(
        private val binding: ItemSkinBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind(data: CapsuleSkinSummary) {
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
        val differ = object : DiffUtil.ItemCallback<CapsuleSkinSummary>() {
            override fun areItemsTheSame(oldItem: CapsuleSkinSummary, newItem: CapsuleSkinSummary): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CapsuleSkinSummary, newItem: CapsuleSkinSummary): Boolean {
                return oldItem == newItem
            }
        }
    }
}