package kz.fime.samal.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.R
import kz.fime.samal.data.models.custom.Status
import kz.fime.samal.databinding.FragmentOtpBinding
import kz.fime.samal.ui.base.handleException
import kz.fime.samal.ui.base.observeEvent
import kz.fime.samal.utils.EditTextUtils
import kz.fime.samal.utils.binding.BindingFragment

@AndroidEntryPoint
class OTPFragment : BindingFragment<FragmentOtpBinding>(FragmentOtpBinding::inflate) {

    private val viewModel: AuthViewModel by activityViewModels()
    private lateinit var phone: String
    private lateinit var name: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            viewModel.getPhoneNumber().observe(viewLifecycleOwner, {
                viewModel.getOTP(it)
                phone = it
            })
            viewModel.getUserName().observe(viewLifecycleOwner, {
                name = it
                binding.registrationLabel.text = String.format(
                    getString(R.string.registration_text),
                    name,
                    EditTextUtils.getPhoneSecretMasked(phone.substring(1))
                )
            })

        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.getOTP.observeEvent(viewLifecycleOwner, {
            checkOTP()
        }, {}, {})
        viewModel.checkOTP.observeEvent(viewLifecycleOwner, {

            binding.loading.visibility = View.GONE
            it.result.let {
                viewModel.saveOTP(binding.otp.text.toString())
                findNavController().navigate(R.id.action_auth_to_password)
            }

        }, { binding.loading.visibility = View.GONE }, {
            binding.loading.visibility = View.VISIBLE
        })
    }

    private fun checkOTP() {
        binding.otp.doAfterTextChanged { p0 ->
            if (p0?.length == 4)
                viewModel.checkOTP(phone, p0.toString())
        }
    }

}