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
import com.droidblossom.archive.domain.model.common.ContentType
import com.droidblossom.archive.domain.model.common.Dummy
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.home.createcapsule.adapter.ImageRVA
import com.droidblossom.archive.presentation.ui.home.createcapsule.dialog.DatePickerDialogFragment
import com.droidblossom.archive.util.FileUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class CreateCapsule3Fragment :
    BaseFragment<CreateCapsuleViewModelImpl, FragmentCreateCapsule3Binding>(R.layout.fragment_create_capsule3) {

    override val viewModel: CreateCapsuleViewModelImpl by activityViewModels()

    private val pickMultipleImageAndVideo =
        registerForActivityResult(
            ActivityResultContracts.PickMultipleVisualMedia(5)
        ) { uris ->
            if (uris.isNotEmpty()) {
                val dummyList = mutableListOf<Dummy>()
                uris.forEach { uri ->
                    val mimeType = activity?.contentResolver?.getType(uri)
                    when {
                        mimeType?.startsWith("image/") == true -> {
                            dummyList.add(Dummy(uri, ContentType.IMAGE, false))
                        }

                        mimeType?.startsWith("video/") == true -> {
                            val videoDuration = FileUtils.getVideoDuration(requireContext(),uri)

                            if (videoDuration <= 30 * 1000) {
                                dummyList.add(Dummy(uri, ContentType.VIDEO, false))
                            } else {
                                showToastMessage("30초 이하의 짧은 동영상만 업로드할 수 있어요!")
                            }
                        }

                        else -> {

                        }
                    }
                }
                viewModel.addContentUris(dummyList)
            } else {

            }
        }

    private val pickSingle =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia())
        { uri ->
            if (uri != null) {

                val mimeType = activity?.contentResolver?.getType(uri)
                when {
                    mimeType?.startsWith("image/") == true -> {
                        viewModel.addContentUris(listOf(Dummy(uri, ContentType.IMAGE, false)))
                    }

                    mimeType?.startsWith("video/") == true -> {
                        viewModel.addContentUris(listOf(Dummy(uri, ContentType.VIDEO, false)))
                    }

                    else -> {
                        Log.d("MediaType", "Unknown media type: $uri")
                    }
                }
            } else {

            }
        }

    private val contentVPA by lazy {
        ImageRVA(
            { viewModel.moveSingleImgUpLoad() },
            { viewModel.submitContentUris(it) }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        initRVA()
        initView()
    }

    private fun initRVA() {
        binding.recycleView.adapter = contentVPA
        binding.recycleView.offscreenPageLimit = 3
        binding.indicator.attachTo(binding.recycleView)
    }

    private fun initView() {
        with(binding) {
            capsuleTitleEditT.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    viewModel.closeTimeSetting()
                }
            }

            nextBtn.setOnClickListener {

                if (viewModel.capsuleLocationName.value == "현재 위치를 확인하는 중입니다."){
                    showToastMessage("현재 위치를 확인하는 중입니다. 잠시만 기다려 주세요.")
                    return@setOnClickListener
                }

                if (viewModel.isSelectTimeCapsule.value && (viewModel.capsuleLatitude.value == 0.0 || viewModel.capsuleTitle.value.isEmpty() || viewModel.capsuleContent.value.isEmpty() || viewModel.dueTime.value.isEmpty())) {
                    showToastMessage("타임캡슐은 시간, 제목, 내용이 필수 입니다.")
                    return@setOnClickListener
                }
                if (!viewModel.isSelectTimeCapsule.value && (viewModel.capsuleLatitude.value == 0.0 || viewModel.capsuleTitle.value.isEmpty() || viewModel.capsuleContent.value.isEmpty())) {
                    showToastMessage("캡슐은 제목, 내용이 필수 입니다.")
                    return@setOnClickListener
                }
                showLoading(requireContext())
                val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                val currentTime = dateFormat.format(Date())
                CoroutineScope(Dispatchers.IO).launch {

                    val contentFilesDeferred = async {
                        viewModel.contentUris.value.mapIndexed { index, dummy ->
                            async {
                                dummy.string?.let { uriString ->
                                    when (dummy.contentType) {
                                        ContentType.IMAGE -> FileUtils.resizeBitmapFromUri(
                                            requireContext(),
                                            uriString,
                                            "IMG_${currentTime}_$index"
                                        )

                                        ContentType.VIDEO -> FileUtils.convertUriToVideoFile(
                                            requireContext(),
                                            uriString,
                                            "VID_${currentTime}_$index"
                                        )

                                        else -> null
                                    }
                                }
                            }
                        }.awaitAll().filterNotNull()
                    }

                    val contentFiles = contentFilesDeferred.await()

                    val imageFiles = contentFiles.filter { file ->
                        file.name.startsWith("IMG_")
                    }
                    val videoFiles = contentFiles.filter { file ->
                        file.name.startsWith("VID_")
                    }
                    viewModel.setFiles(imageFiles, videoFiles)
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
//                            pickMultiple.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            pickMultipleImageAndVideo.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageAndVideo
                                )
                            )
                        }

                        CreateCapsuleViewModel.Create3Event.ClickLocation -> {
                            //위치 목록 dialog or page ?
                        }

                        CreateCapsuleViewModel.Create3Event.CLickSingleImgUpLoad -> {
                            pickSingle.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
                        }

                        is CreateCapsuleViewModel.Create3Event.ShowToastMessage -> {
                            showToastMessage(it.message)
                        }


                        CreateCapsuleViewModel.Create3Event.ClickVideoUpLoad -> {}

                        CreateCapsuleViewModel.Create3Event.DismissLoading -> {
                            dismissLoading()
                        }

                        else -> {}
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contentUris.collect {
                    contentVPA.submitList(it)
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
