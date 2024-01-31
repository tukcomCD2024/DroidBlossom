package com.droidblossom.archive.presentation.ui.home.createcapsule.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentDatePickerDialogBinding
import com.droidblossom.archive.presentation.base.BaseDialogFragment
import com.droidblossom.archive.presentation.ui.home.createcapsule.CreateCapsuleViewModel
import com.droidblossom.archive.presentation.ui.home.createcapsule.CreateCapsuleViewModelImpl
import com.droidblossom.archive.util.DateUtils
import kotlinx.coroutines.launch

class DatePickerDialogFragment(private val onClick: (String) -> Unit): BaseDialogFragment<FragmentDatePickerDialogBinding>(R.layout.fragment_date_picker_dialog) {

    private val viewModel: CreateCapsuleViewModel by viewModels<CreateCapsuleViewModelImpl>()
    private val currentYear = DateUtils.getCurrentYear()
    private val currentMonth = DateUtils.getCurrentMonth()
    private val currentDay = DateUtils.getCurrentDay()
//    private val currentHour = DateUtils.getCurrentHour()
//    private val currentMin = DateUtils.getCurrentMin()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        binding.yearPicker.apply {
            minValue = viewModel.year.value
            maxValue = viewModel.year.value+100
        }
        binding.yearPicker.setOnValueChangedListener { _, oldVal, newVal ->
            viewModel.year.value =  newVal
        }
        binding.monthPicker.apply {
            minValue = 1
            maxValue = 12
        }
        binding.monthPicker.setOnValueChangedListener { _, oldVal, newVal ->
            viewModel.month.value =  newVal
        }
        binding.dayPicker.apply {
            minValue = 1
            maxValue = 31
            value = viewModel.day.value
        }
        binding.dayPicker.setOnValueChangedListener { _, oldVal, newVal ->
            viewModel.day.value =  newVal
        }
        binding.hourPicker.apply {
            minValue = 0
            maxValue = 24
            value = viewModel.hour.value
        }
        binding.hourPicker.setOnValueChangedListener { _, oldVal, newVal ->
            viewModel.hour.value =  newVal
        }
        binding.minPicker.apply {
            minValue = 0
            maxValue = 59
            value = viewModel.min.value
        }
        binding.minPicker.setOnValueChangedListener { _, oldVal, newVal ->
            viewModel.min.value =  newVal
        }

        binding.clickBtn.setOnClickListener {
            if (!viewModel.isSelectTime.value){
                viewModel.goSelectTime()
            }else{
                onClick("${viewModel.year.value}/${viewModel.month.value}/${viewModel.day.value}  ${viewModel.hour.value}:${viewModel.min.value}")
                this.dismiss()
            }
        }



        initObserver()

    }

    private fun initObserver(){

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.year.collect{
                    binding.dayPicker.apply {
                        minValue = if(viewModel.year.value == currentYear && viewModel.month.value == currentMonth) {
                            currentDay
                        }else{
                            1
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.month.collect{
                    binding.dayPicker.apply {
                        if(viewModel.year.value == currentYear && viewModel.month.value == currentMonth) {
                            minValue = currentDay
                            when(viewModel.month.value){
                                1,3,5,7,8,10,12->{
                                    maxValue = 31
                                }
                                4,6,9,11->{
                                    maxValue = 30
                                }
                                2->{
                                    maxValue = 28
                                }
                            }
                        }else{
                            minValue = 1
                            when(viewModel.month.value){
                                1,3,5,7,8,10,12->{
                                    maxValue = 31
                                }
                                4,6,9,11->{
                                    maxValue = 30
                                }
                                2->{
                                    maxValue = 28
                                }
                            }
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.day.collect{
                    if (viewModel.year.value == currentYear && viewModel.month.value == currentMonth && viewModel.day.value == currentDay){
                        binding.hourPicker.apply {
                            minValue = viewModel.hour.value
                            maxValue = 23
                        }
                        binding.minPicker.apply {
                            minValue = viewModel.min.value
                            maxValue = 59
                        }
                    }
                }
            }
        }
    }

}