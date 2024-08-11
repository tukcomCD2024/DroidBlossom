package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemGroupMemberBinding
import com.droidblossom.archive.domain.model.group.GroupMember

class GroupMemberRVA(
    private val requestFriend: (Long) -> Unit,
): ListAdapter<GroupMember,GroupMemberRVA.ItemViewHolder>(differ) {

    inner class ItemViewHolder(
        private val binding: ItemGroupMemberBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(data: GroupMember){
            binding.data = data
            binding.addFriend.setOnClickListener {
                requestFriend(data.memberId)
                notifyItemChanged(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemGroupMemberBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<GroupMember>() {
            override fun areItemsTheSame(
                oldItem: GroupMember,
                newItem: GroupMember
            ): Boolean {
                return oldItem.memberId == newItem.memberId
            }

            override fun areContentsTheSame(
                oldItem: GroupMember,
                newItem: GroupMember
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}


