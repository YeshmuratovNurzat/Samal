package kz.fime.samal.ui.catalog.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.fime.samal.api.ApiResponse
import kz.fime.samal.data.models.CategoryModel
import kz.fime.samal.data.models.Product
import kz.fime.samal.data.models.ProductDetailed
import kz.fime.samal.data.models.custom.Resource
import kz.fime.samal.data.repositories.CatalogRepository
import kz.fime.samal.data.repositories.ProfileRepository
import kz.fime.samal.ui.base.BaseViewModel
import kz.fime.samal.utils.extensions.Item
import javax.inject.Inject

@HiltViewModel
class CategoryProductsViewModel @Inject constructor(
    private val catalogRepository: CatalogRepository,
    private val profileRepository: ProfileRepository
): BaseViewModel() {

    private val getProductsRequest = NetworkRequest<List<Item>>()
    val products = getProductsRequest.liveData

    private val _loadCategoryProducts = MutableLiveData<CategoryProductsRequestModel>()
    val resultProducts: LiveData<Resource<ApiResponse<List<Item>>>> = Transformations
        .switchMap(_loadCategoryProducts) {
            profileRepository.loadCategoryProducts(it.categorySlug, it.body, it.page)
        }

    private val _loadSubcategory = MutableLiveData<String>()
    val resultSubcategory: LiveData<Resource<CategoryModel>> = Transformations
        .switchMap(_loadSubcategory) {
            profileRepository.loadSubcategory(it)
        }

    fun getProducts(categorySlug: String, page: String?){
        _loadCategoryProducts.postValue(
            CategoryProductsRequestModel(
            categorySlug, page, hashMapOf(Pair("sortDir", "desc"), Pair("sortBy", "min_price"))
        ))
    }

    fun getSubcategory(categorySlug: String) {
        _loadSubcategory.postValue(categorySlug)
    }

    fun reset(){
        getProductsRequest.reset()
    }

    data class CategoryProductsRequestModel(
        val categorySlug: String,
        val page: String?,
        val body: Item
    )
}