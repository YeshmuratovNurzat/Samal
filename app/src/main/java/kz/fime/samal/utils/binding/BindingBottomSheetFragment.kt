package kz.fime.samal.utils.binding

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.fime.samal.R

abstract class BindingBottomSheetFragment<B : ViewBinding> constructor(
    private val inflate: (inflater: LayoutInflater, container: ViewGroup?, b: Boolean) -> B?
) : BottomSheetDialogFragment() {

    private var _binding: B? = null
    protected val binding get() = _binding!!

    override fun getTheme(): Int {
        return R.style.BindingBottomSheet
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog is BottomSheetDialog) {
            dialog.behavior.skipCollapsed = true
            dialog.behavior.state = STATE_EXPANDED
        }
        return dialog
    }

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflate.invoke(inflater, container, false)
        if (_binding is ViewDataBinding) (_binding as ViewDataBinding).lifecycleOwner = viewLifecycleOwner
        return _binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}