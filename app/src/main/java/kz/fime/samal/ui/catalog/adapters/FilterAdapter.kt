package kz.fime.samal.ui.catalog.adapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kz.fime.samal.R
import kz.fime.samal.databinding.ItemFilterBinding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull


class FilterAdapter constructor(private val onItemClick: (item: Item) -> Unit
): BindingRvAdapter<Item, ItemFilterBinding>(ItemFilterBinding::inflate){

    var positionItem = -1

    override fun bind(item: Item, binding: ItemFilterBinding) {
        binding.apply {
            tvFilter.text = item.getOrNull("name","")
//            container.setOnClickListener {
//                onItemClick.invoke(item)
//            }
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder<Item, ItemFilterBinding>, position: Int) {
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