package com.droidblossom.archive.presentation.ui.mypage.setting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ItemNoticeBinding
import com.droidblossom.archive.domain.model.setting.Notice


class NoticeRVA :
    ListAdapter<Notice, NoticeRVA.ItemViewHolder>(differ) {

    inner class ItemViewHolder(
        private val binding: ItemNoticeBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Notice) {
            binding.item = data
            val adapter = NoticeContentRVA()
            binding.contentRVA.adapter = adapter
            adapter.submitList(data.contents)
            binding.moreBtn.setOnClickListener{
                if (data.isOpen) {
                    data.isOpen = false
                    binding.contentLayout.isGone = true
                    binding.moreBtn.rotation = 0f
                    binding.line.setBackgroundResource(R.color.gray_400)
                } else {
                    data.isOpen = true
                    binding.contentLayout.isVisible = true
                    binding.moreBtn.rotation = 180f
                    binding.line.setBackgroundResource(R.color.black)
                }
            }
        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemNoticeBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<Notice>() {
            override fun areItemsTheSame(oldItem: Notice, newItem: Notice): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Notice, newItem: Notice): Boolean {
                return oldItem == newItem
            }
        }
    }
}
