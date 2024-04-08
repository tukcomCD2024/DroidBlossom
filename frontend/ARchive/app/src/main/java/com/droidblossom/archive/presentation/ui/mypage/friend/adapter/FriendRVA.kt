package com.droidblossom.archive.presentation.ui.mypage.friend.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.gesture.Gesture
import android.gesture.GestureOverlayView
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemFriendBinding
import com.droidblossom.archive.domain.model.friend.Friend


class FriendRVA(private val context: Context, private val click: (Int?,Int) -> Unit) :
    ListAdapter<Friend, FriendRVA.ItemViewHolder>(differ) {

    private var previousClickedPosition: Int? = null

    inner class ItemViewHolder(
        private val binding: ItemFriendBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ClickableViewAccessibility")
        fun bind(data: Friend) {
            binding.item = data

            val detector = GestureDetector(context, object : GestureDetector.OnGestureListener {
                override fun onDown(p0: MotionEvent) = true
                override fun onShowPress(p0: MotionEvent) = Unit
                override fun onSingleTapUp(p0: MotionEvent) = true
                override fun onScroll(
                    p0: MotionEvent?, p1: MotionEvent,
                    p2: Float, p3: Float
                ): Boolean {
                    val currentClickedPosition = bindingAdapterPosition
                    if (currentClickedPosition != RecyclerView.NO_POSITION) {
                        click(previousClickedPosition, currentClickedPosition)
                        previousClickedPosition?.let { previousClickedPosition ->
                            notifyItemChanged(previousClickedPosition)
                        }
                        notifyItemChanged(currentClickedPosition)
                        previousClickedPosition = currentClickedPosition
                    }
                    Log.d("아이템 ", "onScroll() called : $p0")
                    return true
                }
                override fun onLongPress(p0: MotionEvent) = Unit
                override fun onFling(p0: MotionEvent?, p1: MotionEvent, p2: Float, p3: Float) = true
            })
            binding.root.setOnTouchListener { _, event ->
                detector.onTouchEvent(event)
                return@setOnTouchListener false
            }


            binding.root.setOnGenericMotionListener { _, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_SCROLL -> {
                        Log.d("아이탬", "tqtqt")
                    }
                }

                return@setOnGenericMotionListener false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemFriendBinding.inflate(
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
        val differ = object : DiffUtil.ItemCallback<Friend>() {
            override fun areItemsTheSame(
                oldItem: Friend,
                newItem: Friend
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Friend,
                newItem: Friend
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}