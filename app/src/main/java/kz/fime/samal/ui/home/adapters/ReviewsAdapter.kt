package kz.fime.samal.ui.home.adapters

import kz.fime.samal.data.models.Review
import kz.fime.samal.databinding.ItemReviewBinding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.extensions.loadUrl

class ReviewsAdapter : BindingRvAdapter<Review, ItemReviewBinding>(ItemReviewBinding::inflate) {

    override fun bind(item: Review, binding: ItemReviewBinding) {
        binding.run {

            tvName.text = item.name
            tvDate.text = item.created_at
            iv.loadUrl(item.photo)
            rb.rating = item.rating ?: 0f
        }
    }
}