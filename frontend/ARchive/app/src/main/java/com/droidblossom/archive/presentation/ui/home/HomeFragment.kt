package com.droidblossom.archive.presentation.ui.home

import android.graphics.Point
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.toPointF
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentHomeBinding
import com.droidblossom.archive.domain.model.common.CapsuleMarker
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.customview.HomeSnackBarBig
import com.droidblossom.archive.presentation.customview.HomeSnackBarSmall
import com.droidblossom.archive.presentation.ui.home.createcapsule.CreateCapsuleActivity
import com.droidblossom.archive.presentation.ui.home.dialog.CapsulePreviewDialogFragment
import com.droidblossom.archive.util.CapsuleTypeUtils
import com.droidblossom.archive.util.LocationUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.Projection
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModelImpl, FragmentHomeBinding>(R.layout.fragment_home),
    OnMapReadyCallback {

    override val viewModel: HomeViewModelImpl by viewModels<HomeViewModelImpl>()

    private lateinit var naverMap: NaverMap
    private lateinit var locationUtil: LocationUtil
    private lateinit var locationSource: FusedLocationSource
    private val markers: MutableList<Marker> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationUtil = LocationUtil(requireContext())
        initView()
        initMap()

        val layoutParams = binding.notificationBtn.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += getStatusBarHeight()
        binding.notificationBtn.layoutParams = layoutParams
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
                        calculateRadiusForZoomLevel(),
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
                viewModel.filterCapsuleSelect.collect {
                    viewModel.resetNearbyCapsules()
                    locationUtil.getCurrentLocation { latitude, longitude ->
                        if (::naverMap.isInitialized) {
                            viewModel.getNearbyCapsules(
                                latitude,
                                longitude,
                                calculateRadiusForZoomLevel(),
                                viewModel.filterCapsuleSelect.value.toString()
                            )
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeEvents.collect { event ->
                    when (event) {

                        is HomeViewModel.HomeEvent.ShowCapsulePreviewDialog -> {
                            val sheet = CapsulePreviewDialogFragment.newInstance(event.capsuleId, event.capsuleType, false)
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
                            naverMap.maxZoom = FIXZOOM
                            naverMap.minZoom = FIXZOOM
                            naverMap.locationOverlay.circleRadius = 100
                            naverMap.locationOverlay.circleOutlineWidth = 1
                            naverMap.locationOverlay.circleColor =
                                ContextCompat.getColor(requireContext(), R.color.main_1_alpha20)
                            naverMap.locationOverlay.circleOutlineColor =
                                ContextCompat.getColor(requireContext(), R.color.main_1)
                            naverMap.locationTrackingMode = LocationTrackingMode.Follow
                        } else {
                            naverMap.locationTrackingMode = LocationTrackingMode.NoFollow
                            naverMap.minZoom = MINZOOM
                            naverMap.maxZoom = MAXZOOM
                            naverMap.locationOverlay.circleRadius = 0

                        }
                    }

                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.capsuleList.collect {
                    removeAllMarkers()
                    it.map { capsule -> addMarker(capsule) }
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
        naverMap.minZoom = MINZOOM
        naverMap.maxZoom = MAXZOOM
        LocationUtil(requireContext()).getCurrentLocation { latitude, longitude ->
            val cameraUpdate = CameraUpdate.scrollTo(LatLng(latitude, longitude))
            naverMap.moveCamera(cameraUpdate)

        }
        with(naverMap.locationOverlay) {
            isVisible = true
            icon = OverlayImage.fromResource(R.drawable.ic_my_location_24)
        }
    }

    private fun addMarker(capsuleMarker: CapsuleMarker) {
        val marker = Marker().apply {
            position = LatLng(capsuleMarker.latitude, capsuleMarker.longitude)
            icon = when (capsuleMarker.capsuleType) {
                CapsuleType.SECRET -> OverlayImage.fromResource(R.drawable.ic_marker_pin_secret)
                CapsuleType.GROUP -> OverlayImage.fromResource(R.drawable.ic_marker_pin_group)
                CapsuleType.PUBLIC -> OverlayImage.fromResource(R.drawable.ic_marker_pin_public)
            }
            map = naverMap

            tag = hashMapOf(
                "id" to capsuleMarker.id.toString(),
                "type" to CapsuleTypeUtils.enumToString(capsuleMarker.capsuleType)
            )

            onClickListener = Overlay.OnClickListener { overlay ->
                val clickedMarker = overlay as Marker
                val markerData = clickedMarker.tag as? HashMap<*, *>
                val capsuleId = markerData?.get("id") as? String
                val capsuleType = markerData?.get("type") as? String
                if (capsuleId != null && capsuleType != null) {
                    viewModel.homeEvent(
                        HomeViewModel.HomeEvent.ShowCapsulePreviewDialog(
                            capsuleId,
                            capsuleType
                        )
                    )
                }
                true
            }
        }
        markers.add(marker)
    }

    private fun removeAllMarkers() {
        markers.forEach { it.map = null }
        markers.clear()
    }

    // 구현은 했는데 이렇게하면 한국 전체에 생성된 캡슐을 찾기가 어려움
    private fun calculateDistanceInKilometers(): Double {
        val projection: Projection = naverMap.projection

        val latLng = projection.fromScreenLocation(Point(0, 0).toPointF())

        val centerLatLng = naverMap.cameraPosition.target

        val distanceInMeters = FloatArray(1)
        Location.distanceBetween(
            centerLatLng.latitude, centerLatLng.longitude,
            latLng.latitude, latLng.longitude,
            distanceInMeters
        )

        return distanceInMeters[0] / 1000.0
    }

    fun calculateRadiusForZoomLevel(): Double {
        val radiusAtMinZoom = 550.0
        val zoomDifference = naverMap.cameraPosition.zoom - MINZOOM
        return radiusAtMinZoom * Math.pow(2.0, -zoomDifference)
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
        const val TAG = "homeFragment"
        fun newIntent() = HomeFragment()

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        const val MAXZOOM = 18.0
        const val MINZOOM = 6.0
        const val FIXZOOM = 14.0
    }

}