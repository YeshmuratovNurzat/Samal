package kz.fime.samal.ui.profile.address

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import kz.fime.samal.R
import kz.fime.samal.databinding.ItemAddressBinding
import kz.fime.samal.databinding.ItemOrderBinding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.extensions.InnerItem
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull

class AddressesAdapter(
    private val onItemClick: (Item) -> Unit
): BindingRvAdapter<Item, ItemAddressBinding>(ItemAddressBinding::inflate) {

    var positionItem = -1

    override fun bind(item: Item, binding: ItemAddressBinding) {
        binding.run {
            tvTitle.text = item.getOrNull("name", "")
            tvSubtitle.text = "${item.getOrNull("street", "")}, ${item.getOrNull("house_number", "")}, ${item.getOrNull("apartment", "")}"
            //root.setOnClickListener { onItemClick.invoke(item) }
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder<Item, ItemAddressBinding>, position: Int) {
        super.onBindViewHolder(viewHolder, position)

        val check = viewHolder.itemView.findViewById<ImageView>(R.id._iv_arrow)

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