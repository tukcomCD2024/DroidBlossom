package com.droidblossom.archive.presentation.ui.home.createcapsule.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.droidblossom.archive.ARchiveApplication
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ItemPostImageBinding
import com.droidblossom.archive.domain.model.common.ContentType
import com.droidblossom.archive.domain.model.common.Dummy
import com.droidblossom.archive.util.FileUtils

class ImageRVA(val onClick: () -> Unit, val flowData: (List<Dummy>) -> Unit) :
    ListAdapter<Dummy, ImageRVA.ItemViewHolder>(differ) {

    inner class ItemViewHolder(
        private val binding: ItemPostImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Dummy) {
            //binding.currentImgT.text = String.format(ARchiveApplication.getString(R.string.imgCount),absoluteAdapterPosition+1 ,currentList.count { !it.last })
            if (data.last) {
                binding.root.setOnClickListener {
                    onClick()
                }
                binding.postImg.setImageResource(R.color.Transparency)
                binding.plusImg.isVisible = true
                binding.plusV.isVisible = true
            } else {
                when (data.contentType) {
                    ContentType.IMAGE -> {
                        Log.d("머냐","왜 안됨?")
                        data.string?.let { _ ->
                            binding.postImg.setImageURI(data.string)
                        }
                        binding.plusImg.isGone = true
                        binding.plusV.isGone = true
                        binding.videoT.isGone = true
                        //binding.currentImgT.isVisible =true
                    }
                    ContentType.VIDEO -> {
                        data.string?.let { _ ->
                            Glide.with(binding.postImg.context)
                                .asBitmap()
                                .load(data.string)
                                .apply(RequestOptions().frame(0))
                                .into(binding.postImg)
                        }
                        binding.plusImg.isGone = true
                        binding.plusV.isGone = true
                        binding.videoT.isVisible = true
                        //binding.currentImgT.isVisible =true
                    }
                    else -> {
                        // 다른 콘텐츠 타입에 대한 처리
                    }
                }
                binding.root.setOnClickListener {
                    removeItem(position)
                }
            }
        }
    }

    fun removeItem(position: Int) {
        val currentList = currentList.toMutableList()
        currentList.removeAt(position)
        submitList(currentList)
        flowData(currentList)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemPostImageBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<Dummy>() {
            override fun areItemsTheSame(oldItem: Dummy, newItem: Dummy): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Dummy, newItem: Dummy): Boolean {
                return oldItem == newItem
            }
        }
    }
}