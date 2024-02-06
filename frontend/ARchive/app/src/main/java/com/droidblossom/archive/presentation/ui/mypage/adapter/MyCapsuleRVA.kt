package com.droidblossom.archive.presentation.ui.mypage.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amazonaws.util.ClassLoaderHelper.getResource
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ItemMyCapsuleBinding
import com.droidblossom.archive.domain.model.common.MyCapsule
import com.droidblossom.archive.presentation.ui.home.HomeFragment
import com.droidblossom.archive.util.CapsuleTypeUtils.stringToEnum

class MyCapsuleRVA(
    private val goDetail: (Long, HomeFragment.CapsuleType) -> Unit,
    private val goSummary: (Long, HomeFragment.CapsuleType) -> Unit
) :
    ListAdapter<MyCapsule, MyCapsuleRVA.ItemViewHolder>(differ) {

    inner class ItemViewHolder(
        private val binding: ItemMyCapsuleBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MyCapsule) {
            binding.item = data
            binding.root.setOnClickListener {
                if (data.isOpened) {
                    goDetail(data.capsuleId, stringToEnum(data.capsuleType))
                } else {
                    goSummary(data.capsuleId, stringToEnum(data.capsuleType))
                }

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemMyCapsuleBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<MyCapsule>() {
            override fun areItemsTheSame(oldItem: MyCapsule, newItem: MyCapsule): Boolean {
                return oldItem.capsuleId == newItem.capsuleId
            }

            override fun areContentsTheSame(oldItem: MyCapsule, newItem: MyCapsule): Boolean {
                return oldItem == newItem
            }
        }
    }
}