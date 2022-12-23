package kz.fime.samal.ui.cart

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
import kz.fime.samal.data.repositories.CartRepository
import kz.fime.samal.data.repositories.CatalogRepository
import kz.fime.samal.data.repositories.MainRepository
import kz.fime.samal.ui.base.BaseViewModel
import kz.fime.samal.utils.extensions.Item
import javax.inject.Inject


@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val mainRepository: MainRepository
): BaseViewModel() {

    private val getCartItemsRequest = NetworkRequest { cartRepository.getCartItems() }
    val cartItems = getCartItemsRequest.liveData

    fun loadCart(){
        getCartItemsRequest.call()
    }

    fun loadAddress(){
        clientAddresses.call()
    }

    private val clearCartRequest = NetworkRequest { cartRepository.clearCart() }
    val clearCart = clearCartRequest.liveData

    fun clearCart(){
        clearCartRequest.call()
    }

    private val updateCartItemRequest = NetworkRequestEvent<Item>(CoroutineScope(Dispatchers.IO))
    val updateCartItem = updateCartItemRequest.liveData

    fun updateCartItem(baskedId: String, count: Int) {
        updateCartItemRequest.call { cartRepository.updateCartItems(baskedId, count) }
    }

    private val getDeliveryCostRequest = NetworkRequestEvent<Item>()
    val deliveryCost = getDeliveryCostRequest.liveData

    fun getDeliveryCost(addressName: String){
        getDeliveryCostRequest.call { cartRepository.getDeliveryCost(addressName) }
    }

    val clientPoints = NetworkRequest { cartRepository.getClientPoints() }
    val paymentCards = NetworkRequest { cartRepository.getPaymentCards() }
    val deliveryTypes = NetworkRequest { cartRepository.getDeliveryTypes() }
    val paymentTypes = NetworkRequest { cartRepository.getPaymentTypes() }
    val clientAddresses = NetworkRequest { cartRepository.getClientAddresses() }
    val pickUpLocations = NetworkRequest { cartRepository.getPickUpLocations() }

    private val placeOrderRequest = NetworkRequestEvent<Item>()
    val placeOrder = placeOrderRequest.liveData

    fun placeOrder(baskets: List<String>,
                   deliverySlug: String,
                   paymentSlug: String,
                   addressSlug: String,
                   priceProduct: Int,
                   quota: Int){
        placeOrderRequest.call { mainRepository.placeOrder(baskets, deliverySlug, paymentSlug, addressSlug, priceProduct, quota)}
    }
}