package kz.fime.samal.ui.profile.address

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kz.fime.samal.data.models.order_detail.ClientAddress
import kz.fime.samal.data.repositories.MainRepository
import kz.fime.samal.ui.base.BaseViewModel
import kz.fime.samal.utils.extensions.Item
import javax.inject.Inject

@HiltViewModel
class AddressesViewModel @Inject constructor(
    private val mainRepository: MainRepository
): BaseViewModel() {

    var addressMap: MutableLiveData<Map<String, String>>? = MutableLiveData()

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

    fun addAddress(cityId: Int, name: String, street: String, houseNumber: String, apartment: String, isDefault: Boolean, latitude: String, longitude: String){
        addAddressRequest.call { mainRepository.addClientAddress(cityId, name, street, houseNumber, apartment, isDefault, latitude, longitude) }
    }


    private val getClientAddressDetailsRequest = NetworkRequest<ClientAddress>()
    val clientAddressDetails = getClientAddressDetailsRequest.liveData

    fun getClientAddressDetails(addressId: String){
        getClientAddressDetailsRequest.call {mainRepository.getClientAddressDetails(addressId)}
    }


    private val updateAddressRequest = NetworkRequestEvent<Nothing>(CoroutineScope(Dispatchers.IO), ::load)
    val updateAddress = updateAddressRequest.liveData

    fun updateAddress(addressId: String, cityId: Int, name: String, street: String, houseNumber: String, apartment: String, isDefault: Boolean, latitude: String, longitude: String){
        updateAddressRequest.call { mainRepository.updateClientAddress(addressId, cityId, name, street, houseNumber, apartment, isDefault, latitude, longitude) }
    }

    private val deleteAddressRequest = NetworkRequestEvent<Nothing>(CoroutineScope(Dispatchers.IO), ::load)
    val deleteAddress = deleteAddressRequest.liveData

    fun deleteAddress(addressId: String){
        deleteAddressRequest.call { mainRepository.deleteClientAddress(addressId) }
    }
}