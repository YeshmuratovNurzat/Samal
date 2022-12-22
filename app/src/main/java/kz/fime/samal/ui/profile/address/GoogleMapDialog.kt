package kz.fime.samal.ui.profile.address

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kz.fime.samal.R
import kz.fime.samal.databinding.FragmentGoogleMapDialogBinding
import kz.fime.samal.utils.binding.BindingBottomSheetFragment
import timber.log.Timber
import java.io.IOException
import java.util.*


class GoogleMapDialog : BindingBottomSheetFragment<FragmentGoogleMapDialogBinding>(FragmentGoogleMapDialogBinding::inflate) {

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {

            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

            val latitude = arguments?.getString("latitude")
            val longitude = arguments?.getString("longitude")

            val mapFragment = childFragmentManager.findFragmentById(R.id.location_map) as SupportMapFragment
            mapFragment.getMapAsync { googleMap ->
                mGoogleMap = googleMap
                mGoogleMap.uiSettings.isZoomControlsEnabled = true

                val currentLatLng = LatLng(latitude?.toDouble() ?: 0.0,
                    longitude?.toDouble() ?: 0.0)
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,14f))

            }
        }
    }
}