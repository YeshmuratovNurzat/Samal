package kz.fime.samal.ui.qr

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.fime.samal.R
import kz.fime.samal.data.SessionManager
import kz.fime.samal.databinding.FragmentQrBinding
import kz.fime.samal.ui.AuthActivity
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.components.*
import kz.fime.samal.utils.extensions.changeIconTint
import timber.log.Timber

private const val RC_PERMISSION = 10

class QRFragment: BindingFragment<FragmentQrBinding>(FragmentQrBinding::inflate) {

    private var mPermissionGranted = false
    private lateinit var mCodeScanner: CodeScanner

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBottomBar()
        if (SessionManager.token.isEmpty()) {
            binding.vgBottomBar.visibility = View.GONE
            binding.vgErrorAuth.visibility = View.VISIBLE
            binding.vgErrorPermission.visibility = View.GONE
            binding.qrScanner.visibility = View.GONE
            setUpToolbar(true)
            binding.btnAuth.setOnClickListener {
                startActivity(Intent(requireActivity(), AuthActivity::class.java))
                requireActivity().finish()
            }
        } else {
            BottomBar.getBottomBar()?.hide()
            binding.vgBottomBar.visibility = View.VISIBLE
            binding.vgErrorAuth.visibility = View.GONE
            binding.qrScanner.visibility = View.VISIBLE
            setUpToolbar(false)
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED) {
                Timber.d("Need permission")
                mPermissionGranted = false
                binding.vgErrorPermission.visibility = View.VISIBLE
                requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    RC_PERMISSION
                )
            } else {
                Timber.d("Already has permission")
                mPermissionGranted = true
                binding.qrScanner.visibility = View.VISIBLE
                binding.vgErrorPermission.visibility = View.GONE
            }
        }

        binding.run {
            mCodeScanner = CodeScanner(requireContext(), qrScanner)
            mCodeScanner.setDecodeCallback {
                CoroutineScope(Dispatchers.Main).launch {
                    val dialog = ScanResultDialog(requireContext(), it)
                    dialog.setOnDismissListener { mCodeScanner.startPreview() }
                    dialog.show()
                }
            }
            mCodeScanner.setErrorCallback {
                it.printStackTrace()
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Произошла ошибка!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Timber.d("onRequestPermissionsResult")
        if (requestCode == RC_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = true
                mCodeScanner.startPreview()
                binding.qrScanner.visibility = View.VISIBLE
                binding.vgErrorPermission.visibility = View.GONE
            } else {
                mPermissionGranted = false
                binding.vgErrorPermission.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (mPermissionGranted) {
            mCodeScanner.startPreview()
        }
    }

    override fun onPause() {
        mCodeScanner.releaseResources()
        super.onPause()
    }

    private fun setUpToolbar(isQr: Boolean){
        binding.run {
            if (!isQr) {
                toolbar.setTitleTextColor(Color.WHITE)
                toolbar.changeIconTint(R.id.info, 1f)
            } else {
                toolbar.setTitleTextColor(Color.BLACK)
                toolbar.changeIconTint(R.id.info, 0f)
            }
        }
    }
    private fun setUpBottomBar(){
        binding.run {
            toolbar.setOnMenuItemClickListener {
                if (it.itemId == R.id.info) {
                    findNavController().navigate(R.id.action_global_qr_info)
                }
                false
            }
            btnHome.setOnClickListener { BottomBar.getBottomBar()?.selectItemIndex(0) }
            btnCatalog.setOnClickListener { BottomBar.getBottomBar()?.selectItemIndex(1)  }
            btnCart.setOnClickListener { BottomBar.getBottomBar()?.selectItemIndex(3)  }
            btnAcconut.setOnClickListener { BottomBar.getBottomBar()?.selectItemIndex(4)  }
            btnRequestPermission.setOnClickListener {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), RC_PERMISSION)
            }
        }
    }

}