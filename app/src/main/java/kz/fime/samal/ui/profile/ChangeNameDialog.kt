package kz.fime.samal.ui.profile

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.fime.samal.R
import kz.fime.samal.data.models.custom.Status
import kz.fime.samal.databinding.DialogChangeNameBinding
import kz.fime.samal.databinding.DialogChangeNumberBinding
import kz.fime.samal.databinding.DialogEditNameBinding
import kz.fime.samal.utils.EditTextUtils
import kz.fime.samal.utils.FragmentResultKeys
import kz.fime.samal.utils.binding.BindingBottomSheetFragment
import kz.fime.samal.utils.extensions.isLoading
import kz.fime.samal.utils.extensions.progressButton


class ChangeNameDialog : BindingBottomSheetFragment<DialogEditNameBinding>(DialogEditNameBinding::inflate) {

    private val viewModel: ProfileViewModel2 by activityViewModels()

    companion object {

        val SUCCESS = "success"
        val FAILURE = "failure"
        val RESULT = "result_of_action"
        val EXCEPTION_OF_ACTION = "exception_of_action"

        fun newInstance(): ChangeNameDialog {
            val dialog = ChangeNameDialog()
            return dialog
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            progressButton(btnOk)

            btnOk.setOnClickListener {
                if (binding.etName.text.toString().isNotEmpty())
                    viewModel.changeName(binding.etName.text.toString())
            }
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.resultChangeName.observe(this, Observer {
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
        setFragmentResult(FragmentResultKeys.CHANGE_NAME_REQUEST_KEY, Bundle().apply {
            putString(FragmentResultKeys.CHANGE_NAME_BUNDLE_KEY, result)
        })
        findNavController().navigateUp()
    }

}