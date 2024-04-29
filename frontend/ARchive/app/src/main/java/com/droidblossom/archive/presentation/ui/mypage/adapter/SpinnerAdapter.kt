package com.droidblossom.archive.presentation.ui.mypage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.droidblossom.archive.databinding.ItemMyPageSpinnerBinding
import com.droidblossom.archive.presentation.ui.mypage.MyPageFragment

class SpinnerAdapter(
    private val context: Context,
    private val spinnerItems: Array<MyPageFragment.SpinnerCapsuleType>
) : RecyclerView.Adapter<SpinnerAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(
        private val binding: ItemMyPageSpinnerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(spinnerItems: Array<MyPageFragment.SpinnerCapsuleType>) {
            val spinnerAdapter = CapsuleTypeSpinner(context, spinnerItems)
            binding.capsuleTypeSpinner.adapter = spinnerAdapter
            binding.capsuleTypeSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {

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

    override fun getItemId(position: Int): Long {
        return 1L
    }


    override fun getItemViewType(position: Int): Int {
        return MyPageFragment.SPINNER_TYPE
    }

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
        holder.bind(spinnerItems)
    }

    override fun getItemCount(): Int = 1
}
