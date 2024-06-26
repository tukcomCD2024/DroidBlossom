package com.droidblossom.archive.presentation.ui.mypage.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemMyPageProfileBinding
import com.droidblossom.archive.presentation.ui.mypage.MyPageFragment.Companion.PROFILE_TYPE
import com.droidblossom.archive.presentation.model.mypage.ProfileData

class ProfileRVA(
    private val goGroupList: () -> Unit,
    private val goFriendList: () -> Unit,
    private val goRequestList: () -> Unit,
    private val goSetting: () -> Unit,
    private val tagCopy: (tag:String) -> Unit,
) : ListAdapter<ProfileData, ProfileRVA.ItemViewHolder>(differ) {

    inner class ItemViewHolder(
        private val binding: ItemMyPageProfileBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ProfileData) {
            binding.data = data
            binding.groupLayout.setOnClickListener { goGroupList() }
            binding.friendLayout.setOnClickListener { goFriendList() }
            binding.requestLayout.setOnClickListener { goRequestList() }
            binding.settingBtn.setOnClickListener { goSetting() }
            binding.profileTagT.setOnLongClickListener {
                tagCopy(data.tag)
                true
            }
            binding.tagCopyBtn.setOnClickListener {
                tagCopy(data.tag)
            }
        }
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).profileId
    }

    override fun getItemViewType(position: Int): Int {
        return PROFILE_TYPE
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemMyPageProfileBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<ProfileData>() {
            override fun areItemsTheSame(oldItem: ProfileData, newItem: ProfileData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ProfileData, newItem: ProfileData): Boolean {
                return oldItem == newItem
            }
        }
    }
}