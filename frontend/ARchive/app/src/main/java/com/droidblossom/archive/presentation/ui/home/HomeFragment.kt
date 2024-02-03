package com.droidblossom.archive.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentHomeBinding
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.snack.HomeSnackBarBig
import com.droidblossom.archive.presentation.snack.HomeSnackBarSmall
import com.droidblossom.archive.presentation.ui.home.createcapsule.CreateCapsuleActivity
import com.droidblossom.archive.presentation.ui.home.dialog.CapsulePreviewDialogFragment
import com.droidblossom.archive.util.LocationUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModelImpl, FragmentHomeBinding>(R.layout.fragment_home),
    OnMapReadyCallback {

    override val viewModel: HomeViewModelImpl by viewModels<HomeViewModelImpl>()

    private lateinit var naverMap: NaverMap
    private lateinit var locationUtil: LocationUtil
    private lateinit var locationSource: FusedLocationSource

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationUtil = LocationUtil(requireContext())
        initView()
        initMap()
    }

    private fun initView() {
        with(binding) {
            vm = viewModel

            makeGroupCapsuleBtn.setOnClickListener {
                startActivity(CreateCapsuleActivity.newIntent(requireContext(), 1))
            }
            makeOpenCapsuleBtn.setOnClickListener {
                startActivity(CreateCapsuleActivity.newIntent(requireContext(), 2))
            }
            makeSecretCapsuleBtn.setOnClickListener {
                startActivity(CreateCapsuleActivity.newIntent(requireContext(), 3))
            }
            snackbarTestBtn.setOnClickListener {
                HomeSnackBarSmall(requireView()).show()
            }
            snackbarBigText.setOnClickListener {
                HomeSnackBarBig(requireView(), "", "").show()
            }
            refreshBtn.setOnClickListener {
                locationUtil.getCurrentLocation { latitude, longitude ->
                    viewModel.getNearbyCapsules(
                        latitude,
                        longitude,
                        500.0,
                        viewModel.filterCapsuleSelect.value.toString()
                    )
                }
            }
        }
    }

    override fun observeData() {
        //FAB 상태
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.filterCapsuleSelect.collect(

                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeEvents.collect { event ->
                    when (event) {

                        is HomeViewModel.HomeEvent.ShowCapsulePreviewDialog -> {
                            val sheet = CapsulePreviewDialogFragment()

                            sheet.show(parentFragmentManager, "CapsulePreviewDialog")
                        }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.followLocation.collect {
                    if (::naverMap.isInitialized) {
                        if (it) {
                            naverMap.maxZoom = 14.0
                            naverMap.minZoom = 14.0
                            naverMap.locationOverlay.circleRadius = 100
                            naverMap.locationOverlay.circleOutlineWidth = 1
                            naverMap.locationOverlay.circleColor =
                                ContextCompat.getColor(requireContext(), R.color.main_1_alpha20)
                            naverMap.locationOverlay.circleOutlineColor =
                                ContextCompat.getColor(requireContext(), R.color.main_1)
                            naverMap.locationTrackingMode = LocationTrackingMode.Follow
                        } else {
                            naverMap.locationTrackingMode = LocationTrackingMode.NoFollow
                            naverMap.minZoom = 6.0
                            naverMap.maxZoom = 18.0
                            naverMap.locationOverlay.circleRadius = 0

                        }
                    }

                }
            }
        }
    }

    private fun initMap() {
        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

    }


    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
        naverMap.minZoom = 6.0
        naverMap.maxZoom = 18.0
        LocationUtil(requireContext()).getCurrentLocation { latitude, longitude ->
            val cameraUpdate = CameraUpdate.scrollTo(LatLng(latitude, longitude))
            naverMap.moveCamera(cameraUpdate)

        }
        with(naverMap.locationOverlay) {
            isVisible = true
            icon = OverlayImage.fromResource(R.drawable.ic_my_location_24)
        }
        addMarker(CapsuleType.SECRET)
        addMarker(CapsuleType.GROUP)
        addMarker(CapsuleType.PUBLIC)
    }

    private fun addMarker(capsuleType: CapsuleType) {
        val randomLatitude = Random.nextDouble(33.0, 38.0)
        val randomLongitude = Random.nextDouble(126.0, 129.0)


        Marker().apply {
            position = LatLng(randomLatitude, randomLongitude)
            icon = when (capsuleType) {
                CapsuleType.SECRET -> OverlayImage.fromResource(R.drawable.ic_marker_pin_secret)
                CapsuleType.GROUP -> OverlayImage.fromResource(R.drawable.ic_marker_pin_group)
                CapsuleType.PUBLIC -> OverlayImage.fromResource(R.drawable.ic_marker_pin_public)
            }
            map = naverMap
            tag = when (capsuleType) {
                CapsuleType.SECRET -> "비밀"
                CapsuleType.GROUP -> "그룹"
                CapsuleType.PUBLIC -> "그룹"
            }
            onClickListener = Overlay.OnClickListener { overlay ->
                val clickedMarker = overlay as Marker
                val markerData = clickedMarker.tag.toString()
                viewModel.homeEvent(HomeViewModel.HomeEvent.ShowCapsulePreviewDialog)
                //Toast.makeText(requireContext(), markerData, Toast.LENGTH_SHORT).show()
                true
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        ) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    enum class CapsuleType {
        SECRET,
        GROUP,
        PUBLIC
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

}