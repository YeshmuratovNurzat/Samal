package kz.fime.samal.ui.home.product.pages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import kz.fime.samal.databinding.FragmentProductDetailsDescriptionBinding
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.ui.home.product.ProductDetailsViewModel
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.extensions.asHtml

class DescriptionFragment: BindingFragment<FragmentProductDetailsDescriptionBinding>(FragmentProductDetailsDescriptionBinding::inflate) {

    private val viewModel: ProductDetailsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {

            viewModel.productDetailed.observeState(viewLifecycleOwner, {
                it.result?.let { product ->
                    tvContent.text = product.description.asHtml()
                }
            }, {

            }, {

            })
        }
    }

}