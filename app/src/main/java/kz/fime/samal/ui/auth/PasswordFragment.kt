package kz.fime.samal.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import kz.fime.samal.data.SessionManager
import kz.fime.samal.data.models.custom.Status
import kz.fime.samal.databinding.FragmentPasswordBinding
import kz.fime.samal.ui.MainActivity
import kz.fime.samal.ui.base.handleException
import kz.fime.samal.ui.base.observeEvent
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.extensions.isLoading
import kz.fime.samal.utils.extensions.progressButton

class PasswordFragment :
    BindingFragment<FragmentPasswordBinding>(FragmentPasswordBinding::inflate) {

    private val viewModel: AuthViewModel by activityViewModels()

    private lateinit var phoneNumber: String
    private lateinit var name: String
    private lateinit var otp: String
    private var isForgot: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getPhoneNumber().observe(viewLifecycleOwner, {
            phoneNumber = it
        })
        viewModel.getOTP().observe(viewLifecycleOwner, {
            otp = it
        })
        viewModel.getUserName().observe(viewLifecycleOwner, {
            name = it
        })
        viewModel.isForgot().observe(viewLifecycleOwner, {
            isForgot = it
        })
        binding.run {
            progressButton(continueBtn)
            binding.continueBtn.setOnClickListener {
                if (binding.passwordEt.text.toString()
                        .isNotEmpty() && binding.repeatPasswordEt.text.toString().isNotEmpty()
                ) {
                    if (binding.passwordEt.text.toString() == binding.repeatPasswordEt.text.toString()) {
                        if (isForgot) {
                            viewModel.resetPassword(
                                phoneNumber,
                                otp,
                                binding.passwordEt.text.toString()
                            )
                        } else {
                            viewModel.registerUser(
                                phoneNumber,
                                name,
                                otp,
                                binding.passwordEt.text.toString()
                            )
                        }
                    }
                }
            }
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.registerUser.observeEvent(viewLifecycleOwner, {
            it.result?.let { data ->
                SessionManager.token = data["token"].toString()
                startActivity(Intent(requireActivity(), MainActivity::class.java))
                requireActivity().finish()
            }
        }, {}, {
                binding.continueBtn.isLoading(it)
            })

        viewModel.resetPassword.observeEvent(viewLifecycleOwner,
            {
                it.result?.let { data ->
                    SessionManager.token = data["token"].toString()
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    requireActivity().finish()
                }
            }, {}, {
                binding.continueBtn.isLoading(it)
            })
    }

}