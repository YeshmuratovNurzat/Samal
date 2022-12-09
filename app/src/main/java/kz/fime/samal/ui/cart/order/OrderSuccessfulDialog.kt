package kz.fime.samal.ui.cart.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import kz.fime.samal.databinding.DialogCartClearBinding
import kz.fime.samal.databinding.DialogOrderSuccessfullBinding
import kz.fime.samal.databinding.DialogQrInfoBinding
import kz.fime.samal.utils.binding.BindingBottomSheetFragment

class OrderSuccessfulDialog: BindingBottomSheetFragment<DialogOrderSuccessfullBinding>(DialogOrderSuccessfullBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            btnOk.setOnClickListener {
                dismiss()
            }
        }
    }

}