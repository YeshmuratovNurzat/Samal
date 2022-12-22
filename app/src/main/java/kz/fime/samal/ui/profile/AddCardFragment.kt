package kz.fime.samal.ui.profile

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import kz.fime.samal.databinding.FragmentAddCardBinding
import kz.fime.samal.utils.binding.BindingFragment

class AddCardFragment : BindingFragment<FragmentAddCardBinding>(FragmentAddCardBinding::inflate) {

    var url: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setOnClickListener { findNavController().navigateUp() }
        initWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        url = arguments?.getString("url")!!
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl(url)
        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
            }
        }

        binding.webView.webViewClient = object : WebViewClient() {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view!!.loadUrl(request!!.url.toString())
                return true
            }
        }
    }
}
