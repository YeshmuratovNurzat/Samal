package kz.fime.samal.ui.home.product

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.R
import kz.fime.samal.data.models.Product
import kz.fime.samal.databinding.FragmentProductDetailsBinding
import kz.fime.samal.ui.base.observeEvent
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.ui.home.product.pages.DescriptionFragment
import kz.fime.samal.ui.home.product.pages.ReviewsFragment
import kz.fime.samal.ui.profile.favorites.Favorites
import kz.fime.samal.utils.MessageUtils
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.components.BottomBar
import kz.fime.samal.utils.extensions.*
import timber.log.Timber

@AndroidEntryPoint
class ProductDetailsFragment: BindingFragment<FragmentProductDetailsBinding>(FragmentProductDetailsBinding::inflate) {

    private val viewModel: ProductDetailsViewModel by activityViewModels()

    private var toggle = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            BottomBar.getBottomBar()?.showProductDetails({ findNavController().navigateUp() }, {
                Timber.d("Clicked share")
            })

            ibClose.setOnClickListener { findNavController().navigateUp() }
            viewModel.reset()
            val product = arguments?.getParcelable<Product>(ITEM_ARG)!!
            toggle = Favorites.isFavorite(product.shop_slug!!, product.product_slug!!)
            updateFav()
            Timber.d("Arg: %s", product)
            viewModel.getProductDetailed(product.shop_slug!!, product.product_slug!!)
            viewModel.getProductReviews(product.shop_slug!!, product.product_slug!!)

            val imagesAdapter = ImagesAdapter()
            setUpVpAdapter(vpImages, imagesAdapter, dotsImages)
            setUpPagesAdapter(vpDetails, listOf(DescriptionFragment(), ReviewsFragment()), Pair(listOf("Описание", "Отзывы"), tabs))

            viewModel.toggleFavorites.observeEvent(viewLifecycleOwner, {
                MessageUtils.postMessage(it.message)
                toggle = !toggle
                updateFav()
            })
            viewModel.productDetailed.observeState(viewLifecycleOwner, {
                Timber.d("Got: %s", it)
                it.result?.let { productDetailed ->
                    imagesAdapter.submitList(productDetailed.images)
                    tvName.text = product.name
                    tvCategory.text = productDetailed.categories.firstOrNull().getOrNull("name", "")
                    tvPrice.text = "${productDetailed.price.getOrNull("min", "")} ₸"

                    BottomBar.getBottomBar()?.showProductDetails({ findNavController().navigateUp() }, like = {
                        viewModel.toggleFavorites(product.shop_slug!!, product.product_slug!!)
                        BottomBar.getBottomBar()?.getBinding()?.btnLike?.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_like)
                    }, addToCart = {
                        Timber.d("Adding Item: %s", product.product_slug)
                        if (productDetailed.options.isNullOrEmpty()){

                        viewModel.addCartItem(
                            //productDetailed.shop_uuid!!   -> shop_uuid возвращает null ошибка NullPointerException
                            product.shop_slug,
                            product.product_slug,
                            productDetailed.product_variants!![0].product_variant_id
                        )
                        }
                        else{
                            findNavController().navigate(R.id.action_global_options, bundleOf(Pair("shop_uuid",productDetailed.shop_uuid), Pair("product_slug", product.product_slug),
                                Pair("variants", productDetailed.product_variants)))
                        }
                    }, share = {
                        val text: String =
                            productDetailed.images?.firstOrNull()?.url + "\n" + productDetailed.name + "\n" + productDetailed.categories.firstOrNull().getOrNull("name", "") + "\n" + productDetailed.price.getOrNull("min", "") +"₸\n" + productDetailed.description.asHtml()
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "text/plain"
                        intent.putExtra(Intent.EXTRA_TEXT, text)
                        startActivity(Intent.createChooser(intent,"Поделиться через..."))
                    })

                }
            }, {
                Timber.d("Error: %s", it)
            }, {
                Timber.d("Loading: %s", it)
            })

            viewModel.addCartItem.observeEvent(viewLifecycleOwner, {
                MessageUtils.postMessage("Товар добавлен")
            })
        }
    }

    private fun updateFav(){
        if (toggle){
            BottomBar.getBottomBar()?.getBinding()?.btnLike?.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_like)
        } else {
            BottomBar.getBottomBar()?.getBinding()?.btnLike?.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_like_outlined)
        }
    }

}