package com.droidblossom.archive.presentation.ui.skin.skinmake

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSkinMakeBinding
import com.droidblossom.archive.domain.model.common.FileName
import com.droidblossom.archive.domain.model.s3.S3UrlRequest
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.MainActivity
import com.droidblossom.archive.presentation.ui.home.createcapsule.CreateCapsuleActivity
import com.droidblossom.archive.presentation.ui.home.createcapsule.CreateCapsuleViewModel
import com.droidblossom.archive.presentation.ui.home.createcapsule.dialog.DatePickerDialogFragment
import com.droidblossom.archive.util.FileUtils
import com.droidblossom.archive.util.S3Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class SkinMakeFragment : BaseFragment<SkinMakeViewModelImpl, FragmentSkinMakeBinding>(R.layout.fragment_skin_make) {

    @Inject lateinit var s3Util: S3Util

    lateinit var navController: NavController
    override val viewModel : SkinMakeViewModelImpl by activityViewModels()

    private val picMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {uri ->
        if (uri != null){
            viewModel.skinImgUri.value = uri
        }else{
            Log.d("포토", "No Media selected")
        }
    }

    override fun observeData() {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.skinMakeEvents.collect {
                    when (it) {
                        is SkinMakeViewModel.SkinMakeEvent.ShowToastMessage -> {
                            showToastMessage(it.message)
                        }
                        is SkinMakeViewModel.SkinMakeEvent.SuccessSkinMake -> {
                            requireActivity().finish()
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        binding.vm = viewModel
        initView()
    }

    private fun initView(){

        with(binding){
            skinImageLayout.setOnClickListener {
                picMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            closeBtn.setOnClickListener {
                MainActivity.goMain(requireContext())
            }

            completeBtn.setOnClickListener {

                if (viewModel.skinName.value.isBlank()){
                    viewModel.skinMakeEvent(SkinMakeViewModel.SkinMakeEvent.ShowToastMessage("스킨의 이름은 필수입니다."))
                    return@setOnClickListener
                }

                if (viewModel.skinImgUri.value == null){
                    viewModel.skinMakeEvent(SkinMakeViewModel.SkinMakeEvent.ShowToastMessage("스킨의 이미지는 필수입니다."))
                    return@setOnClickListener
                }else{

                    val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                    val currentTime = dateFormat.format(Date())

                    CoroutineScope(Dispatchers.IO).launch{
                        val skinImgFilesDeferred = FileUtils.convertUriToJpegFile(requireContext(), viewModel.skinImgUri.value!!, "IMG_${currentTime}")
                        viewModel.setFile(skinImgFilesDeferred!!)

                        viewModel.makeSkin()

                    }

                }

            }

        }

    }

}