package com.droidblossom.archive.presentation.ui.skin.skinmake

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentSkinMakeBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.MainActivity
import com.droidblossom.archive.util.FileUtils
import com.droidblossom.archive.util.LocationUtil
import com.droidblossom.archive.util.S3Util
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class SkinMakeFragment : BaseFragment<SkinMakeViewModelImpl, FragmentSkinMakeBinding>(R.layout.fragment_skin_make) {

    @Inject lateinit var s3Util: S3Util

    lateinit var navController: NavController
    override val viewModel : SkinMakeViewModelImpl by activityViewModels()

    private val picMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {uri ->
        if (uri != null){
            viewModel.imgUri.value = uri
        }else{
            Log.d("포토", "No Media selected")
        }
    }

    override fun observeData() {}

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
                val file = viewModel.imgUri.value?.let { uri ->
                    FileUtils.convertUriToJpegFile(requireContext(),
                        uri, "우하하하")
                }

                val fileName = "테스트"

                //s3Util.uploadFile(fileName, file!!)

//                val locationUtil = LocationUtil(requireContext())
//                locationUtil.getCurrentLocation { latitude, longitude ->
//                    // 여기서 위도(latitude)와 경도(longitude)를 사용하여 필요한 작업 수행
//                    Log.d("위치", "위도 : $latitude, 경도 : $longitude")
//                }

            }

        }

    }

}