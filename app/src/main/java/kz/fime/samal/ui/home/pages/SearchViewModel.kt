package kz.fime.samal.ui.home.pages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.fime.samal.api.ApiResponse
import kz.fime.samal.data.models.custom.Resource
import kz.fime.samal.data.repositories.ProfileRepository
import kz.fime.samal.ui.base.BaseViewModel
import javax.inject.Inject
import kz.fime.samal.utils.extensions.Item

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : BaseViewModel() {

    private val _loadSearchProduct = MutableLiveData<String>()
    val resultSearch: LiveData<Resource<ApiResponse<List<Item>>>> = Transformations
        .switchMap(_loadSearchProduct) {
            profileRepository.loadSearchProduct(it)
        }

    private val _loadSearchShop = MutableLiveData<String>()
    val resultSearchShop: LiveData<Resource<ApiResponse<List<Item>>>> = Transformations
        .switchMap(_loadSearchShop) {
            profileRepository.loadShopSearch(it)
        }

    fun searchProduct(text: String) {
        _loadSearchProduct.postValue(text)
    }

    fun searchShop(text: String) {
        _loadSearchShop.postValue(text)
    }

}