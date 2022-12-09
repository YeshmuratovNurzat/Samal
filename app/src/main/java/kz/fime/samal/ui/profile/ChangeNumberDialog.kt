package kz.fime.samal.ui.profile

import android.app.Dialog
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
import kz.fime.samal.databinding.DialogEditPhoneBinding
import kz.fime.samal.utils.EditTextUtils
import kz.fime.samal.utils.FragmentResultKeys.Companion.CHANGE_NUMBER_BUNDLE_KEY
import kz.fime.samal.utils.FragmentResultKeys.Companion.CHANGE_NUMBER_REQUEST_KEY
import kz.fime.samal.utils.binding.BindingBottomSheetFragment
import kz.fime.samal.utils.extensions.isLoading
import kz.fime.samal.utils.extensions.progressButton


class ChangeNumberDialog : BindingBottomSheetFragment<DialogEditPhoneBinding>(DialogEditPhoneBinding::inflate) {

    private val viewModel: ProfileViewModel2 by activityViewModels()

    companion object {

        val SUCCESS = "success"
        val FAILURE = "failure"
    }

    private lateinit var phone: String

    override fun setupDialog(dialog: Dialog, style: Int) {
        initView()
        observeViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            progressButton(btnOk)
            EditTextUtils.setPhoneMask(etPhone)
            btnOk.setOnClickListener {
                viewModel.changePhoneRequest(phone)
            }
        }
        viewModel.getPhoneNumber().observe(this, {
            phone = it
        })
        observeViewModel()
    }

    private fun initView() {
    }

    private fun observeViewModel() {
        viewModel.resultChangePhoneRequest.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.btnOk.isLoading(true)
                }
                Status.SUCCESS -> {
                    binding.btnOk.isLoading(false)
                    viewModel.setNewPhoneNumber("7" + EditTextUtils.getPhoneUnmasked(binding.etPhone.text.toString()))
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

        setFragmentResult(CHANGE_NUMBER_REQUEST_KEY, Bundle().apply {
            putString(CHANGE_NUMBER_BUNDLE_KEY, result)
        })

        findNavController().navigateUp()
    }

}