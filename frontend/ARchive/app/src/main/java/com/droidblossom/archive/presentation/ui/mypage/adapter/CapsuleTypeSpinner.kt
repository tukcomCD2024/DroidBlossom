package com.droidblossom.archive.presentation.ui.mypage.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ItemSpinnerBinding
import com.droidblossom.archive.databinding.ItemSpinnerDropdownBinding

class CapsuleTypeSpinner(private val context: Context, private val items: Array<String>) : BaseAdapter() {

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
        spinnerItemBinding.tvItemName.text = items[position]

        return spinnerItemBinding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        spinnerDropdownItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_spinner_dropdown, parent, false)
        spinnerDropdownItemBinding.tvDropdownItemName.text = items[position]

        if (items[position] == spinnerItemBinding.tvItemName.text.toString()) {
            spinnerDropdownItemBinding.tvDropdownItemName.setTextColor(Color.parseColor("#ED7A2B"))
        } else {
            spinnerDropdownItemBinding.tvDropdownItemName.setTextColor(Color.parseColor("#000000"))
        }

        if (position == (count - 1)) {
            spinnerDropdownItemBinding.tvDivider.visibility = View.GONE
        }

        return spinnerDropdownItemBinding.root
    }
}