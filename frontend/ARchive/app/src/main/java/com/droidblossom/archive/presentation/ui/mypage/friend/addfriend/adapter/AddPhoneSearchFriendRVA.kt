package com.droidblossom.archive.presentation.ui.mypage.friend.addfriend.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemAddPhoneSearchFriendBinding
import com.droidblossom.archive.presentation.model.mypage.friend.AddTagSearchFriendUIModel

class AddPhoneSearchFriendRVA(private val itemClick: (AddTagSearchFriendUIModel) -> Unit) :
    ListAdapter<AddTagSearchFriendUIModel, AddPhoneSearchFriendRVA.ItemViewHolder>(differ) {

    inner class ItemViewHolder(
        private val binding: ItemAddPhoneSearchFriendBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: AddTagSearchFriendUIModel) {
            binding.item = data
            binding.root.setOnClickListener {
                itemClick(data)
                notifyItemChanged(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemAddPhoneSearchFriendBinding.inflate(
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