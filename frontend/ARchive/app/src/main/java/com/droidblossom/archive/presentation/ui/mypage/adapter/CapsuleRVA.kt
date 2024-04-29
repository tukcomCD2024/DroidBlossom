package com.droidblossom.archive.presentation.ui.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemMyPageCapsuleBinding
import com.droidblossom.archive.presentation.ui.mypage.MyPageFragment.Companion.CAPSULE_TYPE
import com.droidblossom.archive.presentation.model.mypage.CapsuleData

class CapsuleRVA(

) :
    ListAdapter<CapsuleData, CapsuleRVA.ItemViewHolder>(differ) {

    inner class ItemViewHolder(
        private val binding: ItemMyPageCapsuleBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CapsuleData) {
            binding.data = data
            binding.root.setOnClickListener {


            }
        }
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).capsuleId
    }


    override fun getItemViewType(position: Int): Int {
        return CAPSULE_TYPE
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemMyPageCapsuleBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<CapsuleData>() {
            override fun areItemsTheSame(oldItem: CapsuleData, newItem: CapsuleData): Boolean {
                return oldItem.capsuleId == newItem.capsuleId
            }

            override fun areContentsTheSame(oldItem: CapsuleData, newItem: CapsuleData): Boolean {
                return oldItem == newItem
            }
        }
    }
}