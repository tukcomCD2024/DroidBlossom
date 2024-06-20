package com.droidblossom.archive.presentation.ui.skin.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemMySkinBinding
import com.droidblossom.archive.domain.model.common.CapsuleSkinSummary

class MySkinRVA(
    private val goSkinDetail:(skin:CapsuleSkinSummary) -> Unit
) : ListAdapter<CapsuleSkinSummary, MySkinRVA.ItemViewHolder>(differ) {

    inner class ItemViewHolder(
        private val binding: ItemMySkinBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CapsuleSkinSummary) {
            binding.item = data
            binding.root.setOnClickListener {
                goSkinDetail(data)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemMySkinBinding.inflate(
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
            override fun areItemsTheSame(oldItem: CapsuleSkinSummary, newItem: CapsuleSkinSummary): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CapsuleSkinSummary, newItem: CapsuleSkinSummary): Boolean {
                return oldItem == newItem
            }
        }
    }
}