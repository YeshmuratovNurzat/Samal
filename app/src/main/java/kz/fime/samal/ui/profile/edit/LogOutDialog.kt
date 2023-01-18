package kz.fime.samal.ui.profile.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import kz.fime.samal.databinding.DialogLogOutBinding
import kz.fime.samal.ui.profile.ProfileViewModel
import kz.fime.samal.ui.profile.ProfileViewModel2
import kz.fime.samal.utils.binding.BindingBottomSheetFragment


class LogOutDialog : BindingBottomSheetFragment<DialogLogOutBinding>(DialogLogOutBinding::inflate) {

    private val viewModel: ProfileViewModel by activityViewModels()
    private val viewModel2: ProfileViewModel2 by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            btnOk.setOnClickListener {
                viewModel2.logoutUser()
                dismiss()
            }
        }
    }
}