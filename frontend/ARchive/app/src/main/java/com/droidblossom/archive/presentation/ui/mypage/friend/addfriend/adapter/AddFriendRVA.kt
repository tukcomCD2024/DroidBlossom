package com.droidblossom.archive.presentation.ui.mypage.friend.addfriend.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemAddFriendBinding
import com.droidblossom.archive.domain.model.friend.FriendsSearchResponse

class AddFriendRVA(private val check: (Int) -> Unit) :
    ListAdapter<FriendsSearchResponse, AddFriendRVA.ItemViewHolder>(differ) {

    inner class ItemViewHolder(
        private val binding: ItemAddFriendBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: FriendsSearchResponse) {
            binding.item = data
            binding.root.setOnClickListener {
                check(bindingAdapterPosition)
                notifyItemChanged(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemAddFriendBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<FriendsSearchResponse>() {
            override fun areItemsTheSame(
                oldItem: FriendsSearchResponse,
                newItem: FriendsSearchResponse
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: FriendsSearchResponse,
                newItem: FriendsSearchResponse
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}