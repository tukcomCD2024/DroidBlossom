package com.droidblossom.archive.presentation.ui.mypage.friendaccept.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemAcceptGroupBinding
import com.droidblossom.archive.domain.model.group.GroupInviteSummary


class GroupAcceptRVA(
    private val onDeny: (GroupInviteSummary) -> Unit,
    private val onAccept: (GroupInviteSummary) -> Unit
) :
    ListAdapter<GroupInviteSummary, GroupAcceptRVA.ItemViewHolder>(differ) {


    inner class ItemViewHolder(
        private val binding: ItemAcceptGroupBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: GroupInviteSummary) {
            binding.item = data
            binding.acceptBtn.setOnClickListener {
                onAccept(data)
            }
            binding.denyBtn.setOnClickListener {
                onDeny(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemAcceptGroupBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<GroupInviteSummary>() {
            override fun areItemsTheSame(
                oldItem: GroupInviteSummary,
                newItem: GroupInviteSummary
            ): Boolean {
                return oldItem.groupId == newItem.groupId
            }

            override fun areContentsTheSame(
                oldItem: GroupInviteSummary,
                newItem: GroupInviteSummary
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}