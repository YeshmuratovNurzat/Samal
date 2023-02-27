package kz.fime.samal.ui.profile.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kz.fime.samal.api.ApiResponse
import kz.fime.samal.data.models.OrderRequestModel
import kz.fime.samal.data.models.custom.Resource
import kz.fime.samal.data.models.order_detail.Order
import kz.fime.samal.data.models.order_detail.OrderDetailResponse
import kz.fime.samal.data.repositories.MainRepository
import kz.fime.samal.data.repositories.ProfileRepository
import kz.fime.samal.ui.base.BaseViewModel
import kz.fime.samal.utils.extensions.Item
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val profileRepository: ProfileRepository

): BaseViewModel() {

    private val getOrdersRequest = NetworkRequest{mainRepository.getOrders()}
    val orders = getOrdersRequest.liveData

    private val getOrderDetailRequest = NetworkRequest<OrderDetailResponse>()
    val orderDetailed = getOrderDetailRequest.liveData

    fun load(){
        getOrdersRequest.call()
    }

    private val _loadOrders = MutableLiveData<OrdersRequestModel>()
    val resultOrders: LiveData<Resource<ApiResponse<List<Item>>>> = Transformations
        .switchMap(_loadOrders) {
            profileRepository.getOrders(it.page)
        }

    fun getOrders(page: String?){
        _loadOrders.postValue(OrdersRequestModel(page))
    }

    fun loadDetails(uuid: String) {
        getOrderDetailRequest.call {
            mainRepository.getOrderDetails(uuid)
        }
    }

    private val placeOrderRequest = NetworkRequestEvent<Item>(CoroutineScope(Dispatchers.IO))
    val placeOrder = placeOrderRequest.liveData

    private val cancelOrderRequest = NetworkRequestEvent<Nothing>(CoroutineScope(Dispatchers.IO), ::load)
    val cancelOrder = cancelOrderRequest.liveData

    fun cancelOrder(orderId: String) {
        cancelOrderRequest.call {mainRepository.cancelOrder(orderId)}
    }

    data class OrdersRequestModel(
        val page: String?
    )

//    fun placeOrder(){
//        placeOrderRequest.call { mainRepository.placeOrder() }
//    }

    init {
        getOrders("")
    }

}