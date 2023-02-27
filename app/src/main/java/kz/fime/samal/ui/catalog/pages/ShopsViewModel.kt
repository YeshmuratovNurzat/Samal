package kz.fime.samal.ui.catalog.pages

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kz.fime.samal.api.ApiResponse
import kz.fime.samal.data.base.State
import kz.fime.samal.data.models.Banner
import kz.fime.samal.data.models.Product
import kz.fime.samal.data.models.Widget
import kz.fime.samal.data.models.custom.Resource
import kz.fime.samal.data.repositories.CatalogRepository
import kz.fime.samal.data.repositories.ProfileRepository
import kz.fime.samal.ui.base.BaseViewModel
import kz.fime.samal.ui.catalog.category.CategoryProductsViewModel
import kz.fime.samal.utils.extensions.Item
import javax.inject.Inject

@HiltViewModel
class ShopsViewModel @Inject constructor(
    private val catalogRepository: CatalogRepository,
    private val profileRepository: ProfileRepository
): BaseViewModel() {

//    private val getShopsRequest = NetworkRequest { catalogRepository.getShops() }
//    val shops = getShopsRequest.liveData
//
//    fun getShops(){ getShopsRequest.call() }

    private val _loadShops =
        MutableLiveData<ShopsRequestModel>()
    val resultShops: LiveData<Resource<ApiResponse<List<Item>>>> = Transformations
        .switchMap(_loadShops) {
            profileRepository.loadShops(it.page)
        }


    fun getShops(page: String?) {
        _loadShops.postValue(
            ShopsRequestModel(page)
        )
    }


    data class ShopsRequestModel(
        val page: String?,
    )

    init {
        getShops("1")
    }

}