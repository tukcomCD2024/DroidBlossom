package com.droidblossom.archive.presentation.ui.skin.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemSkinMotionBinding
import com.droidblossom.archive.domain.model.capsule_skin.SkinMotion

class SkinMotionRVA(val ItemClick: (previousPosition: Int?, currentPosition: Int) -> Unit) : ListAdapter<SkinMotion, SkinMotionRVA.ItemViewHolder>(differ) {

    private var previousClickedPosition: Int? = null

    inner class ItemViewHolder(
        private val binding: ItemSkinMotionBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SkinMotion) {
            binding.item = data
            binding.root.setOnClickListener {
                val currentClickedPosition = bindingAdapterPosition
                if (currentClickedPosition != RecyclerView.NO_POSITION) {
                    ItemClick(previousClickedPosition, currentClickedPosition)
                    previousClickedPosition?.let { previousClickedPosition -> notifyItemChanged(previousClickedPosition) }
                    notifyItemChanged(currentClickedPosition)
                    previousClickedPosition = currentClickedPosition
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemSkinMotionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: List<SkinMotion>?) {
        previousClickedPosition = null
        super.submitList(list)
    }



    companion object {
        val differ = object : DiffUtil.ItemCallback<SkinMotion>() {
            override fun areItemsTheSame(oldItem: SkinMotion, newItem: SkinMotion): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SkinMotion, newItem: SkinMotion): Boolean {
                return oldItem == newItem
            }
        }
    }
}