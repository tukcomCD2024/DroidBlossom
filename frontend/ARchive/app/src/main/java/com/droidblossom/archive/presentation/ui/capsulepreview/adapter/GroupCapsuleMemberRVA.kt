package com.droidblossom.archive.presentation.ui.capsulepreview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemGroupNumberBinding
import com.droidblossom.archive.domain.model.group_capsule.GroupCapsuleMember

class GroupCapsuleMemberRVA(

): ListAdapter<GroupCapsuleMember, GroupCapsuleMemberRVA.ItemViewHolder>(differ) {

    inner class ItemViewHolder(
        private val binding: ItemGroupNumberBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(data: GroupCapsuleMember){
            binding.item = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemGroupNumberBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<GroupCapsuleMember>() {
            override fun areItemsTheSame(oldItem: GroupCapsuleMember, newItem: GroupCapsuleMember): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: GroupCapsuleMember, newItem: GroupCapsuleMember): Boolean {
                return oldItem == newItem
            }
        }
    }


}