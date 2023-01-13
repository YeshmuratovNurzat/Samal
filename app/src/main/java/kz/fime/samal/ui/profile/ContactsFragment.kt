package kz.fime.samal.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kz.fime.samal.data.models.AboutResponse
import kz.fime.samal.data.models.custom.Status
import kz.fime.samal.databinding.FragmentContactsBinding
import kz.fime.samal.utils.EditTextUtils
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.extensions.asHtml
import kz.fime.samal.utils.extensions.loadUrl

class ContactsFragment : BindingFragment<FragmentContactsBinding>(FragmentContactsBinding::inflate) {

    private val viewModel: ProfileViewModel2 by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }
        loadData()
        observeViewModel()
    }

    private fun loadData() {
        viewModel.loadAbout()
    }

    private fun observeViewModel() {
        viewModel.resultLoadAbout.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let {
                        it.data?.let { it1 -> drawAbout(it1) }
                    }
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }
            }
        }
    }

    private fun drawAbout(about: AboutResponse) {
        Glide.with(this).load(about.url_map).into(binding.mapImg)
        binding.addressTv.text = about.address
        binding.phone1.text = EditTextUtils.getPhoneMasked(about.phone1)
        binding.phone2.text = EditTextUtils.getPhoneMasked(about.phone2)
        binding.phone1.setOnClickListener {
            Log.d("MyLog","click")
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel: ${binding.phone1.text}")
            startActivity(callIntent)
        }

        binding.phone2.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data =  Uri.parse("tel: ${binding.phone2.text}")
            startActivity(callIntent)
        }
        binding.aboutCompany.text = about.description.replace("<p>","").replace("</p>","")
    }

}