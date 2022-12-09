package kz.fime.samal.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import kz.fime.samal.R
import kz.fime.samal.data.models.custom.Status
import kz.fime.samal.databinding.DialogChangePhotoBinding
import kz.fime.samal.ui.base.handleException
import kz.fime.samal.utils.FragmentResultKeys
import kz.fime.samal.utils.RequestCodeEnums
import kz.fime.samal.utils.binding.BindingBottomSheetFragment
import kz.fime.samal.utils.extensions.loadUrl
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*

class ChangePhotoDialog: BindingBottomSheetFragment<DialogChangePhotoBinding>(DialogChangePhotoBinding::inflate) {

    private val viewModel: ProfileViewModel2 by activityViewModels()
    private val args: ChangePhotoDialogArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = args.url
        binding.run {
            ivUserPhoto.loadUrl(url, R.drawable.ic_avatar_placeholder)
            loadNewImage.setOnClickListener {
                chooseFromGallery()
            }
            deleteImage.setOnClickListener {
                viewModel.changeImage(buildImageBodyPart(
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_avatar_placeholder)!!.toBitmap()))
            }
        }
        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.resultChangePhoto.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    setResult("success")
                }
                Status.ERROR -> {
                    handleException(it.exception)
                    setResult("failure")
                }
            }
        })
    }

    private fun setResult(result: String) {
        setFragmentResult(FragmentResultKeys.CHANGE_PHOTO_REQUEST_KEY, Bundle().apply {
            putString(FragmentResultKeys.CHANGE_PHOTO_BUNDLE_KEY, result)
        })

        findNavController().navigateUp()
    }


    private fun chooseFromGallery() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, RequestCodeEnums.RESULT_LOAD_IMG.id)
    }

    private fun buildImageBodyPart(bitmap: Bitmap): MultipartBody.Part {
        val imageFile = convertBitmapToFile(bitmap)
        val reqFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imageFile)

        return MultipartBody.Part.createFormData("photo", "avatar.jpg", reqFile)
    }

    private fun convertBitmapToFile(bitmap: Bitmap): File {
        val file = File(requireContext().cacheDir, "avatar")
        file.createNewFile()

        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)
        val bitmapData = bos.toByteArray()

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos?.write(bitmapData)
            fos?.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            RequestCodeEnums.RESULT_LOAD_IMG.id -> {
                data?.data?.let {
                    try {
                        val imageUri: Uri = it
                        val bitmap: Bitmap = if (Build.VERSION.SDK_INT >= 29) {
                            ImageDecoder.decodeBitmap(
                                ImageDecoder.createSource(
                                    requireActivity().contentResolver,
                                    imageUri
                                )
                            )
                        } else {
                            MediaStore.Images.Media.getBitmap(
                                requireActivity().contentResolver,
                                imageUri
                            )
                        }
                        viewModel.changeImage(buildImageBodyPart(bitmap))
                        Glide.with(requireContext()).load(imageUri).into(binding.ivUserPhoto)

                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

}