package kz.fime.samal.ui.profile.address

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kz.fime.samal.R
import kz.fime.samal.databinding.FragmentGoogleMapDialogBinding
import kz.fime.samal.utils.binding.BindingBottomSheetFragment
import java.io.IOException


class GoogleMapDialog : BindingBottomSheetFragment<FragmentGoogleMapDialogBinding>(FragmentGoogleMapDialogBinding::inflate) {

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false
    private lateinit var markerCenter: Marker


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {

            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

            val latitude = arguments?.getString("latitude")
            val longitude = arguments?.getString("longitude")

            val mapFragment =
                childFragmentManager.findFragmentById(R.id.location_map) as SupportMapFragment

            mapFragment.getMapAsync { googleMap ->
                mGoogleMap = googleMap
                mGoogleMap.uiSettings.isZoomControlsEnabled = true

                val currentLatLng = LatLng(latitude?.toDouble() ?: 0.0, longitude?.toDouble() ?: 0.0)
                val markerOptions = MarkerOptions()
                markerOptions.position(currentLatLng)
                markerOptions.icon(getBitmapDescriptorFromVector(requireContext(), R.drawable.ic_location))
                markerCenter = mGoogleMap.addMarker(markerOptions)!!
                mGoogleMap.setOnCameraMoveListener {
                    markerCenter.position = mGoogleMap.cameraPosition.target //to center in map
                }
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))

            }

            btn.setOnClickListener {
                dismiss()
            }
        }
    }

    fun getBitmapDescriptorFromVector(context: Context, @DrawableRes vectorDrawableResourceId: Int): BitmapDescriptor? {

        val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId)
        val bitmap = Bitmap.createBitmap(
            vectorDrawable!!.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}

