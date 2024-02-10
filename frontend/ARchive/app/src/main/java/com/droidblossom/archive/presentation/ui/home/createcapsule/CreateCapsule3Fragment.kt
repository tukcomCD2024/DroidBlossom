package com.droidblossom.archive.presentation.ui.home.createcapsule

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentCreateCapsule3Binding
import com.droidblossom.archive.domain.model.common.Dummy
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.home.createcapsule.adapter.ImageRVA
import com.droidblossom.archive.presentation.ui.home.createcapsule.dialog.DatePickerDialogFragment
import com.droidblossom.archive.util.FileUtils
import com.droidblossom.archive.util.LocationUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class CreateCapsule3Fragment :
    BaseFragment<CreateCapsuleViewModelImpl, FragmentCreateCapsule3Binding>(R.layout.fragment_create_capsule3) {

    override val viewModel: CreateCapsuleViewModelImpl by activityViewModels()


    private val pickMultiple =
        registerForActivityResult(
            ActivityResultContracts.PickMultipleVisualMedia(5)
        ) { uris ->
            if (uris.isNotEmpty()) {
                viewModel.addImgUris(uris.map { Dummy(it, false) })
            } else {
                Log.d("포토", "No media selected")
            }
        }

    private val pickSingle =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia())
        { uri ->
            if (uri != null) {
                viewModel.addImgUris(listOf(Dummy(uri, false)))
            } else {
                Log.d("포토", "No Media selected")
            }
        }


    private val imgVPA by lazy {
        ImageRVA(
            { viewModel.moveSingleImgUpLoad() },
            { viewModel.submitUris(it) }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        initRVA()
        initView()
    }

    private fun initRVA(){
        binding.recycleView.adapter = imgVPA
        binding.recycleView.offscreenPageLimit = 3
        binding.indicator.attachTo(binding.recycleView)
//        val locationUtil = LocationUtil(requireContext())
//        locationUtil.getCurrentLocation { latitude, longitude ->
//            Log.d("위치", "위도 : $latitude, 경도 : $longitude")
//            viewModel.coordToAddress(latitude = latitude, longitude = longitude)
//        }
    }

    private fun initView() {
        with(binding) {
            nextBtn.setOnClickListener {

                val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                val currentTime = dateFormat.format(Date())
                CoroutineScope(Dispatchers.IO).launch {
                    val fileDeferreds = viewModel.imgUris.value.mapIndexedNotNull { index, uri ->
                        uri.string?.let { uriString ->
                            async {
                                FileUtils.convertUriToJpegFile(
                                    requireContext(),
                                    uriString,
                                    "${currentTime}_$index"
                                )
                            }
                        }
                    }
                    val fileList = fileDeferreds.awaitAll().filterNotNull()
                    viewModel.makeFiles(fileList)
                    viewModel.moveFinish()
                }
            }
        }
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.create3Events.collect {
                    when (it) {
                        CreateCapsuleViewModel.Create3Event.ClickDate -> {
                            //날짜 Dialog 디자인?
                            val sheet = DatePickerDialogFragment { time, server ->
                                viewModel.capsuleDueDate.value = time
                                viewModel.getDueTime(server)
                            }
                            sheet.show(
                                (activity as CreateCapsuleActivity).supportFragmentManager,
                                sheet.tag
                            )

                        }

                        CreateCapsuleViewModel.Create3Event.ClickFinish -> {
                            //캡슐 생성 api 추가
                            requireActivity().finish()
                        }

                        CreateCapsuleViewModel.Create3Event.ClickImgUpLoad -> {
                            pickMultiple.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
                        }

                        CreateCapsuleViewModel.Create3Event.ClickLocation -> {
                            //위치 목록 dialog or page ?
                        }

                        CreateCapsuleViewModel.Create3Event.CLickSingleImgUpLoad -> {
                            pickSingle.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }

                        is CreateCapsuleViewModel.Create3Event.ShowToastMessage -> {
                            showToastMessage(it.message)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.imgUris.collect {
                    imgVPA.submitList(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.capsuleDueDate.collect {
                    binding.timeT.text = it
                }
            }
        }
    }
}