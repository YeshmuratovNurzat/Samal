package kz.fime.samal.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.R
import kz.fime.samal.databinding.FragmentRegistrationBinding
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.extensions.progressButton

@AndroidEntryPoint
class RegistrationFragment :
    BindingFragment<FragmentRegistrationBinding>(FragmentRegistrationBinding::inflate) {

    private val viewModel: AuthViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            progressButton(continueBtn)
            binding.continueBtn.setOnClickListener {
                if (binding.name.text.toString().isNotEmpty()) {
                    viewModel.setUserName(binding.name.text.toString())
                    viewModel.isForgotPassword(false)
                    findNavController().navigate(R.id.action_auth_to_otp)
                }
            }
        }
    }


}