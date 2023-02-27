package kz.fime.samal.ui.cart

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kz.fime.samal.data.repositories.CartRepository
import kz.fime.samal.data.repositories.MainRepository
import kz.fime.samal.ui.base.BaseViewModel
import kz.fime.samal.utils.extensions.Item
import javax.inject.Inject


@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val mainRepository: MainRepository
): BaseViewModel() {

    var fetchPickUp: MutableLiveData<Item>? = MutableLiveData()

    private val getCartItemsRequest = NetworkRequest { cartRepository.getCartItems() }
    val cartItems = getCartItemsRequest.liveData

    private val getAddressesRequest = NetworkRequest{ mainRepository.getClientAddresses() }
    val addresses = getAddressesRequest.liveData

    fun loadCart(){
        getCartItemsRequest.call()
    }

    fun loadAddress(){
        getAddressesRequest.call()
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
    val installment = NetworkRequest { mainRepository.getInstallment() }

    private val placeOrderRequest = NetworkRequestEvent<Item>()
    val placeOrder = placeOrderRequest.liveData

    fun placeOrder(baskets: List<String>,
                   deliverySlug: String,
                   pick_up_point_slug: String,
                   paymentSlug: String,
                   installment_uuid : String,
                   addressSlug: String,
                   priceProduct: Int,
                   quota: Int){
        placeOrderRequest.call { mainRepository.placeOrder(baskets, deliverySlug, pick_up_point_slug, paymentSlug, installment_uuid, addressSlug, priceProduct, quota)}
    }
}