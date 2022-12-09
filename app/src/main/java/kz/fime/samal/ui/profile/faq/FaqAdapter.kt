package kz.fime.samal.ui.profile.faq

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import kz.fime.samal.R
import kz.fime.samal.data.models.FaqItem
import kz.fime.samal.databinding.ItemFaqBinding
import kz.fime.samal.utils.binding.BindingRvAdapter

class FaqAdapter(
    val context: Context
) : BindingRvAdapter<FaqItem, ItemFaqBinding>(ItemFaqBinding::inflate) {

    override fun bind(item: FaqItem, binding: ItemFaqBinding) {
        binding.run {
            answer.text = item.answer
            question.text = item.question
            if (item.isExpanded) {
                parentlayout.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_98))
                answer.visibility = View.VISIBLE
                question.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up_24, 0)
            } else {
                parentlayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                answer.visibility = View.GONE
                question.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down_24, 0)
            }
            question.setOnClickListener {
                expandClicked(!item.isExpanded, this)
                item.isExpanded = !item.isExpanded
            }
        }
    }

    private fun expandClicked(isExpand: Boolean, binding: ItemFaqBinding) {
        binding.run {
            if (isExpand) {
                parentlayout.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_98))
                answer.visibility = View.VISIBLE
                question.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up_24, 0)
            } else {
                parentlayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                answer.visibility = View.GONE
                question.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_down_24,
                    0
                )
            }
        }
    }

}