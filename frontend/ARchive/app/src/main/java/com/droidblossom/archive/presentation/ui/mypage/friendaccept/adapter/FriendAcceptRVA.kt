package com.droidblossom.archive.presentation.ui.mypage.friendaccept.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemAcceptFriendBinding
import com.droidblossom.archive.domain.model.friend.Friend


class FriendAcceptRVA(
    private val onDeny: (Friend) -> Unit,
    private val onAccept: (Friend) -> Unit
) :
    ListAdapter<Friend, FriendAcceptRVA.ItemViewHolder>(differ) {


    inner class ItemViewHolder(
        private val binding: ItemAcceptFriendBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Friend) {
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
            ItemAcceptFriendBinding.inflate(
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