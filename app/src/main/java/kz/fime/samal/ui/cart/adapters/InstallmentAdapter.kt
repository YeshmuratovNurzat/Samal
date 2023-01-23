package kz.fime.samal.ui.cart.adapters

import android.view.View
import android.widget.ImageView
import kz.fime.samal.R
import kz.fime.samal.databinding.ItemFilterBinding
import kz.fime.samal.databinding.ItemInstallmentBinding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull
import kz.fime.samal.utils.extensions.loadUrl

class InstallmentAdapter(private val onItemClick: (item: Item) -> Unit) : BindingRvAdapter<Item, ItemInstallmentBinding>(ItemInstallmentBinding::inflate) {

    var positionItem = -1

    override fun bind(item: Item, binding: ItemInstallmentBinding) {
        binding.run {
            tvDescription.text = item.getOrNull("short_description", "")
            iv.setImageResource(R.drawable.ic_home_credit_bank)
            vgContainer.setOnClickListener { onItemClick.invoke(item) }
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder<Item, ItemInstallmentBinding>, position: Int) {
        super.onBindViewHolder(viewHolder, position)

        val check = viewHolder.itemView.findViewById<ImageView>(R.id.check)

        if (position != positionItem) {
            check.visibility = View.GONE
        } else {
            check.visibility = View.VISIBLE
        }

        viewHolder.itemView.setOnClickListener(View.OnClickListener {
            positionItem = position
            onItemClick.invoke(items[position])
            notifyDataSetChanged()
        })
    }
}