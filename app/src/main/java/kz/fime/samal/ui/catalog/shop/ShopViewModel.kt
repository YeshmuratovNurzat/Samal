package kz.fime.samal.ui.catalog.shop

import dagger.hilt.android.lifecycle.HiltViewModel
import kz.fime.samal.data.models.Product
import kz.fime.samal.data.models.ProductDetailed
import kz.fime.samal.data.repositories.CatalogRepository
import kz.fime.samal.ui.base.BaseViewModel
import kz.fime.samal.utils.extensions.Item
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val catalogRepository: CatalogRepository
): BaseViewModel() {

    private val getShopRequest = NetworkRequest<Item>()
    val shop = getShopRequest.liveData

    private val getShopCategoriesRequest = NetworkRequest<List<Item>>()
    val shopCategories = getShopCategoriesRequest.liveData

    fun getShop(shopId: String){
        getShopRequest.call { catalogRepository.getShop(shopId) }
        getShopCategoriesRequest.call { catalogRepository.getShopCategories(shopId) }
    }


}