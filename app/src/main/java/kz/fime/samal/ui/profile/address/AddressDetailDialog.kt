package kz.fime.samal.ui.profile.address

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kz.fime.samal.R
import kz.fime.samal.data.base.State
import kz.fime.samal.data.models.City
import kz.fime.samal.data.models.order_detail.ClientAddress
import kz.fime.samal.databinding.AddressDetailDialogBinding
import kz.fime.samal.ui.base.observeEvent
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.ui.cart.order.OrderInfoDialog
import kz.fime.samal.utils.MessageUtils
import kz.fime.samal.utils.binding.BindingBottomSheetFragment
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.extensions.InnerItem
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull

class AddressDetailDialog : BindingBottomSheetFragment<AddressDetailDialogBinding>(AddressDetailDialogBinding::inflate) {

    private val viewModel: AddressesViewModel by activityViewModels()
    private lateinit var mGoogleMap: GoogleMap
    private var idCity = 0
    private var latitudeCity: String? = null
    private var longitudeCity: String? = null
    private var name: String? = null
    private var street: String? = null
    private var houseNumber: String? = null
    private var apartment: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {

            viewModel.cities.observeState(viewLifecycleOwner, {
                val items = mutableListOf<String>()
                it.result?.forEach {
                    items.add(it.getOrNull("name","")!!)
                }
                val adapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, items)
                etCity.setAdapter(adapter)
            })

            val addressSlug = arguments?.getString("address_slug")
            viewModel.getClientAddressDetails(addressSlug.toString())

            viewModel.clientAddressDetails.observeState(viewLifecycleOwner, {
                it.result?.let { it1 -> clientAddressDetail(it1) }
            })

            addressDelete.setOnClickListener {
                findNavController().navigate(R.id.action_global_deleteAddressDialog,
                    bundleOf(Pair("address_slug", addressSlug),
                        (Pair("name", name)),
                        (Pair("street", street)),
                        (Pair("house_number", houseNumber)),
                        (Pair("apartment", apartment))))
            }

            viewModel.deleteAddress.observeEvent(viewLifecycleOwner) {
                MessageUtils.postMessage("Адрес удалено")
                dismiss()
            }

            viewModel.addressMap?.observe(viewLifecycleOwner) {
                val addressNumber = it?.get("addressNumber")
                val addressName = it?.get("addressName")

                if(addressName != null) {
                    etStreet.setText(addressName.toString())
                    etHouse.setText(addressNumber.toString())
                }
            }


            btnSave.setOnClickListener {

                val tvName = etName.text.toString()
                val tvStreet = etStreet.text.toString()
                val tvHouseNumber = etHouse.text.toString()
                val tvApartment = etApartment.text.toString()

                if (tvName.isEmpty() || tvStreet.isEmpty() || tvHouseNumber.isEmpty()) {
                    val bundle = bundleOf(
                        OrderInfoDialog.KEY_TITLE to "Ошибка",
                        OrderInfoDialog.KEY_DESC to "Заполните все поля!",
                        OrderInfoDialog.KEY_BUTTON_TEXT to "Понятно, закрыть",
                        OrderInfoDialog.KEY_IMAGE to R.drawable.ic_cat_order_error)
                    findNavController().navigate(R.id.action_global_order_info, bundle)
                    return@setOnClickListener
                }

                viewModel.updateAddress(addressSlug!!, idCity, tvName, tvStreet, tvHouseNumber, tvApartment, false, latitudeCity.toString(), longitudeCity.toString())
                dismiss()
            }
        }
    }

    private fun clientAddressDetail(clientAddress: ClientAddress){
        binding.run {


            if(clientAddress.apartment == "null"){
                etApartment.setText("")
            }else{
                etApartment.setText(clientAddress.apartment)
            }

            etHouse.setText(clientAddress.house_number)
            etStreet.setText(clientAddress.street)
            etName.setText(clientAddress.name)
            etCity.setText(clientAddress.city.name)

            idCity = clientAddress.city.city_id
            latitudeCity = clientAddress.point.latitude
            longitudeCity = clientAddress.point.longitude

            name = clientAddress.name
            street = clientAddress.street
            apartment = clientAddress.apartment
            houseNumber = clientAddress.house_number


            val mapFragment = childFragmentManager.findFragmentById(R.id.location) as SupportMapFragment

            mapFragment.getMapAsync { googleMap ->
                mGoogleMap = googleMap

                val currentLatLng = LatLng(latitudeCity?.toDouble() ?: 0.0,longitudeCity?.toDouble() ?: 0.0)
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,10f))
                mGoogleMap.uiSettings.isScrollGesturesEnabled = false
                mGoogleMap.uiSettings.isZoomGesturesEnabled = false
                mGoogleMap.uiSettings.isMapToolbarEnabled = false

                googleMap.setOnMapClickListener {
                    findNavController().navigate(R.id.action_global_googleMapDialog,
                        bundleOf(Pair("latitude", it.latitude.toString()),Pair("longitude", it.longitude.toString())))
                }
            }

        }
    }


    override fun onStop() {
        super.onStop()
        viewModel.addressMap?.value = null
    }


    //            etCity.setOnDismissListener {
//
//                val cityName = etCity.text.toString()
//
//                if (viewModel.cities.value is State.Success) {
//                    idCity = ((viewModel.cities.value as State.Success<List<Item>>).result?.first() {
//                        it.getOrNull("name", "")!! == cityName }).getOrNull("id", 0)!!
//
//                    val cityId =
//                        ((viewModel.cities.value as State.Success<List<Item>>).result?.first() {
//                            it.getOrNull("name", "")!! == cityName
//                        }).getOrNull("id", 0)!!
//
//                    val latitude =
//                        ((viewModel.cities.value as State.Success<List<Item>>).result?.first {
//                            it.getOrNull("id", 0)!! == cityId
//                        })?.getOrNull<InnerItem>("point").let {
//                            it.getOrNull("latitude","")
//                        }
//                    latitudeCity = latitude
//
//                    val longitude =
//                        ((viewModel.cities.value as State.Success<List<Item>>).result?.first {
//                            it.getOrNull("id", 0)!! == cityId
//                        })?.getOrNull<InnerItem>("point").let {
//                            it.getOrNull("longitude","")
//                        }
//                    longitudeCity = longitude
//
//                    val currentLatLng = LatLng(latitudeCity?.toDouble() ?: 0.0,longitudeCity?.toDouble() ?: 0.0)
//                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,10f))
//                }
//            }

}