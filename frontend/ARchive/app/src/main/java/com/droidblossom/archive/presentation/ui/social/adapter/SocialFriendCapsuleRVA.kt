package com.droidblossom.archive.presentation.ui.social.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemSocialCapsuleCloseBinding
import com.droidblossom.archive.databinding.ItemSocialCapsuleOpenBinding

class SocialFriendCapsuleRVA() : ListAdapter<TestSocialFriendModel, RecyclerView.ViewHolder>(differ) {

    inner class OpenedCapsuleViewHolder(
        private val binding: ItemSocialCapsuleOpenBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bindOpenedCapsule(data : TestSocialFriendModel){
            binding.item = data
        }

    }

    inner class ClosedCapsuleViewHolder(
        private val binding: ItemSocialCapsuleCloseBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bindClosedCapsule(data: TestSocialFriendModel){
            binding.item = data
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isOpened) {
            TYPE_OPENED_CAPSULE
        } else {
            TYPE_CLOSED_CAPSULE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_OPENED_CAPSULE -> OpenedCapsuleViewHolder(ItemSocialCapsuleOpenBinding.inflate(inflater, parent, false))
            TYPE_CLOSED_CAPSULE -> ClosedCapsuleViewHolder(ItemSocialCapsuleCloseBinding.inflate(inflater, parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is OpenedCapsuleViewHolder -> holder.bindOpenedCapsule(item)
            is ClosedCapsuleViewHolder -> holder.bindClosedCapsule(item)
        }
    }


    companion object {

        private const val TYPE_OPENED_CAPSULE = 0
        private const val TYPE_CLOSED_CAPSULE = 1

        val differ = object : DiffUtil.ItemCallback<TestSocialFriendModel>() {
            override fun areItemsTheSame(oldItem: TestSocialFriendModel, newItem: TestSocialFriendModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TestSocialFriendModel, newItem: TestSocialFriendModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}