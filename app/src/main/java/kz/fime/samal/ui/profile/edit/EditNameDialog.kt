package kz.fime.samal.ui.profile.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import kz.fime.samal.databinding.DialogEditNameBinding
import kz.fime.samal.ui.profile.ProfileViewModel
import kz.fime.samal.utils.binding.BindingBottomSheetFragment

class EditNameDialog: BindingBottomSheetFragment<DialogEditNameBinding>(DialogEditNameBinding::inflate) {

    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            btnOk.setOnClickListener {

                dismiss()
            }
        }
    }

}