package com.seumulseumul.cld.ui.gym

import android.content.ActivityNotFoundException
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.seumulseumul.cld.R
import com.seumulseumul.cld.databinding.ActivityGymDetailBinding
import com.seumulseumul.cld.sharedpref.PrefData
import com.seumulseumul.cld.sharedpref.PrefKey
import com.seumulseumul.cld.ui.video.VideoDetailActivity
import com.seumulseumul.domain.model.Location
import com.seumulseumul.domain.model.Record
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


@AndroidEntryPoint
class GymDetailActivity: AppCompatActivity(), MapView.POIItemEventListener {

    companion object {
        private val TAG: String = GymDetailActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityGymDetailBinding

    private val viewModel: GymDetailViewModel by viewModels()

    private val auth by lazy {
        PrefData.getString(PrefKey.authToken, "")
    }
    private val gymId by lazy {
        intent.getStringExtra("id")!!
    }
    private val mapView by lazy {
        MapView(this@GymDetailActivity)
    }
    private var recordInfo: Record? = null
    private lateinit var placeId: String

    private var isBookmark = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGymDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getClimbingGymDetail(
            auth,
            gymId
        )
        viewModel.getTitleClimeRecord(
            auth,
            gymId
        )

        initViewModelStream()

        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.ivBookmark.setOnClickListener {
            isBookmark = if (isBookmark) {
                viewModel.deleteClimbingGymBookmark(auth, gymId)
                binding.ivBookmark.setImageResource(R.drawable.baseline_bookmark_border_20)
                false
            } else {
                viewModel.postClimbingGymBookmark(auth, gymId)
                binding.ivBookmark.setImageResource(R.drawable.baseline_bookmark_on_20)
                true
            }
        }

        binding.ivGymVideo.setOnClickListener {
            if (recordInfo != null) {
                val intent = Intent(this, VideoDetailActivity::class.java)
                intent.putExtra("record", recordInfo)
                startActivity(intent)
            }
        }
    }

    private fun initViewModelStream() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.titleRecordSharedFlow.collect {
                    if (it.records.isEmpty()) {
                        binding.layoutGymVideo.visibility = View.GONE
                    } else {
                        /*binding.vvGymVideo.setVideoURI(Uri.parse(it.records[0].video))

                        binding.vvGymVideo.setOnInfoListener { mp, what, extra ->
                            if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
                                Log.d("TESTLOG", "MEDIA_INFO_VIDEO_RENDERING_START")
                                binding.pbLoadingVideo.visibility = View.GONE
                            }
                            if (MediaPlayer.MEDIA_INFO_BUFFERING_START == what) {
                                Log.d("TESTLOG", "MEDIA_INFO_BUFFERING_START")
                                //binding.pbLoadingVideo.visibility = View.VISIBLE
                            }
                            if (MediaPlayer.MEDIA_INFO_BUFFERING_END == what) {
                                Log.d("TESTLOG", "MEDIA_INFO_BUFFERING_END")
                                binding.pbLoadingVideo.visibility = View.GONE
                            }
                            false
                        }
                        binding.vvGymVideo.seekTo(1)*/
                        Glide.with(this@GymDetailActivity)
                            .load(it.records[0].video)
                            .into(binding.ivGymVideo)
                        recordInfo = it.records[0]
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.gymDetailSharedFlow.collect {
                    binding.tvGymTitle.text = it.place.name
                    binding.tvGymAddress.text = it.place.roadAddressName
                    binding.tvGymTel.text = it.place.roadAddressName
                    if (it.place.phone.isNullOrEmpty()) binding.tvGymTel.visibility = View.GONE
                    else binding.tvGymTel.text = it.place.phone

                    placeId = if (it.place.placeId.isNullOrEmpty()) ""
                    else it.place.placeId!!

                    it.location?.let { loc -> initMapView(loc, it.place.name) }
                }
            }
        }
    }

    private fun initMapView(location: Location, name: String) {
        val gymPoint = MapPoint.mapPointWithGeoCoord(location.y, location.x)
        mapView.setMapCenterPointAndZoomLevel(gymPoint, 3, true)

        mapView.setPOIItemEventListener(this)

        val marker = MapPOIItem()
        marker.itemName = name
        marker.mapPoint = gymPoint
        marker.markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.
        marker.selectedMarkerType =
            MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView.addPOIItem(marker)

        binding.layoutMapView.addView(mapView)
    }

    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
        Log.d(TAG, "TESTLOG onPOIItemSelected")
    }

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {
    }

    override fun onCalloutBalloonOfPOIItemTouched(
        p0: MapView?,
        p1: MapPOIItem?,
        p2: MapPOIItem.CalloutBalloonButtonType?
    ) {
        Log.d(TAG, "TESTLOG placeId: $placeId")
        if (placeId.isNotEmpty()) {
            val url = "kakaomap://place?id=$placeId"
            Log.d(TAG, "TESTLOG url: $url")
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=net.daum.android.map")
                ))
            }
        }
    }

    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
    }
}