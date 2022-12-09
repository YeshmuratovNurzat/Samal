package kz.fime.samal.ui.profile.orders

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kz.fime.samal.data.models.order_detail.OrderDetailResponse
import kz.fime.samal.data.repositories.MainRepository
import kz.fime.samal.ui.base.BaseViewModel
import kz.fime.samal.utils.extensions.Item
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val mainRepository: MainRepository
): BaseViewModel() {

    private val getOrdersRequest = NetworkRequest{ mainRepository.getOrders() }
    val orders = getOrdersRequest.liveData

    private val getOrderDetailRequest = NetworkRequest<OrderDetailResponse>()
    val orderDetailed = getOrderDetailRequest.liveData

    fun load(){
        getOrdersRequest.call()
    }

    fun loadDetails(uuid: String) {
        getOrderDetailRequest.call {
            mainRepository.getOrderDetails(uuid)
        }
    }

    private val placeOrderRequest = NetworkRequestEvent<Item>(CoroutineScope(Dispatchers.IO))
    val placeOrder = placeOrderRequest.liveData

//    fun placeOrder(){
//        placeOrderRequest.call { mainRepository.placeOrder() }
//    }

}