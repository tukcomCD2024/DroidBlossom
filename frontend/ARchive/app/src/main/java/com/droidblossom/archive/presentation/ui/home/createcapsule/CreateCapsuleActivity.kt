package com.droidblossom.archive.presentation.ui.home.createcapsule

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.findNavController
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ActivityCreateCapsuleBinding
import com.droidblossom.archive.presentation.base.BaseActivity
import com.droidblossom.archive.util.LocationUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCapsuleActivity : BaseActivity<CreateCapsuleViewModelImpl, ActivityCreateCapsuleBinding>(R.layout.activity_create_capsule) {

    override val viewModel: CreateCapsuleViewModelImpl  by viewModels()

    override fun observeData() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.closeBtn.setOnClickListener {
            finish()
        }

        (intent.getIntExtra(CREATE_CAPSULE,1)).let {
            viewModel.groupTypeInt = it
            viewModel.choseCapsuleType(it)
        }

        val locationUtil = LocationUtil(this)
        locationUtil.getCurrentLocation { latitude, longitude ->
            Log.d("위치", "위도 : $latitude, 경도 : $longitude")
            viewModel.coordToAddress(latitude = latitude, longitude = longitude)
        }
        viewModel.getSkinList()

    }
    companion object {
        const val CREATE_CAPSULE = "create_capsule"

        fun newIntent(context: Context,capsuleType : Int) =
            Intent(context, CreateCapsuleActivity::class.java).apply {
                putExtra(CREATE_CAPSULE, capsuleType)
            }
    }
}