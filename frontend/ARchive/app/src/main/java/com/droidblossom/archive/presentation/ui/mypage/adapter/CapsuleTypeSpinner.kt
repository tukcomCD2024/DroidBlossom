package com.droidblossom.archive.presentation.ui.mypage.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Outline
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ItemSpinnerBinding
import com.droidblossom.archive.databinding.ItemSpinnerDropdownBinding

class CapsuleTypeSpinner(private val context: Context, private val items: Array<String>) : BaseAdapter() {

    var spinnerIsOpened = false
    private lateinit var spinnerItemBinding: ItemSpinnerBinding

    private lateinit var spinnerDropdownItemBinding: ItemSpinnerDropdownBinding

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): String {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        spinnerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_spinner, parent, false)
        spinnerItemBinding.spinnerItemName.text = items[position]

        return spinnerItemBinding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        spinnerDropdownItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_spinner_dropdown, parent, false)
        spinnerDropdownItemBinding.dropdownItemName.text = items[position]

        if (items[position] == spinnerItemBinding.spinnerItemName.text.toString()) {
            spinnerDropdownItemBinding.dropdownItemName.setTextColor(Color.WHITE)
        } else {
            spinnerDropdownItemBinding.dropdownItemName.setTextColor(ContextCompat.getColor(context, R.color.gray_300))
        }

        if (position == (count - 1)) {
            spinnerDropdownItemBinding.divider.visibility = View.GONE
            spinnerDropdownItemBinding.root.setBackgroundResource(R.drawable.spinner_dropdown_item_last)

        }

        if (spinnerIsOpened){
            spinnerItemBinding.spinnerItemName.setBackgroundResource(R.drawable.spinner_background_close)
        } else{
            spinnerItemBinding.spinnerItemName.setBackgroundResource(R.drawable.spinner_background_open)
        }

        return spinnerDropdownItemBinding.root
    }
}