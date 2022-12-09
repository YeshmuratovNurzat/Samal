package kz.fime.samal.ui.profile.favorites

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kz.fime.samal.data.models.Product
import kz.fime.samal.data.models.ProductDetailed
import kz.fime.samal.data.repositories.CatalogRepository
import kz.fime.samal.data.repositories.MainRepository
import kz.fime.samal.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val mainRepository: MainRepository
): BaseViewModel() {

    private val getFavoritesRequest = NetworkRequest{ mainRepository.getFavorites() }
    val favorites = getFavoritesRequest.liveData

    private val toggleFavoritesRequest = NetworkRequestEvent<Nothing>()
    val toggleFavorites = toggleFavoritesRequest.liveData

    fun load(){
        getFavoritesRequest.call()
    }

    fun toggleFavorites(shopId: String, productSlug: String){
        toggleFavoritesRequest.call { mainRepository.toggleFavorites(shopId, productSlug) }
    }

    private val clearFavoritesRequest = NetworkRequestEvent(CoroutineScope(Dispatchers.IO), ::load) { mainRepository.clearFavorites() }
    val clearFavorites = clearFavoritesRequest.liveData

    fun clearFavorites(){
        clearFavoritesRequest.call()
    }
//    init { load() }

}