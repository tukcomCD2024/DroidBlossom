package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemManagementGroupMemberBinding
import com.droidblossom.archive.domain.model.group.GroupMember

class ManagementGroupMemberRVA(

) : ListAdapter<GroupMember, ManagementGroupMemberRVA.ItemViewHolder>(differ){

    inner class ItemViewHolder(
        private val binding: ItemManagementGroupMemberBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(data: GroupMember){
            binding.item = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemManagementGroupMemberBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object{
        val differ = object : DiffUtil.ItemCallback<GroupMember>(){
            override fun areItemsTheSame(oldItem: GroupMember, newItem: GroupMember): Boolean {
                return oldItem.memberId == newItem.memberId
            }

            override fun areContentsTheSame(oldItem: GroupMember, newItem: GroupMember): Boolean {
                return oldItem == newItem
            }

        }
    }
}