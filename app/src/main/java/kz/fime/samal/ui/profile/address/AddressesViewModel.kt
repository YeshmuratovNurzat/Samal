package kz.fime.samal.ui.profile.address

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kz.fime.samal.data.repositories.MainRepository
import kz.fime.samal.ui.base.BaseViewModel
import kz.fime.samal.utils.extensions.Item
import javax.inject.Inject

@HiltViewModel
class AddressesViewModel @Inject constructor(
    private val mainRepository: MainRepository
): BaseViewModel() {

    private val getAddressesRequest = NetworkRequest{ mainRepository.getClientAddresses() }
    val addresses = getAddressesRequest.liveData

    private val getCitiesRequest = NetworkRequest{ mainRepository.getClientCities() }
    val cities = getCitiesRequest.liveData

    fun load(){
        getAddressesRequest.call()
        getCitiesRequest.call()
    }

    private val addAddressRequest = NetworkRequestEvent<Nothing>(CoroutineScope(Dispatchers.IO), ::load)
    val addAddress = addAddressRequest.liveData

    fun addAddress(cityId: Int, name: String, street: String, houseNumber: String, apartment: String, isDefault: Boolean){
        addAddressRequest.call { mainRepository.addClientAddress(cityId, name, street, houseNumber, apartment, isDefault) }
    }

}