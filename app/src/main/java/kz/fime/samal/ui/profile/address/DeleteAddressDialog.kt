package kz.fime.samal.ui.profile.address

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import kz.fime.samal.databinding.DialogAddressDeleteBinding
import kz.fime.samal.utils.binding.BindingBottomSheetFragment

class DeleteAddressDialog: BindingBottomSheetFragment<DialogAddressDeleteBinding>(DialogAddressDeleteBinding::inflate) {

    private val viewModel: AddressesViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            val name = arguments?.getString("name")
            val street = arguments?.getString("street")
            val houseNumber = arguments?.getString("house_number")
            val apartment = arguments?.getString("apartment")

            tvMainAddress.text = "Вы действительно хотите удалить адрес $name, $street, $houseNumber, $apartment?"

            btnDelete.setOnClickListener {
                val addressSlug = arguments?.getString("address_slug")
                viewModel.deleteAddress(addressSlug!!)
                dismiss()
            }
        }
    }
}