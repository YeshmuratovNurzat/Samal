package kz.fime.samal.utils.binding

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kz.fime.samal.data.repositories.*

abstract class BindingFragment<B : ViewBinding> constructor(
    private val inflate: (inflater: LayoutInflater, container: ViewGroup?, b: Boolean) -> B?
) : Fragment() {

    private var _binding: B? = null
    protected val binding get() = _binding!!

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