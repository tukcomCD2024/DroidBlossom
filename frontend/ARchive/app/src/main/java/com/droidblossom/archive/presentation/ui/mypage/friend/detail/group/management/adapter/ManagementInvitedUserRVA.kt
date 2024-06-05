package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemManagementInvitedUserBinding
import com.droidblossom.archive.domain.model.group.GroupInvitedUser

class ManagementInvitedUserRVA(

): ListAdapter<GroupInvitedUser, ManagementInvitedUserRVA.ItemViewHolder>(differ) {

    inner class ItemViewHolder(
        private val binding: ItemManagementInvitedUserBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: GroupInvitedUser){
            binding.item = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemManagementInvitedUserBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<GroupInvitedUser>(){
            override fun areItemsTheSame(
                oldItem: GroupInvitedUser,
                newItem: GroupInvitedUser
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: GroupInvitedUser,
                newItem: GroupInvitedUser
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}