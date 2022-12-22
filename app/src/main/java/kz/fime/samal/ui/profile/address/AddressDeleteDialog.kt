package kz.fime.samal.ui.profile.address

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import kz.fime.samal.databinding.AddressDeleteDialogBinding
import kz.fime.samal.utils.binding.BindingBottomSheetFragment

class AddressDeleteDialog: BindingBottomSheetFragment<AddressDeleteDialogBinding>(AddressDeleteDialogBinding::inflate){

    private val viewModel: AddressesViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {

            btnDelete.setOnClickListener {
                val addressSlug = arguments?.getString("address_slug")
                viewModel.deleteAddress(addressSlug!!)
                dismiss()
            }
        }
    }
}