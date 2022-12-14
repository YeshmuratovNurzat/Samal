package kz.fime.samal.ui.profile.address


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kz.fime.samal.R
import kz.fime.samal.data.base.State
import kz.fime.samal.databinding.DialogAddAddressBinding
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.utils.MessageUtils
import kz.fime.samal.utils.binding.BindingBottomSheetFragment
import kz.fime.samal.utils.extensions.InnerItem
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull

class AddAddressDialog: BindingBottomSheetFragment<DialogAddAddressBinding>(DialogAddAddressBinding::inflate) {

    private val viewModel: AddressesViewModel by activityViewModels()
    private lateinit var mGoogleMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            viewModel.load()
            viewModel.cities.observeState(viewLifecycleOwner, {
                val items = mutableListOf<String>()
                it.result?.forEach {
                    items.add(it.getOrNull("name","")!!)
                }
                val adapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, items)
                etCity.setAdapter(adapter)
            })

            val mapFragment = childFragmentManager.findFragmentById(R.id.location) as SupportMapFragment

            mapFragment.getMapAsync { googleMap ->
                mGoogleMap = googleMap

                val latitude = 49.806406
                val longitude = 73.085485

                val currentLatLng = LatLng(latitude, longitude)
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,10f))
                mGoogleMap.uiSettings.isScrollGesturesEnabled = false
                mGoogleMap.uiSettings.isZoomGesturesEnabled = false
                mGoogleMap.uiSettings.isMapToolbarEnabled = false

                googleMap.setOnMapClickListener {
                    findNavController().navigate(R.id.action_global_googleMapDialog,
                        bundleOf(Pair("latitude", it.latitude.toString()),Pair("longitude", it.longitude.toString())))
                }
            }

            etCity.setOnDismissListener {
                val cityName = etCity.text.toString()
                if (viewModel.cities.value is State.Success) {

                    val cityId =
                        ((viewModel.cities.value as State.Success<List<Item>>).result?.first() {
                            it.getOrNull("name", "")!! == cityName
                        }).getOrNull("id", 0)!!

                    val latitude =
                        ((viewModel.cities.value as State.Success<List<Item>>).result?.first {
                            it.getOrNull("id", 0)!! == cityId
                        })?.getOrNull<InnerItem>("point").let {
                            it.getOrNull("latitude","")
                        }

                    val longitude =
                        ((viewModel.cities.value as State.Success<List<Item>>).result?.first {
                            it.getOrNull("id", 0)!! == cityId
                        })?.getOrNull<InnerItem>("point").let {
                            it.getOrNull("longitude","")
                        }

                        val currentLatLng = LatLng(latitude?.toDouble() ?: 0.0,longitude?.toDouble() ?: 0.0)
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,10f))
                }
            }

            btnSave.setOnClickListener {
                val name = etName.text.toString()
                val city = etCity.text.toString()
                val street = etStreet.text.toString()
                val houseNumber = etHouse.text.toString()
                val apartment = etApartment.text.toString()

                if (name.isEmpty() || city.isEmpty() || street.isEmpty() || houseNumber.isEmpty() || apartment.isEmpty()) {
                    MessageUtils.postMessage("?????????????????? ?????? ????????!")
                    return@setOnClickListener
                }
                if (viewModel.cities.value is State.Success) {
                    val cityId =
                        ((viewModel.cities.value as State.Success<List<Item>>).result?.first {
                            it.getOrNull("name", "")!! == city
                        }).getOrNull("id", 0)!!

                    viewModel.addAddress(cityId, name, street, houseNumber, apartment, false, "49.806406", "73.085485")
                    dismiss()
                }
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