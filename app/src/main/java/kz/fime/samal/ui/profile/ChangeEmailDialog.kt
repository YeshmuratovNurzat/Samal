package kz.fime.samal.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kz.fime.samal.data.models.custom.Status
import kz.fime.samal.databinding.DialogEditEmailBinding
import kz.fime.samal.utils.FragmentResultKeys.Companion.CHANGE_EMAIL_BUNDLE_KEY
import kz.fime.samal.utils.FragmentResultKeys.Companion.CHANGE_EMAIL_REQUEST_KEY
import kz.fime.samal.utils.binding.BindingBottomSheetFragment
import kz.fime.samal.utils.extensions.isLoading
import kz.fime.samal.utils.extensions.progressButton


class ChangeEmailDialog : BindingBottomSheetFragment<DialogEditEmailBinding>(DialogEditEmailBinding::inflate) {

    private val viewModel: ProfileViewModel2 by activityViewModels()

    companion object {

        val SUCCESS = "success"
        val FAILURE = "failure"
        val RESULT = "result_of_action"
        val EXCEPTION_OF_ACTION = "exception_of_action"

        fun newInstance(): ChangeEmailDialog {
            val dialog = ChangeEmailDialog()
            return dialog
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            progressButton(btnOk)
            btnOk.setOnClickListener {
                if (binding.etEmail.text.toString().isNotEmpty())
                    viewModel.changeEmail(binding.etEmail.text.toString())
            }
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.resultChangeEmail.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.btnOk.isLoading(true)
                }
                Status.SUCCESS -> {
                    binding.btnOk.isLoading(false)
                    setResult(SUCCESS)
                }
                Status.ERROR -> {
                    binding.btnOk.isLoading(false)
                    setResult(FAILURE)
                }
            }
        })
    }

    private fun setResult(result: String) {
        setFragmentResult(CHANGE_EMAIL_REQUEST_KEY, Bundle().apply {
            putString(CHANGE_EMAIL_BUNDLE_KEY, result)
        })

        findNavController().navigateUp()
    }

}