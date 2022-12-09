package kz.fime.samal.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kz.fime.samal.R
import kz.fime.samal.data.SessionManager
import kz.fime.samal.data.models.custom.Status
import kz.fime.samal.databinding.FragmentLoginBinding
import kz.fime.samal.ui.MainActivity
import kz.fime.samal.ui.base.handleException
import kz.fime.samal.ui.base.observeEvent
import kz.fime.samal.utils.EditTextUtils
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.extensions.isLoading
import kz.fime.samal.utils.extensions.progressButton

class LoginFragment : BindingFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

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
            EditTextUtils.setPhoneMask(etPhone)
            progressButton(btnLogin)
            btnLogin.setOnClickListener {
                if (etPhone.text.toString()
                        .isNotEmpty() && etPassword.text.toString().isNotEmpty()
                ) {
                    viewModel.loginUser(
                        EditTextUtils.getPhoneUnmasked(etPhone.text.toString()),
                        etPassword.text.toString()
                    )
                }
            }

            btnForgotPassword.setOnClickListener {
                viewModel.isForgotPassword(true)
                findNavController().navigate(R.id.action_auth_to_otp)
            }
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.loginUser.observeEvent(viewLifecycleOwner, {
            it.result?.let { data ->
                SessionManager.token = data["token"].toString()
                startActivity(Intent(requireActivity(), MainActivity::class.java))
                requireActivity().finish()
            }
        }, {
        }, {
            binding.btnLogin.isLoading(it)
        })
    }

}