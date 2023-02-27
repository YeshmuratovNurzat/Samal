package kz.fime.samal.ui.catalog.subcategory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.R
import kz.fime.samal.data.models.custom.Status
import kz.fime.samal.databinding.FragmentSubcategory2Binding
import kz.fime.samal.databinding.FragmentSubcategoryBinding
import kz.fime.samal.ui.catalog.category.CategoryProductsViewModel
import kz.fime.samal.utils.binding.BindingFragment
@AndroidEntryPoint
class Subcategory2Fragment : BindingFragment<FragmentSubcategory2Binding>(FragmentSubcategory2Binding::inflate) {

    private val viewModel: CategoryProductsViewModel by viewModels()
    private var shopId: String? = null
    private var slug: String? = null
    private var name: String? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            toolbar.title = arguments?.getString("name")
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

            slug = arguments?.getString("slug")
            shopId = arguments?.getString("shop_id")
            name = arguments?.getString("name")

            viewModel.getSubcategory(slug.toString())

            observeViewModel()
        }
    }


    private fun observeViewModel() {
        viewModel.resultSubcategory.observe(viewLifecycleOwner, Observer {
            it.data
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    it.data?.let {
                        val adapter = SubcategoryAdapter {
                            findNavController().navigate(R.id.action_global_category_products, bundleOf(Pair("name", it.name), Pair("slug", it.category_slug), Pair("shop_id", shopId)))
                        }
                        adapter.submitList(it.data)
                        binding.run {
                            subcategory2Rv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                            subcategory2Rv.adapter = adapter
                        }
                    }
                }
                Status.ERROR -> {
                    findNavController().popBackStack()
                    findNavController().navigate(R.id.action_global_category_products, bundleOf(Pair("name", name),Pair("slug", slug),Pair("shop_id", shopId)))
                }
            }
        })
    }
}