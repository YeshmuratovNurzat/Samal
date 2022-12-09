package kz.fime.samal.ui.profile.faq

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kz.fime.samal.data.models.FaqResponse
import kz.fime.samal.data.models.custom.Status
import kz.fime.samal.databinding.FragmentFaqBinding
import kz.fime.samal.ui.profile.ProfileViewModel2
import kz.fime.samal.utils.binding.BindingFragment

class FaqFragment : BindingFragment<FragmentFaqBinding>(FragmentFaqBinding::inflate) {

    private val viewModel: ProfileViewModel2 by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }
        loadFaq()
        observeViewModel()
    }

    private fun loadFaq() {
        viewModel.loadFaq()
    }

    private fun observeViewModel() {
        viewModel.resultLoadFaq.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let {
                        drawFaq(it)
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {

                }
            }
        })
    }

    private fun drawFaq(faqList: FaqResponse) {
        val faqAdapter = FaqAdapter(requireContext())
        binding.faqRv.adapter = faqAdapter
        faqAdapter.submitList(faqList.data)
    }

}