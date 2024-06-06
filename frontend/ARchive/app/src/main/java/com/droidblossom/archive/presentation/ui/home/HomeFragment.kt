package com.droidblossom.archive.presentation.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.FragmentHomeBinding
import com.droidblossom.archive.domain.model.capsule.CapsuleMarker
import com.droidblossom.archive.presentation.base.BaseFragment
import com.droidblossom.archive.presentation.ui.home.createcapsule.CreateCapsuleActivity
import com.droidblossom.archive.presentation.ui.home.dialog.CapsulePreviewDialogFragment
import com.droidblossom.archive.presentation.ui.home.notification.NotificationActivity
import com.droidblossom.archive.util.LocationUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.clustering.ClusterMarkerInfo
import com.naver.maps.map.clustering.Clusterer
import com.naver.maps.map.clustering.DefaultClusterMarkerUpdater
import com.naver.maps.map.clustering.DefaultLeafMarkerUpdater
import com.naver.maps.map.clustering.LeafMarkerInfo
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.pow

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModelImpl, FragmentHomeBinding>(R.layout.fragment_home),
    OnMapReadyCallback {

    override val viewModel: HomeViewModelImpl by viewModels<HomeViewModelImpl>()

    //private lateinit var naverMap: NaverMap
    private lateinit var locationUtil: LocationUtil
    private lateinit var locationSource: FusedLocationSource

    // https://navermaps.github.io/android-map-sdk/guide-ko/5-8.html
    private val clusterer: Clusterer<CapsuleClusteringKey> =
        Clusterer.Builder<CapsuleClusteringKey>()
            .clusterMarkerUpdater(object : DefaultClusterMarkerUpdater() {
                override fun updateClusterMarker(info: ClusterMarkerInfo, marker: Marker) {
                    super.updateClusterMarker(info, marker)
                    marker.icon = OverlayImage.fromResource(R.drawable.ic_cluster_marker_46)
                    marker.height = 120
                    marker.width = 120
                    marker.captionColor = Color.BLACK
                    marker.captionHaloColor =
                        ContextCompat.getColor(requireContext(), R.color.main_bg_1)
                    marker.captionTextSize = if (info.size >= 100) 15f else 18f
                    marker.onClickListener = Overlay.OnClickListener {
                        // 클러스터된 거 클릭이벤트 - 나중에 클러스터에 행당된 캡슐들 사이드에 보여주거나 하면 좋을듯?
                        true
                    }
                }
            }).leafMarkerUpdater(object : DefaultLeafMarkerUpdater() {
                override fun updateLeafMarker(info: LeafMarkerInfo, marker: Marker) {
                    super.updateLeafMarker(info, marker)
                    val key = info.key as CapsuleClusteringKey
                    marker.icon = when (key.capsuleType) {
                        CapsuleType.SECRET -> OverlayImage.fromResource(R.drawable.ic_marker_pin_secret)
                        CapsuleType.GROUP -> OverlayImage.fromResource(R.drawable.ic_marker_pin_group)
                        CapsuleType.PUBLIC -> OverlayImage.fromResource(R.drawable.ic_marker_pin_public)
                        CapsuleType.TREASURE -> OverlayImage.fromResource(R.drawable.ic_marker_pin_treasure)
                    }
                    marker.width = 120
                    marker.height = 132
                    marker.onClickListener = Overlay.OnClickListener {
                        viewModel.homeEvent(
                            HomeViewModel.HomeEvent.ShowCapsulePreviewDialog(
                                key.id.toString(),
                                key.capsuleType.toString()
                            )
                        )
                        true
                    }
                }
            })
            .minZoom(FIXZOOM.toInt() - 6)
            .maxZoom(FIXZOOM.toInt() + 2)
            .build()


    private val zoomToRadiusMap: Map<Double, Double> by lazy {
        val map = mutableMapOf<Double, Double>()

        for (zoomLevel in MINZOOM.toInt()..MAXZOOM.toInt()) {
            val normalizedZoom = 1 - ((zoomLevel - MINZOOM) / (MAXZOOM - MINZOOM))
            val radius = MINRADIUS + (MAXRADIUS - MINRADIUS) * normalizedZoom.pow(3)
            map[zoomLevel.toDouble()] = radius
        }
        map.toMap()
    }

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
                    fetchCapsulesInCameraFocus()
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
                            val existingDialog = parentFragmentManager.findFragmentByTag(CapsulePreviewDialogFragment.TAG) as DialogFragment?
                            if (existingDialog == null) {
                                val dialog = CapsulePreviewDialogFragment.newInstance(
                                    "-1",
                                    event.capsuleId,
                                    event.capsuleType,
                                    false
                                )
                                dialog.show(parentFragmentManager, CapsulePreviewDialogFragment.TAG)
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.followLocation.collect { followLocation ->
                    clusterer.map?.let { map ->
                        if (followLocation) {
                            map.maxZoom = FIXZOOM
                            map.minZoom = FIXZOOM
                            map.locationOverlay.circleRadius = 100
                            map.locationOverlay.circleOutlineWidth = 1
                            map.locationOverlay.circleColor =
                                ContextCompat.getColor(requireContext(), R.color.main_1_alpha20)
                            map.locationOverlay.circleOutlineColor =
                                ContextCompat.getColor(requireContext(), R.color.main_1)
                            map.locationTrackingMode = LocationTrackingMode.Follow
                        } else {
                            map.locationTrackingMode = LocationTrackingMode.NoFollow
                            map.minZoom = MINZOOM
                            map.maxZoom = MAXZOOM
                            map.locationOverlay.circleRadius = 0
                        }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isFriendsCapsuleDisplay.collect { state ->
                    clusterer.map?.let { map ->
                        if (state && (viewModel.filterCapsuleSelect.value == HomeViewModel.CapsuleFilter.ALL
                                    || viewModel.filterCapsuleSelect.value == HomeViewModel.CapsuleFilter.PUBLIC)
                        ) {
                            viewModel.getNearbyFriendsCapsules(
                                map.cameraPosition.target.latitude,
                                map.cameraPosition.target.longitude,
                                getRadiusForCurrentZoom(),
                            )
                        } else {
                            viewModel.getNearbyMyCapsules(
                                map.cameraPosition.target.latitude,
                                map.cameraPosition.target.longitude,
                                getRadiusForCurrentZoom(),
                                viewModel.filterCapsuleSelect.value.name
                            )
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.capsuleList.collect {
                    clusterer.map?.let { _ ->
                        // 마커 지우는 로직
                        clusterer.clear()
                        // 마커 찍는 로직
                        addMarker(it)
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
        this.clusterer.map = naverMap
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
    }

    private fun addMarker(capsuleList: List<CapsuleMarker>) {

        val keyTagMap: Map<CapsuleClusteringKey, *> = capsuleList.associate {
            CapsuleClusteringKey(
                id = it.id,
                capsuleType = it.capsuleType,
                position = LatLng(it.latitude, it.longitude)
            ) to null
        }

        clusterer.addAll(keyTagMap)
    }

    private fun fetchCapsulesNearUser() {
        locationUtil.getCurrentLocation { latitude, longitude ->
            val radius = if (clusterer.map != null) getRadiusForCurrentZoom() else DEFATULTRADIUS
            if (viewModel.isFriendsCapsuleDisplay.value &&
                (viewModel.filterCapsuleSelect.value == HomeViewModel.CapsuleFilter.ALL ||
                        viewModel.filterCapsuleSelect.value == HomeViewModel.CapsuleFilter.PUBLIC)
            ) {
                viewModel.getNearbyMyAndFriendsCapsules(
                    latitude,
                    longitude,
                    radius,
                    viewModel.filterCapsuleSelect.value.toString()
                )
            } else {
                viewModel.getNearbyMyCapsules(
                    latitude,
                    longitude,
                    radius,
                    viewModel.filterCapsuleSelect.value.toString()
                )
            }
        }
    }

    private fun fetchCapsulesInCameraFocus() {
        clusterer.map?.let { map ->
            val cameraTarget = map.cameraPosition.target
            if (viewModel.isFriendsCapsuleDisplay.value &&
                (viewModel.filterCapsuleSelect.value == HomeViewModel.CapsuleFilter.ALL ||
                        viewModel.filterCapsuleSelect.value == HomeViewModel.CapsuleFilter.PUBLIC)
            ) {
                viewModel.getNearbyMyAndFriendsCapsules(
                    cameraTarget.latitude,
                    cameraTarget.longitude,
                    getRadiusForCurrentZoom(),
                    viewModel.filterCapsuleSelect.value.toString()
                )
            } else {
                viewModel.getNearbyMyCapsules(
                    cameraTarget.latitude,
                    cameraTarget.longitude,
                    getRadiusForCurrentZoom(),
                    viewModel.filterCapsuleSelect.value.toString()
                )
            }
        }
    }


    private fun getRadiusForCurrentZoom(): Double {
        val currentZoom = clusterer.map?.cameraPosition?.zoom ?: return FIXZOOM

        val closestZoomLevel =
            zoomToRadiusMap.keys.minByOrNull { kotlin.math.abs(it - currentZoom) }

        return zoomToRadiusMap[closestZoomLevel]
            ?: throw IllegalArgumentException("Invalid zoom level: $currentZoom")
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
                clusterer.map?.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    enum class CapsuleType {
        SECRET,
        GROUP,
        PUBLIC,
        TREASURE
    }

    override fun onResume() {
        super.onResume()
        fetchCapsulesNearUser()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
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
        const val DEFATULTRADIUS = 4.0
        const val MAXRADIUS = 1100.0
        const val MINRADIUS = 2.0
    }

}