package com.droidblossom.archive.presentation.ui.mypage.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemMyPageSpinnerBinding
import com.droidblossom.archive.presentation.ui.mypage.MyPageFragment

class SpinnerAdapter(
    private val context: Context,
    private val spinnerItems: Array<MyPageFragment.SpinnerCapsuleType>,
    private val selectedCapsuleType: (MyPageFragment.SpinnerCapsuleType) -> Unit
) : RecyclerView.Adapter<SpinnerAdapter.ItemViewHolder>() {

    private var selectedPosition = 0

    inner class ItemViewHolder(
        private val binding: ItemMyPageSpinnerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(spinnerItems: Array<MyPageFragment.SpinnerCapsuleType>, position: Int) {
            val spinnerAdapter = CapsuleTypeSpinner(context, spinnerItems)
            binding.capsuleTypeSpinner.adapter = spinnerAdapter
            binding.capsuleTypeSpinner.setSelection(selectedPosition, false)
            binding.capsuleTypeSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        selectedCapsuleType(spinnerItems[position])
                        selectedPosition = position
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }
                }
            binding.capsuleTypeSpinner.viewTreeObserver.addOnWindowFocusChangeListener { hasFocus ->
                spinnerAdapter.spinnerIsOpened = hasFocus
                spinnerAdapter.notifyDataSetChanged()
            }
        }
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = MyPageFragment.SPINNER_TYPE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemMyPageSpinnerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(spinnerItems, position)
    }

    override fun getItemCount(): Int = 1
}