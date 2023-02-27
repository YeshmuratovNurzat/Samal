package kz.fime.samal.ui.profile.address

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kz.fime.samal.R
import kz.fime.samal.databinding.FragmentGoogleMapDialogBinding
import kz.fime.samal.utils.binding.BindingBottomSheetFragment
import java.util.*

class GoogleMapDialog : BindingBottomSheetFragment<FragmentGoogleMapDialogBinding>(FragmentGoogleMapDialogBinding::inflate) {

    private val viewModel: AddressesViewModel by activityViewModels()
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var markerCenter: Marker
    private var mPositionLatitude : Double = 0.0
    private var mPositionLongitude : Double = 0.0
    private var addressName : String = ""
    private var addressNumber : String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {

            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

            val latitude = arguments?.getString("latitude")
            val longitude = arguments?.getString("longitude")

            val mapFragment = childFragmentManager.findFragmentById(R.id.location_map) as SupportMapFragment

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

                mGoogleMap.setOnCameraIdleListener {
                    val mPosition = mGoogleMap.cameraPosition.target
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    val addresses = geocoder.getFromLocation(mPosition.latitude, mPosition.longitude, 1)
                    val address = addresses[0].getAddressLine(0)
                    val title = "$address"
                    if(addresses[0].thoroughfare != null && addresses[0].subThoroughfare != null){
                        addressName = addresses[0].thoroughfare
                        addressNumber = addresses[0].subThoroughfare
                    }
                    mPositionLatitude = mPosition.latitude
                    mPositionLongitude = mPosition.longitude
                    locationInfo.text = title
                }
            }

            btn.setOnClickListener {
                viewModel.addressMap?.value = mapOf("addressName" to addressName, "addressNumber" to addressNumber)
                dismiss()
            }
        }
    }

    private fun getBitmapDescriptorFromVector(context: Context, @DrawableRes vectorDrawableResourceId: Int): BitmapDescriptor? {

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

