package com.febiarifin.storyappsubmissiondicoding.ui.maps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.febiarifin.storyappsubmissiondicoding.R
import com.febiarifin.storyappsubmissiondicoding.databinding.ActivityMapsBinding
import com.febiarifin.storyappsubmissiondicoding.ui.main.MainActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object{
        const val TAG = "MapsActivity"
    }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModel: MapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MapsViewModel::class.java)

        viewModel.setStories(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getMyLocation()
        addStoryMarker()
        setMapStyle()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
        return true
    }

    private fun addStoryMarker() {

        viewModel.getStories().observe(this, {
            if (it != null) {

                lifecycleScope.launch {
                    it.forEach { story ->

                        val firstStory =  it.first()
                        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                            LatLng(firstStory.lat!!, firstStory.lon!!), 10f
                        )
                        mMap.animateCamera(cameraUpdate)

                        val latLng = LatLng(story.lat!!, story.lon!!)

                        try {
                            val bitmap = withContext(Dispatchers.IO) {
                                Glide.with(this@MapsActivity)
                                    .asBitmap()
                                    .load(story.photoUrl)
                                    .override(100, 100)
                                    .circleCrop()
                                    .submit()
                                    .get()
                            }

                            mMap.addMarker(
                                MarkerOptions()
                                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                                    .position(latLng)
                                    .title(story.name)
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        })
    }

    private fun setMapStyle(){
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }
}