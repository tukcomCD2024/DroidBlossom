package com.droidblossom.archive.presentation.ui.camera

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentCameraBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.util.LocationUtil
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.Plane
import com.google.ar.core.Session
import com.google.ar.core.TrackingState
import dagger.hilt.android.AndroidEntryPoint
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.node.ModelNode
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@AndroidEntryPoint
class CameraFragment :
    BaseFragment<CameraViewModelImpl, FragmentCameraBinding>(R.layout.fragment_camera) {

    override val viewModel: CameraViewModelImpl by viewModels<CameraViewModelImpl>()

    // private val capsules: MutableList<CapsuleMarker> = mutableListOf()
    private var anchorNode: AnchorNode? = null
    private lateinit var session: Session
    private lateinit var config: Config

    override fun observeData() {
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
                                        addAnchorNode(earthAnchor)
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

    private fun addAnchorNode(anchor: Anchor) {
        Log.d("CameraFragmentAR", "addAnchorNode added")
        val anchorNode = AnchorNode(binding.sceneView.engine, anchor)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                binding.sceneView.modelLoader.loadModelInstance("https://sceneview.github.io/assets/models/DamagedHelmet.glb").let { modelInstance ->
                    modelInstance?.let {
                        val modelNode = ModelNode(modelInstance = it).apply {
                            playAnimation(0)
                        }
                        anchorNode.addChildNode(modelNode)
                    }
                }
            }
        }

        binding.sceneView.addChildNode(anchorNode)
        this.anchorNode = anchorNode
    }


    private fun createSession() {
        session = Session(requireContext())
        config = Config(session)
        config.geospatialMode = Config.GeospatialMode.ENABLED
        session.configure(config)
    }

    companion object {

        const val TAG = "CAMERA"
        fun newIntent() = CameraFragment()
    }
}