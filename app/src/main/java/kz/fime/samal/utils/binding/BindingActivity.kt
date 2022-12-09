package kz.fime.samal.utils.binding

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding

abstract class BindingActivity<B : ViewBinding> constructor(
    private val inflate: (inflater: LayoutInflater) -> B?
) : AppCompatActivity() {

    private var _binding: B? = null
    protected val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = inflate.invoke(layoutInflater)
        if (_binding is ViewDataBinding) (_binding as ViewDataBinding).lifecycleOwner = this
        setContentView(_binding?.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}