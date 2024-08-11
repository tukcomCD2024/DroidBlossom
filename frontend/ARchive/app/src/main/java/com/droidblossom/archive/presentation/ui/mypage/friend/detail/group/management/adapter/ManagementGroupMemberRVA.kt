package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemManagementGroupMasterBinding
import com.droidblossom.archive.databinding.ItemManagementGroupMemberBinding
import com.droidblossom.archive.domain.model.group.GroupMember

class ManagementGroupMemberRVA(
    private val kickGroupMember: (GroupMember) -> Unit
) : ListAdapter<GroupMember, RecyclerView.ViewHolder>(differ){

    inner class GroupMasterItemViewHolder(
        private val binding: ItemManagementGroupMasterBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun groupMasterBind(data: GroupMember){
            binding.item = data
        }
    }

    inner class GroupMemberItemViewHolder(
        private val binding: ItemManagementGroupMemberBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun groupMemberBind(data: GroupMember){
            binding.item = data
            binding.kickBtn.setOnClickListener {
                kickGroupMember(data)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isOwner) {
            TYPE_GROUP_MASTER
        } else {
            TYPE_GROUP_MEMBER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType){
            TYPE_GROUP_MASTER -> GroupMasterItemViewHolder(ItemManagementGroupMasterBinding.inflate(inflater,parent,false))
            TYPE_GROUP_MEMBER -> GroupMemberItemViewHolder(ItemManagementGroupMemberBinding.inflate(inflater,parent,false))
            else -> throw IllegalArgumentException("Invalid view type")

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder){
            is GroupMasterItemViewHolder -> holder.groupMasterBind(item)
            is GroupMemberItemViewHolder -> holder.groupMemberBind(item)
        }
    }

    companion object{

        private const val TYPE_GROUP_MASTER = 0
        private const val TYPE_GROUP_MEMBER = 1

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