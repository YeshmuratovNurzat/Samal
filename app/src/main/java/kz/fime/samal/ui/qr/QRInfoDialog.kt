package kz.fime.samal.ui.qr

import android.os.Bundle
import android.view.View
import kz.fime.samal.databinding.DialogQrInfoBinding
import kz.fime.samal.utils.binding.BindingBottomSheetFragment

class QRInfoDialog: BindingBottomSheetFragment<DialogQrInfoBinding>(DialogQrInfoBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            btnOk.setOnClickListener { dismiss() }
        }
    }

}