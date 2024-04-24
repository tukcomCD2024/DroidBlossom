package com.droidblossom.archive.presentation.ui.mypage.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ItemSpinnerBinding
import com.droidblossom.archive.databinding.ItemSpinnerDropdownBinding
import com.droidblossom.archive.presentation.ui.mypage.MyPageFragment

class CapsuleTypeSpinner(private val context: Context, private val items: Array<MyPageFragment.SpinnerCapsuleType>) : BaseAdapter() {

    var spinnerIsOpened = false
    private lateinit var spinnerItemBinding: ItemSpinnerBinding
    private var selectedItemDescription: String? = null

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): MyPageFragment.SpinnerCapsuleType = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        spinnerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_spinner, parent, false)
        spinnerItemBinding.spinnerItemName.text = getItem(position).description
        selectedItemDescription = getItem(position).description
        return spinnerItemBinding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {

        if (spinnerIsOpened) {
            spinnerItemBinding.spinnerItemName.setBackgroundResource(R.drawable.spinner_background_close)
        } else {
            spinnerItemBinding.spinnerItemName.setBackgroundResource(R.drawable.spinner_background_open)
        }

        val isLastVisibleItem = (position == items.indices.last { getItem(it).description != selectedItemDescription })

        if (getItem(position).description == selectedItemDescription) {
            val view = convertView ?: View(context)
            view.visibility = View.GONE
            return view
        } else {
            val binding: ItemSpinnerDropdownBinding = if (convertView == null || convertView.tag == null) {
                DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_spinner_dropdown, parent, false)
            } else {
                convertView.tag as ItemSpinnerDropdownBinding
            }

            binding.dropdownItemName.text = getItem(position).description

            if (isLastVisibleItem) {
                binding.root.setBackgroundResource(R.drawable.spinner_dropdown_item_background_last)
            } else {
                binding.root.setBackgroundResource(R.drawable.spinner_dropdown_item_background)
            }

            if (getItem(position).description != selectedItemDescription) {
                binding.dropdownItemName.setTextColor(ContextCompat.getColor(context, R.color.gray_300))
            }

            binding.root.tag = binding
            binding.root.visibility = View.VISIBLE
            return binding.root
        }
    }
}