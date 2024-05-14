package com.droidblossom.archive.presentation.ui.home.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemNotificationBinding
import com.droidblossom.archive.domain.model.member.NotificationModel

class NotificationRVA(val onClick: (NotificationModel) -> Unit) :
    ListAdapter<NotificationModel, NotificationRVA.ItemViewHolder>(differ) {

    inner class ItemViewHolder(
        private val binding: ItemNotificationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: NotificationModel) {
            binding.item = data
            binding.root.setOnClickListener {
                onClick(data)
            }
        }
    }

    companion object {
        val differ = object : DiffUtil.ItemCallback<NotificationModel>() {
            override fun areItemsTheSame(
                oldItem: NotificationModel, newItem: NotificationModel
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: NotificationModel, newItem: NotificationModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}