package com.droidblossom.archive.presentation.ui.mypage.setting.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemNoticeBinding
import com.droidblossom.archive.databinding.ItemNoticeContentBinding
import com.droidblossom.archive.domain.model.setting.Notice
import com.droidblossom.archive.domain.model.setting.NoticeContent


class NoticeContentRVA :
    ListAdapter<NoticeContent, NoticeContentRVA.ItemViewHolder>(differ) {

    inner class ItemViewHolder(
        private val binding: ItemNoticeContentBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: NoticeContent) {
            binding.item = data
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemNoticeContentBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<NoticeContent>() {
            override fun areItemsTheSame(oldItem: NoticeContent, newItem: NoticeContent): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: NoticeContent, newItem: NoticeContent): Boolean {
                return oldItem == newItem
            }
        }
    }
}
