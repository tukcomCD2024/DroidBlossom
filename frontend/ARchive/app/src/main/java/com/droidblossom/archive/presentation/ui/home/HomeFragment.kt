package com.droidblossom.archive.presentation.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.Toast
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
import com.droidblossom.archive.util.LocationUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModelImpl, FragmentHomeBinding>(R.layout.fragment_home),
    OnMapReadyCallback{

    override val viewModel: HomeViewModelImpl by viewModels<HomeViewModelImpl>()

    private lateinit var naverMap : NaverMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initMap()
    }

    private fun initView() {
        with(binding){
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
    }

    private fun initMap(){
        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        LocationUtil(requireContext()).getCurrentLocation { latitude, longitude ->
            val cameraUpdate = CameraUpdate.scrollTo(LatLng(latitude, longitude))
            naverMap.moveCamera(cameraUpdate)
        }
        addMarker(CapsuleType.SECRET)
        addMarker(CapsuleType.GROUP)
        addMarker(CapsuleType.PUBLIC)
    }

    private fun addMarker(capsuleType : CapsuleType){
        val randomLatitude = Random.nextDouble(33.0, 38.0)
        val randomLongitude = Random.nextDouble(126.0, 129.0)


        val marker = Marker().apply {
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
                Toast.makeText(requireContext(), markerData, Toast.LENGTH_SHORT).show()
                true
            }
        }
    }

    enum class CapsuleType{
        SECRET,
        GROUP,
        PUBLIC
    }

}