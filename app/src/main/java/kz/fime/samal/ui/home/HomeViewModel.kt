package kz.fime.samal.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kz.fime.samal.data.base.State
import kz.fime.samal.data.models.Banner
import kz.fime.samal.data.models.BannerData
import kz.fime.samal.data.models.Product
import kz.fime.samal.data.models.Widget
import kz.fime.samal.data.repositories.CatalogRepository
import kz.fime.samal.ui.base.BaseViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val catalogRepository: CatalogRepository
): BaseViewModel() {

    private val _banners = MutableLiveData<State<BannerData>>()
    val banners:LiveData<State<BannerData>> = _banners

    private val _widgets = MutableLiveData<State<List<Widget>>>()
    val widgets:LiveData<State<List<Widget>>> = _widgets

    private val _state = MutableLiveData<State<Nothing>>()
    val state: LiveData<State<Nothing>> = _state

    fun loadData(){
        viewModelScope.launch {
            _state.postValue(State.Loading)
            val deferredBanners = async { catalogRepository.getBanners() }
            val deferredWidgets = async { catalogRepository.getWidgets() }
            val banners = deferredBanners.await()
            val widgets = deferredWidgets.await()
            if (banners is State.Success && widgets is State.Success) {
                _state.postValue(State.Success())
                _banners.postValue(banners)
                _widgets.postValue(widgets)
            } else {
                _state.postValue(State.NetworkError)
            }
        }
    }

    init {
        loadData()
    }

}