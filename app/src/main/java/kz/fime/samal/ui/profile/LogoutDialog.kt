package kz.fime.samal.ui.profile

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.fime.samal.R
import kz.fime.samal.data.models.custom.Status
import kz.fime.samal.databinding.DialogLogOutBinding
import kz.fime.samal.utils.extensions.isLoading
import kz.fime.samal.utils.extensions.progressButton


class LogoutDialog : BottomSheetDialogFragment() {

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

    lateinit var binding: DialogLogOutBinding

    companion object {

        val SUCCESS = "success"
        val FAILURE = "failure"
        val RESULT = "result_of_action"
        val EXCEPTION_OF_ACTION = "exception_of_action"

        fun newInstance(): LogoutDialog {
            val dialog = LogoutDialog()
            return dialog
        }
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
//        binding = DataBindingUtil.inflate(
//            LayoutInflater.from(context),
//            R.layout.dialog_log_out,
//            null,
//            false
//        )

        dialog.setContentView(binding.root)
        setBottomSheetCallback(binding.root)
        initView()
        observeViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressButton(binding.btnOk)
    }

    private fun initView() {
        binding.btnOk.setOnClickListener {
            viewModel.logoutUser()
        }
    }

    private fun observeViewModel() {
        viewModel.resultLogout.observe(this, Observer {
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
        val bundle = Bundle()
        bundle.putString(RESULT, result)

        val intent = Intent()
        intent.putExtras(bundle)

        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
        dismiss()
    }

}