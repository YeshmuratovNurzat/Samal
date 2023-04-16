package kz.fime.samal.ui.profile.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.fime.samal.api.ApiResponse
import kz.fime.samal.data.models.custom.Resource
import kz.fime.samal.data.repositories.MainRepository
import kz.fime.samal.data.repositories.ProfileRepository
import kz.fime.samal.ui.base.BaseViewModel
import kz.fime.samal.ui.catalog.category.CategoryProductsViewModel
import kz.fime.samal.utils.extensions.Item
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val profileRepository: ProfileRepository
): BaseViewModel() {


//    private val getNotificationsRequest =  NetworkRequest { mainRepository.notifications() }
//    val notifications = getNotificationsRequest.liveData

//    fun notificationsLoad() {
//        getNotificationsRequest.call()
//    }

    private val _loadNotifications = MutableLiveData<NotificationsRequestModel>()
    val resultNotifications: LiveData<Resource<ApiResponse<List<Item>>>> = Transformations
        .switchMap(_loadNotifications) {
            profileRepository.loadNotifications(it.page)
        }

    private val getNotificationsReadAll = NetworkRequest { mainRepository.notificationsReadAll() }
    val notificationsReadAll = getNotificationsReadAll.liveData

    fun getNotifications(page: String?){
        _loadNotifications.postValue(NotificationsRequestModel(page))
    }

    fun notificationsReadAll() {
        getNotificationsReadAll.call()
    }

    private val getNotificationReadRequest = NetworkRequest<Item>()
    val notificationRead = getNotificationReadRequest.liveData

    fun notificationRead(notificationId: String){
        getNotificationReadRequest.call {mainRepository.notificationRead(notificationId)}
    }

    data class NotificationsRequestModel(
        val page: String?,
    )

    init {
        getNotifications(null)
    }
}
