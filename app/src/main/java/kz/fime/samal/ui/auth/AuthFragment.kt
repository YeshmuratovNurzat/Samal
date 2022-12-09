package kz.fime.samal.ui.auth

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.R
import kz.fime.samal.data.models.custom.Status
import kz.fime.samal.data.repositories.EmptyException
import kz.fime.samal.databinding.FragmentAuthBinding
import kz.fime.samal.ui.base.handleException
import kz.fime.samal.ui.base.observeEvent
import kz.fime.samal.utils.EditTextUtils
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.extensions.progressButton
import kz.fime.samal.utils.extensions.isLoading

@AndroidEntryPoint
class AuthFragment : BindingFragment<FragmentAuthBinding>(FragmentAuthBinding::inflate) {

    private val viewModel: AuthViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            toolbar.setOnMenuItemClickListener {
                if (it.itemId == R.id.close) {
                    activity?.onBackPressed()
                }
                false
            }
            progressButton(btnContinue)
            EditTextUtils.setPhoneMask(etPhone)
            btnContinue.setOnClickListener {
                viewModel.checkAccount(EditTextUtils.getPhoneUnmasked(etPhone.text.toString()))
            }

            etPhone.doAfterTextChanged {
                btnContinue.isEnabled =
                    viewModel.isEnteredValidPhone(EditTextUtils.getPhoneUnmasked(etPhone.text.toString()))
            }
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.isAccountChecked.observeEvent(viewLifecycleOwner, {
            if (it.code == 204) {
                findNavController().navigate(R.id.action_auth_to_register)
            } else {
                findNavController().navigate(R.id.action_auth_to_login)
            }
        }, {

        }, {
            binding.btnContinue.isLoading(it)
        })
    }
}