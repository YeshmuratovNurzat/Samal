package kz.fime.samal.ui.profile.address

import android.os.Bundle
import android.util.Log
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
import kz.fime.samal.databinding.AddressDetailDialogBinding
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.utils.MessageUtils
import kz.fime.samal.utils.binding.BindingBottomSheetFragment
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull

class AddressDetailDialog : BindingBottomSheetFragment<AddressDetailDialogBinding>(AddressDetailDialogBinding::inflate) {

    private val viewModel: AddressesViewModel by activityViewModels()
    private lateinit var mGoogleMap: GoogleMap
    var idCity = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {

            val latitude = arguments?.getString("latitude")
            val longitude = arguments?.getString("longitude")

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

                val currentLatLng = LatLng(latitude?.toDouble() ?: 0.0,longitude?.toDouble() ?: 0.0)
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,10f))

                googleMap.setOnMapClickListener {
                    findNavController().navigate(R.id.action_global_googleMapDialog,
                        bundleOf(Pair("latitude",latitude),Pair("longitude",longitude)))
                }
            }

            val addressSlug = arguments?.getString("address_slug")!!
            val addressId = arguments?.getString("address_id")!!
            val name = arguments?.getString("name")
            val street = arguments?.getString("street")
            val houseNumber = arguments?.getString("house_number")
            val apartment = arguments?.getString("apartment")
            val default = arguments?.getBoolean("default")!!
            val city = arguments?.getString("city")
            val cityId = arguments?.getInt("city_id")

            etName.setText(name.toString())
            etStreet.setText(street.toString())
            etHouse.setText(houseNumber.toString())
            etApartment.setText(apartment.toString())
            etCity.setText(city)

            addressDelete.setOnClickListener {
                findNavController().navigate(R.id.action_global_deleteAddressDialog,
                    bundleOf(
                        Pair("address_slug", addressSlug),
                        (Pair("name", name)),
                        (Pair("street", street)),
                        (Pair("house_number", houseNumber)),
                        (Pair("apartment", apartment))))
            }

            etCity.setOnDismissListener {

                val cityName = etCity.text.toString()

                if (viewModel.cities.value is State.Success) {

                    Log.d("MyLog","cityId = $cityId")
                    idCity =
                        ((viewModel.cities.value as State.Success<List<Item>>).result?.first() {
                            it.getOrNull("name", "")!! == cityName
                        }).getOrNull("id", 0)!!
                }
            }

            btnSave.setOnClickListener {

                val tvName = etName.text.toString()
                val tvStreet = etStreet.text.toString()
                val tvHouseNumber = etHouse.text.toString()
                val tvApartment = etApartment.text.toString()

                if(idCity == 0){
                    idCity = cityId!!
                }

                if (tvName.isEmpty() || tvStreet.isEmpty() || tvHouseNumber.isEmpty() || tvApartment.isEmpty()) {
                    MessageUtils.postMessage("Заполните все поля!")
                    return@setOnClickListener
                }

                viewModel.updateAddress(addressSlug, idCity, tvName, tvStreet, tvHouseNumber, tvApartment, default,"49.806406", "73.085485")
                dismiss()
            }
        }
    }
}