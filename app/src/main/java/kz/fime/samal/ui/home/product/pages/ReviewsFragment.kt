package kz.fime.samal.ui.home.product.pages

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import kz.fime.samal.data.models.Review
import kz.fime.samal.databinding.FragmentProductDetailsReviewsBinding
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.ui.home.adapters.ReviewsAdapter
import kz.fime.samal.ui.home.product.ProductDetailsViewModel
import kz.fime.samal.utils.binding.BindingFragment

class ReviewsFragment :
    BindingFragment<FragmentProductDetailsReviewsBinding>(FragmentProductDetailsReviewsBinding::inflate) {

    private val viewModel: ProductDetailsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            val reviewsAdapter = ReviewsAdapter()
            rv.adapter = reviewsAdapter

            viewModel.productReviews.observeState(viewLifecycleOwner, {
                //
                if(it.result.isNullOrEmpty()){
                    contentEmtpy.root.visibility = View.VISIBLE
                    rv.visibility = View.GONE
                }else{
                    contentEmtpy.root.visibility = View.GONE
                    rv.visibility = View.VISIBLE
                    it.result?.let { reviews ->
                        reviewsAdapter.submitList(reviews)
                    }
                }

                }, {

                }, {

            })
        }
    }
}