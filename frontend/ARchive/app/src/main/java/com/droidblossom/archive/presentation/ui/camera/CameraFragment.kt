package com.droidblossom.archive.presentation.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentCameraBinding
import com.droidblossom.archive.domain.model.capsule.CapsuleAnchor
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.customview.PermissionDialogButtonClickListener
import com.droidblossom.archive.presentation.customview.PermissionDialogFragment
import com.droidblossom.archive.presentation.ui.MainActivity
import com.droidblossom.archive.presentation.ui.home.dialog.CapsulePreviewDialogFragment
import com.droidblossom.archive.util.CustomLifecycleOwner
import com.droidblossom.archive.util.FragmentManagerProvider
import com.droidblossom.archive.util.LocationUtil
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.rendering.ViewAttachmentManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.node.AnchorNode
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CameraFragment :
    BaseFragment<CameraViewModelImpl, FragmentCameraBinding>(R.layout.fragment_camera),
    FragmentManagerProvider {

    override val viewModel: CameraViewModelImpl by viewModels<CameraViewModelImpl>()
    lateinit var arSceneView: ARSceneView
    private lateinit var session: Session
    private lateinit var config: Config
    private lateinit var viewAttachmentManager: ViewAttachmentManager
    private val locationUtil by lazy { LocationUtil(requireContext()) }

    private val visibleLifecycleOwner: CustomLifecycleOwner by lazy {
        CustomLifecycleOwner()
    }

    private val arPermissionList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions.all { it.value } -> {
                    locationUtil.getCurrentLocation { latitude, longitude ->
                        viewModel.getCapsules(latitude = latitude, longitude = longitude)
                    }
                }

                permissions.none { it.value } -> {
                    handleAllPermissionsDenied()
                }

                else -> {
                    handlePartialPermissionsDenied(permissions)
                }
            }
        }

    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                viewAttachmentManager.onResume()
                if (this.isHidden) {
                    onHidden()
                }
                requestPermissionLauncher.launch(arPermissionList)
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    showToastMessage("AR 기능을 사용하려면 카메라 권한이 필요합니다.")
                } else {
                    requestPermissionLauncher.launch(arPermissionList)
                }

            }
        }

    private fun handleAllPermissionsDenied() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ||
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) ||
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
        ) {
            showToastMessage("AR 기능을 사용하려면 카메라, 위치 권한이 필요합니다.")
        } else {
            showSettingsDialog(
                PermissionDialogFragment.PermissionType.AR,
                object : PermissionDialogButtonClickListener {
                    override fun onLeftButtonClicked() {
                        showToastMessage("AR 기능을 사용하려면 카메라, 위치 권한이 필요합니다.")
                        //requireActivity().finish()

                    }

                    override fun onRightButtonClicked() {
                        navigateToAppSettings { requestPermissionLauncher.launch(arPermissionList) }
                    }

                })
        }
    }

    private fun handlePartialPermissionsDenied(permissions: Map<String, Boolean>) {
        permissions.forEach { (permission, granted) ->
            if (!granted) {
                when (permission) {
                    Manifest.permission.CAMERA -> showPermissionDialog(PermissionDialogFragment.PermissionType.CAMERA)
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION -> {
                        if (!permissions.getValue(Manifest.permission.ACCESS_FINE_LOCATION) ||
                            !permissions.getValue(Manifest.permission.ACCESS_COARSE_LOCATION)
                        ) {
                            showPermissionDialog(PermissionDialogFragment.PermissionType.LOCATION)
                        }
                    }
                }
            }
        }
    }

    private fun showPermissionDialog(permissionType: PermissionDialogFragment.PermissionType) {

        if (shouldShowRequestPermissionRationale(permissionType.toString())) {
            showToastMessage("AR 기능을 사용하려면 ${permissionType.description} 권한이 필요합니다.")
        } else {
            showSettingsDialog(permissionType, object : PermissionDialogButtonClickListener {
                override fun onLeftButtonClicked() {
                    showToastMessage("AR 기능을 사용하려면 ${permissionType.description} 권한이 필요합니다.")
                    //requireActivity().finish()
                }

                override fun onRightButtonClicked() {
                    navigateToAppSettings { requestPermissionLauncher.launch(arPermissionList) }
                }

            })
        }

    }


    override fun provideFragmentManager(): FragmentManager {
        return parentFragmentManager
    }

    override fun observeData() {
        visibleLifecycleOwner.lifecycleScope.launch {
            visibleLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cameraEvents.collect { event ->
                    when (event) {
                        is CameraViewModel.CameraEvent.ShowCapsulePreviewDialog -> {

                            val existingDialog =
                                parentFragmentManager.findFragmentByTag(CapsulePreviewDialogFragment.TAG) as DialogFragment?
                            if (existingDialog == null) {
                                val dialog = CapsulePreviewDialogFragment.newInstance(
                                    "-1",
                                    event.capsuleId,
                                    event.capsuleType,
                                    true
                                )
                                dialog.show(parentFragmentManager, CapsulePreviewDialogFragment.TAG)
                            }

                        }

                        is CameraViewModel.CameraEvent.ShowLoading -> {
                            showLoading(requireContext())
                        }

                        is CameraViewModel.CameraEvent.DismissLoading -> {
                            dismissLoading()
                        }

                        else -> {}
                    }
                }
            }
        }

        visibleLifecycleOwner.lifecycleScope.launch {
            visibleLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.anchorNodes
                    .filter { anchorNodes ->
                        anchorNodes.size == viewModel.capsuleListSize
                    }
                    .collect {
                        dismissLoading()
                        showToastMessage("${it.size}개의 캡슐을 찾았습니다.")
                    }
            }
        }

        visibleLifecycleOwner.lifecycleScope.launch {
            visibleLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.capsuleList.collect { capsuleList ->
                    arSceneView.onSessionUpdated = { session, frame ->
                        if (viewModel.capsuleList.value.isNotEmpty() && !viewModel.isCapsulesAdded) {
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
                                    viewModel.isCapsulesAdded = true
                                }
                            }
                        }

                    }
                }
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(requireContext())
        binding.vm = viewModel
        binding.view = this
        arSceneView = binding.sceneView

        viewAttachmentManager = ViewAttachmentManager(
            arSceneView.context,
            arSceneView
        )

        val layoutParams = binding.filterAll.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.filterAll.layoutParams = layoutParams

        initCustomLifeCycle()

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            initView()
            createSession()
        } else {
            MainActivity.goMain(requireContext())
        }
    }

    private fun initView() {
        arSceneView.configureSession { session, config ->
            config.geospatialMode = Config.GeospatialMode.ENABLED
            config.planeFindingMode = Config.PlaneFindingMode.DISABLED
        }

    }


    private fun addAnchorNode(anchor: Anchor, capsule: CapsuleAnchor) {
        Log.d("CameraFragmentAR", "${capsule.id} addAnchorNode added")
        arSceneView.let { sceneView ->
            viewAttachmentManager.let { attachManager ->
                ARContentNode(
                    sceneView,
                    attachManager,
                    this,
                    capsule,
                    layoutInflater,
                    requireContext(),
                    onLoaded = { viewNode ->
                        sceneView.engine.let {
                            AnchorNode(it, anchor).apply {
                                isEditable = true
                                lifecycleScope.launch {
                                    addChildNode(viewNode)
                                }
                                viewModel.addAnchorNode(this)
                            }
                        }.let {
                            sceneView.addChildNode(it)
                        }
                    }
                )
            }
        }

    }

    private fun createSession() {
        session = Session(requireContext())
        config = Config(session)
        config.geospatialMode = Config.GeospatialMode.ENABLED
        session.configure(config)
    }

    private fun initCustomLifeCycle() {
        viewLifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_START,
                    Lifecycle.Event.ON_CREATE,
                    Lifecycle.Event.ON_RESUME,
                    Lifecycle.Event.ON_PAUSE,
                    -> {
                        if (isHidden) {
                            visibleLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
                        } else {
                            visibleLifecycleOwner.handleLifecycleEvent(event)
                        }
                    }

                    else -> {
                        visibleLifecycleOwner.handleLifecycleEvent(event)
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            onHidden()
        } else {
            onShow()
        }
    }

    private fun onHidden() {
        dismissLoading()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            arSceneView.clearChildNodes()
            viewModel.clearAnchorNode()
            viewAttachmentManager.onPause()
            arSceneView.session?.pause()
        }
    }

    private fun onShow() {
        visibleLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_START)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            arSceneView.session?.resume()
            viewAttachmentManager.onResume()
            requestPermissionLauncher.launch(arPermissionList)
        }
    }

    fun onClickFilter(capsuleFilterType: CameraViewModel.CapsuleFilterType) {
        showLoading(requireContext())
        arSceneView.clearChildNodes()
        viewModel.clearAnchorNode()
        locationUtil.getCurrentLocation { latitude, longitude ->
            viewModel.selectFilter(capsuleFilterType, latitude = latitude, longitude = longitude)
        }
    }

    companion object {

        const val TAG = "CAMERA"
        fun newIntent() = CameraFragment()
    }

}