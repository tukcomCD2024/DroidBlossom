package com.droidblossom.archive.presentation.ui.home.createcapsule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemSkinBinding
import com.droidblossom.archive.domain.model.common.CapsuleSkinSummary

class SkinRVA(val SkinFlow: (previousPosition: Int?, currentPosition: Int) -> Unit) :
    ListAdapter<CapsuleSkinSummary, SkinRVA.ItemViewHolder>(differ) {

    private var previousClickedPosition: Int? = null

    inner class ItemViewHolder(
        private val binding: ItemSkinBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: CapsuleSkinSummary) {
            binding.item = data
            binding.root.setOnClickListener {
                val currentClickedPosition = bindingAdapterPosition
                if (currentClickedPosition != RecyclerView.NO_POSITION) {
                    SkinFlow(previousClickedPosition, currentClickedPosition)
                    previousClickedPosition?.let { previousClickedPosition ->
                        notifyItemChanged(previousClickedPosition)
                    }
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
            ItemSkinBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<CapsuleSkinSummary>() {
            override fun areItemsTheSame(
                oldItem: CapsuleSkinSummary,
                newItem: CapsuleSkinSummary
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CapsuleSkinSummary,
                newItem: CapsuleSkinSummary
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}