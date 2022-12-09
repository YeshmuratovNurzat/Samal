package kz.fime.samal.ui.home.product

import dagger.hilt.android.lifecycle.HiltViewModel
import kz.fime.samal.data.models.Product
import kz.fime.samal.data.models.ProductDetailed
import kz.fime.samal.data.models.Review
import kz.fime.samal.data.repositories.CatalogRepository
import kz.fime.samal.data.repositories.MainRepository
import kz.fime.samal.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val catalogRepository: CatalogRepository,
    private val mainRepository: MainRepository
): BaseViewModel() {

    private val getProductDetailedRequest = NetworkRequest<ProductDetailed>()
    val productDetailed = getProductDetailedRequest.liveData

    private val getProductReviewsRequest = NetworkRequest<List<Review>>()
    val productReviews = getProductReviewsRequest.liveData

    fun getProductReviews(shopSlug: String, productSlug: String){
        getProductReviewsRequest.call { catalogRepository.getReviews(shopSlug, productSlug) }
    }

    fun getProductDetailed(product: Product){
        getProductDetailedRequest.call { catalogRepository.getProduct(product) }
    }

    fun getProductDetailed(shopSlug: String, productSlug: String){
        getProductDetailedRequest.call { catalogRepository.getProductDetails(shopSlug, productSlug) }
    }

    fun reset(){
        getProductDetailedRequest.reset()
    }

    private val addCartItemRequest = NetworkRequestEvent<Nothing>()
    val addCartItem = addCartItemRequest.liveData

    fun addCartItem(shopId: String, productSlug: String, productVariant: Int){
        addCartItemRequest.call { catalogRepository.addCartItem(shopId, productSlug, productVariant) }
    }

    private val toggleFavoritesRequest = NetworkRequestEvent<Nothing>()
    val toggleFavorites = toggleFavoritesRequest.liveData

    fun toggleFavorites(shopId: String, productSlug: String){
        toggleFavoritesRequest.call { mainRepository.toggleFavorites(shopId, productSlug) }
    }
}