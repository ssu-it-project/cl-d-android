package com.seumulseumul.cld.ui.search

import android.Manifest
import android. content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.seumulseumul.cld.databinding.FragmentSearchGymBinding
import com.seumulseumul.cld.sharedpref.PrefData
import com.seumulseumul.cld.sharedpref.PrefKey
import com.seumulseumul.cld.ui.adapter.GymsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchNearGymsFragment: Fragment() {
    private var _binding: FragmentSearchGymBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by activityViewModels()

    private val adapter by lazy {
        GymsAdapter()
    }

    private val accessLocationFlag = 1000

    private var currentLat = 37.49609318770147
    private var currentLon = 126.95428024925512

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchGymBinding.inflate(inflater, container, false)

        permissionCheck()

        initView()
        initViewModelStream()
        viewModel.getClimbingGyms(
            PrefData.getString(PrefKey.authToken, ""),
            currentLon,
            currentLat,
            ""
        )

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViewModelStream() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.climbingGymsSharedFlow.collect {
                    if (it.climbingGyms.isNotEmpty()) adapter.submitList(it.climbingGyms.toMutableList())
                }
            }
        }
    }

    private fun initView() {
        binding.rvGyms.also {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = adapter
        }
    }

    // 위치 권한 확인
    private fun permissionCheck() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없는 상태
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                // 권한 거절 (다시 한 번 물어봄)
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("위치 권한을 허용해주세요.")
                builder.setPositiveButton("확인") { dialog, which ->
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), accessLocationFlag)
                }
                builder.setNegativeButton("취소") { dialog, which ->

                }
                builder.show()
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), accessLocationFlag)
            }
        } else {
            // 권한이 있는 상태
            val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val userLocation = if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            else locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (userLocation != null) {
                Log.d("TESTLOG", "userLocation.latitude: ${userLocation.latitude}")
                Log.d("TESTLOG", "userLocation.longitude: ${userLocation.longitude}")
                currentLat = userLocation.latitude
                currentLon = userLocation.longitude
            }
        }
    }

    // 권한 요청 후 행동
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == accessLocationFlag) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한 요청 후 승인됨 (추적 시작)
                //Toast.makeText(this, "위치 권한이 승인되었습니다", Toast.LENGTH_SHORT).show()
                //startTracking()
            } else {
                // 권한 요청 후 거절됨 (다시 요청 or 토스트)
                //Toast.makeText(this, "위치 권한이 거절되었습니다", Toast.LENGTH_SHORT).show()
                permissionCheck()
            }
        }
    }

    // GPS가 켜져있는지 확인
    private fun checkLocationService(): Boolean {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}