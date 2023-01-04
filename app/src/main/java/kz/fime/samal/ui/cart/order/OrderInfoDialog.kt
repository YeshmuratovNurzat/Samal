package kz.fime.samal.ui.cart.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import kz.fime.samal.databinding.DialogCartClearBinding
import kz.fime.samal.databinding.DialogOrderInfoBinding
import kz.fime.samal.databinding.DialogQrInfoBinding
import kz.fime.samal.utils.binding.BindingBottomSheetFragment

class OrderInfoDialog: BindingBottomSheetFragment<DialogOrderInfoBinding>(DialogOrderInfoBinding::inflate) {
    companion object {
        const val KEY_TITLE = "key_title"
        const val KEY_DESC = "key_desc"
        const val KEY_IMAGE = "key_image"
        const val KEY_BUTTON_TEXT = "key_button_text"
    }
    private val title: String? by lazy { arguments?.getString(KEY_TITLE) }
    private val desc: String? by lazy { arguments?.getString(KEY_DESC) }
    private val image: Int? by lazy { arguments?.getInt(KEY_IMAGE) }
    private val btnText: String? by lazy { arguments?.getString(KEY_BUTTON_TEXT) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        title?.let {
            binding.title.text = it
        }
        desc?.let {
            binding.desc.text = it
        }
        btnText?.let {
            binding.btnOk.text = it
        }
        image?.let {
            binding.icon.setImageResource(it)
        }

        binding.run {
            btnOk.setOnClickListener {
                dismiss()
            }
        }
    }

}