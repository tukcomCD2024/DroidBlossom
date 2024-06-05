package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemManagementInvitableFriendBinding
import com.droidblossom.archive.presentation.model.mypage.detail.FriendForGroupInvite

class ManagementInvitableFriendRVA(
    private val onItemClick: (Int) -> Unit
): ListAdapter<FriendForGroupInvite,ManagementInvitableFriendRVA.ItemViewHolder>(differ){

        inner class ItemViewHolder(
            private val binding: ItemManagementInvitableFriendBinding
        ): RecyclerView.ViewHolder(binding.root){
            fun bind(data: FriendForGroupInvite){
                binding.item = data
                binding.root.setOnClickListener {
                    onItemClick(bindingAdapterPosition)
                    notifyItemChanged(bindingAdapterPosition)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemManagementInvitableFriendBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<FriendForGroupInvite>() {
            override fun areItemsTheSame(
                oldItem: FriendForGroupInvite,
                newItem: FriendForGroupInvite
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: FriendForGroupInvite,
                newItem: FriendForGroupInvite
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}