package com.droidblossom.archive.presentation.ui.mypage.friend.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemGroupWithNumberBinding
import com.droidblossom.archive.domain.model.group.GroupSummary


class GroupRVA(
    private val onClick: (Long) -> Unit
) :
    ListAdapter<GroupSummary, GroupRVA.ItemViewHolder>(differ) {

    inner class ItemViewHolder(
        private val binding: ItemGroupWithNumberBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: GroupSummary) {
            binding.data = data
            binding.root.setOnClickListener {
                onClick(data.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemGroupWithNumberBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<GroupSummary>() {
            override fun areItemsTheSame(
                oldItem: GroupSummary,
                newItem: GroupSummary
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: GroupSummary,
                newItem: GroupSummary
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}