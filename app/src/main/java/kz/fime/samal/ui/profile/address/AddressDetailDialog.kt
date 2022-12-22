package kz.fime.samal.ui.profile.address

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import kz.fime.samal.R
import kz.fime.samal.data.models.City
import kz.fime.samal.databinding.AddressDetailDialogBinding
import kz.fime.samal.utils.binding.BindingBottomSheetFragment

class AddressDetailDialog : BindingBottomSheetFragment<AddressDetailDialogBinding>(AddressDetailDialogBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            val addressSlug = arguments?.getString("address_slug")
            val name = arguments?.getString("name")
            val street = arguments?.getString("street")
            val houseNumber = arguments?.getString("house_number")
            val apartment = arguments?.getString("apartment")
            val city = arguments?.getString("city")

            etName.setText(name.toString())
            etStreet.setText(street.toString())
            etHouse.setText(houseNumber.toString())
            etApartment.setText(apartment.toString())
            etCity.setText(city)

            addressDelete.setOnClickListener {
                findNavController().navigate(R.id.action_global_addressDeleteDialog, bundleOf(Pair("address_slug", addressSlug)))
                dismiss()
            }
        }

    }
}