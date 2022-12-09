package kz.fime.samal.ui.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import kz.fime.samal.databinding.DialogCartClearBinding
import kz.fime.samal.databinding.DialogQrInfoBinding
import kz.fime.samal.utils.binding.BindingBottomSheetFragment

class ClearCartDialog: BindingBottomSheetFragment<DialogCartClearBinding>(DialogCartClearBinding::inflate) {

    private val viewModel: CartViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            btnOk.setOnClickListener {
                viewModel.clearCart()
                dismiss()
            }
        }
    }

}