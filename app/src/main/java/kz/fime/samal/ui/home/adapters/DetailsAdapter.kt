package kz.fime.samal.ui.home.adapters

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import kz.fime.samal.data.models.Product
import kz.fime.samal.databinding.ItemDescriptionBinding
import kz.fime.samal.databinding.ItemProductBinding
import kz.fime.samal.utils.binding.BindingRvAdapter
import kz.fime.samal.utils.extensions.loadUrl

class DetailsAdapter: BindingRvAdapter<HashMap<String, Any>, ItemDescriptionBinding>(ItemDescriptionBinding::inflate) {

    override fun bind(item: HashMap<String, Any>, binding: ItemDescriptionBinding) {
        binding.run {

        }
    }

}