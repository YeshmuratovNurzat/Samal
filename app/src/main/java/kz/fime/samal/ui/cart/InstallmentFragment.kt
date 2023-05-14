package kz.fime.samal.ui.cart


import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.navigation.fragment.findNavController
import kz.fime.samal.databinding.FragmentInstallmentBinding
import kz.fime.samal.utils.binding.BindingFragment


class InstallmentFragment : BindingFragment<FragmentInstallmentBinding>(FragmentInstallmentBinding::inflate) {

    var url: String = ""
    private val REQUEST_CAMERA_PERMISSION = 100
    private val REQUEST_PHOTO_CAPTURE = 200
    private lateinit var webView: WebView
    private var filePathCallback: ValueCallback<Array<Uri>>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setOnClickListener { findNavController().navigateUp() }
        initWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        url = arguments?.getString("url_installment")!!
//        url = "https://api.ffin.credit/ru/frames/cc1b4d82-accc-401d-9dec-640644009854/"
        with(binding.webView.settings){
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            this.allowContentAccess = true
            this.allowFileAccess = true

        }
        binding.webView.loadUrl(url)
        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
            }

            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                if (ContextCompat.checkSelfPermission( requireContext(), Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED )
                { ActivityCompat.requestPermissions( requireActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION )
                    return false
                }
                this@InstallmentFragment.filePathCallback = filePathCallback
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, REQUEST_PHOTO_CAPTURE)
                return true
            }
        }

        binding.webView.webViewClient = object : WebViewClient() {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view!!.loadUrl(request!!.url.toString())
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }
    }
}