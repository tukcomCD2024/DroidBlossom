package com.droidblossom.archive.presentation.ui.home.createcapsule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemGroupBinding
import com.droidblossom.archive.domain.model.group.GroupSummary

class GroupSelectRVA(val GroupFlow: (previousPosition: Int?, currentPosition: Int) -> Unit) :
    ListAdapter<GroupSummary, GroupSelectRVA.ItemViewHolder>(differ) {

    private var previousClickedPosition: Int? = null

    inner class ItemViewHolder(
        private val binding: ItemGroupBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: GroupSummary) {
            binding.item = data
            binding.root.setOnClickListener {
                val currentClickedPosition = bindingAdapterPosition
                if (currentClickedPosition != RecyclerView.NO_POSITION) {
                    GroupFlow(previousClickedPosition, currentClickedPosition)
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
            ItemGroupBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<GroupSummary>() {
            override fun areItemsTheSame(
                oldItem: GroupSummary,
                newItem: GroupSummary
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: GroupSummary,
                newItem: GroupSummary
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}