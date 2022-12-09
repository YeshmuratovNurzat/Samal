package kz.fime.samal.ui.profile

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.fime.samal.R
import kz.fime.samal.data.models.custom.Status
import kz.fime.samal.databinding.DialogChangeNumberBinding
import kz.fime.samal.databinding.DialogOtpBinding
import kz.fime.samal.utils.EditTextUtils
import kz.fime.samal.utils.extensions.isLoading
import kz.fime.samal.utils.extensions.progressButton


class OTPDialog : BottomSheetDialogFragment() {

    private val viewModel: ProfileViewModel2 by activityViewModels()

    protected fun setBottomSheetCallback(v: View) {
        val mBottomSheetBehavior = BottomSheetBehavior.from(v.parent as View)
        mBottomSheetBehavior.addBottomSheetCallback(mBottomSheetBehaviorCallback)
    }

    private val mBottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN)
                dismiss()
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
        }
    }

    lateinit var binding: DialogOtpBinding

    companion object {

        val SUCCESS = "success"
        val FAILURE = "failure"
        val RESULT = "result_of_action"
        val EXCEPTION_OF_ACTION = "exception_of_action"

        fun newInstance(): OTPDialog {
            val dialog = OTPDialog()
            return dialog
        }
    }

    private lateinit var phone: String
    private lateinit var oldPhone: String

    override fun setupDialog(dialog: Dialog, style: Int) {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_otp,
            null,
            false
        )

        dialog.setContentView(binding.root)
        setBottomSheetCallback(binding.root)
        initView()
        observeViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getNewPhoneNumber().observe(this, Observer {
            phone = it
        })
        viewModel.getPhoneNumber().observe(this, Observer {
            oldPhone = it
        })
    }

    private fun initView() {
        binding.changeNumberText.text = String.format(getString(R.string.you_changing_number), phone, oldPhone)
        binding.otp.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 4)
                    viewModel.changePhone(phone, p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
    }

    private fun observeViewModel() {
        viewModel.resultChangePhone.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    setResult(SUCCESS)
                }
                Status.ERROR -> {
                    setResult(FAILURE)
                }
            }
        })
    }

    private fun setResult(result: String) {
        val bundle = Bundle()
        bundle.putString(RESULT, result)

        val intent = Intent()
        intent.putExtras(bundle)

        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
        dismiss()
    }

}