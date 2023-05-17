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
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    var permissionsRequest: PermissionRequest? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setOnClickListener { findNavController().navigateUp() }
        initWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        url = arguments?.getString("url_installment")!!
        with(binding.webView.settings) {
            javaScriptEnabled = true
            allowFileAccessFromFileURLs = true
            allowUniversalAccessFromFileURLs = true
        }
        binding.webView.loadUrl(url)
        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
            }

            override fun onPermissionRequest(request: PermissionRequest?) {
                permissionsRequest = request
                val permissions = request!!.resources

                if (permissions.contains(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
                    val cameraPermission = Manifest.permission.CAMERA
                    val hasCameraPermission = ContextCompat.checkSelfPermission(
                        requireContext(),
                        cameraPermission
                    ) == PackageManager.PERMISSION_GRANTED

                    if (hasCameraPermission) {
                        request.grant(arrayOf(PermissionRequest.RESOURCE_VIDEO_CAPTURE))
                    } else {
                        requestPermissions(
                            arrayOf(cameraPermission),
                            CAMERA_PERMISSION_REQUEST_CODE
                        )
                    }
                }
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionsRequest?.grant(arrayOf(PermissionRequest.RESOURCE_VIDEO_CAPTURE))
            } else {
                binding.webView.reload()
            }
        }
    }
}