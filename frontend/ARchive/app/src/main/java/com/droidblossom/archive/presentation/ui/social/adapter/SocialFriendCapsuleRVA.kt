package com.droidblossom.archive.presentation.ui.social.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemSocialCapsuleCloseBinding
import com.droidblossom.archive.databinding.ItemSocialCapsuleOpenBinding
import com.droidblossom.archive.domain.model.common.SocialCapsules

class SocialFriendCapsuleRVA() : ListAdapter<SocialCapsules, RecyclerView.ViewHolder>(differ) {

    inner class OpenedCapsuleViewHolder(
        private val binding: ItemSocialCapsuleOpenBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bindOpenedCapsule(data : SocialCapsules){
            binding.item = data
        }

    }

    inner class ClosedCapsuleViewHolder(
        private val binding: ItemSocialCapsuleCloseBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bindClosedCapsule(data: SocialCapsules){
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

        val differ = object : DiffUtil.ItemCallback<SocialCapsules>() {
            override fun areItemsTheSame(oldItem: SocialCapsules, newItem: SocialCapsules): Boolean {
                return oldItem.capsuleId == newItem.capsuleId
            }

            override fun areContentsTheSame(oldItem: SocialCapsules, newItem: SocialCapsules): Boolean {
                return oldItem == newItem
            }
        }
    }
}