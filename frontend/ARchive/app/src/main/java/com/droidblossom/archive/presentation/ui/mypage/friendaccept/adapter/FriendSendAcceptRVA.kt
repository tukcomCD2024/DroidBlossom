package com.droidblossom.archive.presentation.ui.mypage.friendaccept.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemAcceptSendFriendBinding
import com.droidblossom.archive.domain.model.friend.Friend


class FriendSendAcceptRVA(
    private val onCancel: (Friend) -> Unit,
) :
    ListAdapter<Friend, FriendSendAcceptRVA.ItemViewHolder>(differ) {


    inner class ItemViewHolder(
        private val binding: ItemAcceptSendFriendBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Friend) {
            binding.item = data
            binding.cancelBtn.setOnClickListener {
                onCancel(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemAcceptSendFriendBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<Friend>() {
            override fun areItemsTheSame(
                oldItem: Friend,
                newItem: Friend
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Friend,
                newItem: Friend
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}