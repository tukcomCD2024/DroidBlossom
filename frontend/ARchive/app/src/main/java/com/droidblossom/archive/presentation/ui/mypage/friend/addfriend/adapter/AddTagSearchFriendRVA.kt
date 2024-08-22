package com.droidblossom.archive.presentation.ui.mypage.friend.addfriend.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemAddFriendBinding
import com.droidblossom.archive.databinding.ItemAddTagSearchFriendBinding
import com.droidblossom.archive.domain.model.friend.FriendsSearchResponse
import com.droidblossom.archive.presentation.model.mypage.friend.AddTagSearchFriendUIModel

class AddTagSearchFriendRVA(private val request: (Long) -> Unit) :
    ListAdapter<AddTagSearchFriendUIModel, AddTagSearchFriendRVA.ItemViewHolder>(differ) {

    inner class ItemViewHolder(
        private val binding: ItemAddTagSearchFriendBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: AddTagSearchFriendUIModel) {
            binding.item = data
            binding.addBtn.setOnClickListener {
                request(data.id)
                notifyItemChanged(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemAddTagSearchFriendBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<AddTagSearchFriendUIModel>() {
            override fun areItemsTheSame(
                oldItem: AddTagSearchFriendUIModel,
                newItem: AddTagSearchFriendUIModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: AddTagSearchFriendUIModel,
                newItem: AddTagSearchFriendUIModel
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}