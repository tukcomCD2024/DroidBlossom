package com.droidblossom.archive.presentation.ui.home

import android.graphics.Point
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.droidblossom.archive.presentation.ui.home.createcapsule.CreateCapsuleActivity
import com.droidblossom.archive.presentation.ui.home.dialog.CapsulePreviewDialogFragment
import com.droidblossom.archive.presentation.ui.home.notification.NotificationActivity
import com.droidblossom.archive.util.CapsuleTypeUtils
import com.droidblossom.archive.util.LocationUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
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
import ted.gun0912.clustering.naver.TedNaverClustering

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModelImpl, FragmentHomeBinding>(R.layout.fragment_home),
    OnMapReadyCallback {

    override val viewModel: HomeViewModelImpl by viewModels<HomeViewModelImpl>()

    private lateinit var naverMap: NaverMap
    private lateinit var locationUtil: LocationUtil
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverCluster: TedNaverClustering<MapCapsuleMarker>
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
            refreshBtn.setOnClickListener {
                fetchCapsulesInCameraFocus()
            }

        }
    }

    override fun observeData() {
        //FAB 상태
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.filterCapsuleSelect.collect {
                    viewModel.resetNearbyCapsules()
                    if (::naverMap.isInitialized) {
                        fetchCapsulesInCameraFocus()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeEvents.collect { event ->
                    when (event) {
                        HomeViewModel.HomeEvent.GoNotification -> {
                            startActivity(NotificationActivity.newIntent(requireContext()))
                        }

                        is HomeViewModel.HomeEvent.ShowCapsulePreviewDialog -> {
                            val sheet = CapsulePreviewDialogFragment.newInstance(
                                "-1",
                                event.capsuleId,
                                event.capsuleType,
                                false
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
                    if (::naverMap.isInitialized) {
                        naverCluster.clearItems()
                        naverCluster.addItems(getItems(it))
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
        naverMap.uiSettings.isRotateGesturesEnabled = false
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
        naverMap.minZoom = MINZOOM
        naverMap.maxZoom = MAXZOOM

        val southWest = LatLng(MINLAT, MINLNG)
        val northEast = LatLng(MAXLAT, MAXLNG)
        val koreaBounds = LatLngBounds(southWest, northEast)
        naverMap.extent = koreaBounds

        LocationUtil(requireContext()).getCurrentLocation { latitude, longitude ->
            val cameraUpdate = CameraUpdate.scrollTo(LatLng(latitude, longitude))
            naverMap.moveCamera(cameraUpdate)
        }
        with(naverMap.locationOverlay) {
            isVisible = true
            icon = OverlayImage.fromResource(R.drawable.ic_my_location_24)
        }

        naverCluster = TedNaverClustering.with<MapCapsuleMarker>(requireContext(), naverMap)
            .markerClickListener { capsuleMarker ->
                viewModel.homeEvent(
                    HomeViewModel.HomeEvent.ShowCapsulePreviewDialog(
                        capsuleMarker.capsuleMarker.id.toString(),
                        capsuleMarker.capsuleMarker.capsuleType.toString()
                    )
                )

            }.customMarker { capsuleMarker ->
                Marker().apply {
                    icon = when (capsuleMarker.capsuleMarker.capsuleType) {
                        CapsuleType.SECRET -> OverlayImage.fromResource(R.drawable.ic_marker_pin_secret)
                        CapsuleType.GROUP -> OverlayImage.fromResource(R.drawable.ic_marker_pin_group)
                        CapsuleType.PUBLIC -> OverlayImage.fromResource(R.drawable.ic_marker_pin_public)
                    }
                }
            }.customCluster { cluster ->
                val clusterView = LayoutInflater.from(requireContext()).inflate(R.layout.item_marker_cluster, null)
                val clusterSize = clusterView.findViewById<TextView>(R.id.capsuleNumTextView)

                val displayText = when {
                    cluster.items.size < 10 -> cluster.items.size.toString()
                    cluster.items.size % 10 == 0 -> cluster.items.size.toString()
                    else -> "${(cluster.items.size / 10) * 10}+"
                }

                clusterSize.text = displayText
                clusterView
            }
            .make()
    }


    private fun getItems(capsuleList: List<CapsuleMarker>): List<MapCapsuleMarker> {
        val markers = ArrayList<MapCapsuleMarker>()
        capsuleList.map { capsuleMarker ->
            val temp = MapCapsuleMarker(capsuleMarker)
            markers.add(temp)
        }
        return markers
    }

    private fun fetchCapsulesNearUser(){
        locationUtil.getCurrentLocation { latitude, longitude ->
            viewModel.getNearbyCapsules(
                latitude,
                longitude,
                calculateRadiusForZoomLevel(),
                viewModel.filterCapsuleSelect.value.toString()
            )
        }
    }

    private fun fetchCapsulesInCameraFocus(){
        if (::naverMap.isInitialized){
            val cameraTarget = naverMap.cameraPosition.target
            viewModel.getNearbyCapsules(
                cameraTarget.latitude,
                cameraTarget.longitude,
                calculateRadiusForZoomLevel(),
                viewModel.filterCapsuleSelect.value.toString()
            )
        }
    }

    private fun calculateRadiusForZoomLevel(): Double {
        val earthRadius = 6371.01

        val latDistance = Math.toRadians(MAXLAT - MINLAT)
        val lngDistance = Math.toRadians(MAXLNG - MINLNG)

        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(MINLAT)) * Math.cos(Math.toRadians(MAXLAT)) *
                Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2)

        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        val maxRadius = earthRadius * c

        return maxRadius
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

    override fun onResume() {
        super.onResume()
        fetchCapsulesNearUser()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden){
            fetchCapsulesInCameraFocus()
        }
    }

    companion object {
        const val TAG = "homeFragment"
        fun newIntent() = HomeFragment()

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        const val MAXZOOM = 21.0
        const val MINZOOM = 5.5
        const val FIXZOOM = 14.0
        const val MINLAT = 32.0
        const val MAXLAT = 43.0
        const val MINLNG = 124.0
        const val MAXLNG = 132.0
    }

}