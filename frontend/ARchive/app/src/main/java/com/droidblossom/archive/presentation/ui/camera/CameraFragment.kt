package com.droidblossom.archive.presentation.ui.camera

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentCameraBinding
import com.droidblossom.archive.domain.model.common.CapsuleMarker
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.home.dialog.CapsulePreviewDialogFragment
import com.droidblossom.archive.util.CameraManager
import com.droidblossom.archive.util.FragmentManagerProvider
import com.droidblossom.archive.util.LocationUtil
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.rendering.ViewAttachmentManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.sceneview.ar.node.AnchorNode
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CameraFragment :
    BaseFragment<CameraViewModelImpl, FragmentCameraBinding>(R.layout.fragment_camera),
    FragmentManagerProvider {

    override val viewModel: CameraViewModelImpl by viewModels<CameraViewModelImpl>()

    // private val capsules: MutableList<CapsuleMarker> = mutableListOf()
    private var anchorNode: AnchorNode? = null
    private lateinit var session: Session
    private lateinit var config: Config
    private lateinit var viewAttachmentManager: ViewAttachmentManager

    override fun provideFragmentManager(): FragmentManager {
        return parentFragmentManager
    }

    override fun observeData() {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cameraEvents.collect { event ->
                    when (event) {
                        is CameraViewModel.CameraEvent.ShowCapsulePreviewDialog -> {
                            val sheet = CapsulePreviewDialogFragment.newInstance(
                                event.capsuleId,
                                event.capsuleType
                            )
                            sheet.show(parentFragmentManager, "CapsulePreviewDialog")
                        }

                        else -> {}
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.capsuleList.collect { capsuleList ->
                    binding.sceneView.onSessionUpdated = { session, frame ->
                        if (anchorNode == null) {
                            Log.d("CameraFragmentAR", "earth setting start")
                            val earth = session.earth
                            if (earth == null) {
                                Log.d("CameraFragmentAR", "earth is null")
                            } else {
                                Log.d(
                                    "CameraFragmentAR",
                                    "earth.trackingState = ${earth.cameraGeospatialPose.latitude}"
                                )
                                if (earth.trackingState == TrackingState.TRACKING) {
                                    capsuleList.forEach { capsule ->
                                        val latitude = capsule.latitude
                                        val longitude = capsule.longitude
                                        val altitude = earth.cameraGeospatialPose.altitude

                                        val earthAnchor = earth.createAnchor(
                                            latitude,
                                            longitude,
                                            altitude,
                                            0f,
                                            0f,
                                            0f,
                                            0f
                                        )
                                        addAnchorNode(earthAnchor, capsule)
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    val permissionList = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val locationUtil = LocationUtil(requireContext())
        locationUtil.getCurrentLocation { latitude, longitude ->
            viewModel.getCapsules(latitude = latitude, longitude = longitude)
        }
        viewAttachmentManager = ViewAttachmentManager(
            binding.sceneView.context,
            binding.sceneView
        )
        initView()
        createSession()

    }

    private fun initView() {
        with(binding) {
            sceneView.configureSession { session, config ->
                config.geospatialMode = Config.GeospatialMode.ENABLED
                config.planeFindingMode = Config.PlaneFindingMode.DISABLED
            }
        }

    }


    private fun addAnchorNode(anchor: Anchor, capsule: CapsuleMarker) {
        Log.d("CameraFragmentAR", "addAnchorNode added")
        val arContentNode =
            binding.sceneView.let { scenview ->
                viewAttachmentManager.let { attachManager ->
                    ARContentNode(scenview, attachManager, this, capsule,layoutInflater, requireContext() ,onLoaded = { viewNode ->
                        binding.sceneView.engine.let {
                            AnchorNode(it, anchor)
                                .apply {
                                    isEditable = true
                                    lifecycleScope.launch {
                                        addChildNode(viewNode)
                                    }
                                    anchorNode = this
                                }
                        }.let {
                            binding.sceneView.addChildNode(it)
                        }
                    })
                }
            }
    }

    private fun createSession() {
        session = Session(requireContext())
        config = Config(session)
        config.geospatialMode = Config.GeospatialMode.ENABLED
        session.configure(config)
    }

    override fun onResume() {
        super.onResume()
        viewAttachmentManager.onResume()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            viewAttachmentManager.onPause()
            lifecycleScope.launch {
                CameraManager.getCameraInstance()
                CameraManager.releaseCamera()
            }
        } else {
            viewAttachmentManager.onResume()
        }
    }

    companion object {

        const val TAG = "CAMERA"
        fun newIntent() = CameraFragment()
    }
}