package kz.fime.samal.ui.catalog.pages

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kz.fime.samal.data.base.State
import kz.fime.samal.data.models.Banner
import kz.fime.samal.data.models.Product
import kz.fime.samal.data.models.Widget
import kz.fime.samal.data.repositories.CatalogRepository
import kz.fime.samal.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val catalogRepository: CatalogRepository
): BaseViewModel() {

    private val getCategoriesRequest = NetworkRequest { catalogRepository.getCategories() }
    val categories = getCategoriesRequest.liveData

    fun getCategories(){ getCategoriesRequest.call() }

    init {
        getCategories()
    }

}