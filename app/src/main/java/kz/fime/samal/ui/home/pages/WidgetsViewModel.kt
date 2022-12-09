package kz.fime.samal.ui.home.pages

import dagger.hilt.android.lifecycle.HiltViewModel
import kz.fime.samal.data.models.Product
import kz.fime.samal.data.models.ProductDetailed
import kz.fime.samal.data.repositories.CatalogRepository
import kz.fime.samal.ui.base.BaseViewModel
import kz.fime.samal.utils.extensions.Item
import javax.inject.Inject

@HiltViewModel
class WidgetsViewModel @Inject constructor(
    private val catalogRepository: CatalogRepository
): BaseViewModel() {

    private val getProductsRequest = NetworkRequest<List<Product>>()
    val products = getProductsRequest.liveData

    fun getProducts(categorySlug: String){
        getProductsRequest.call { catalogRepository.getWidgetProducts(categorySlug) }
    }

    fun reset(){
        getProductsRequest.reset()
    }
}