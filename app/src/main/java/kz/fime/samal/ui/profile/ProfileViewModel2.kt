package kz.fime.samal.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.fime.samal.api.ApiResponse
import kz.fime.samal.data.models.*
import kz.fime.samal.data.models.custom.Resource
import kz.fime.samal.data.models.order_detail.ClientAddress
import kz.fime.samal.data.repositories.ProfileRepository
import kz.fime.samal.ui.base.BaseViewModel
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class ChangePhoneBody(
    val phone: String,
    val code: String
)

@HiltViewModel
class ProfileViewModel2 @Inject constructor(
    private val profileRepository: ProfileRepository
): BaseViewModel() {

    private val _loadProfile = MutableLiveData<Unit>()
    val resultProfile: LiveData<Resource<ProfileResponse>> = Transformations
        .switchMap(_loadProfile) {
            profileRepository.getProfile3()
        }

    private val _logoutUser = MutableLiveData<Unit>()
    val resultLogout: LiveData<Resource<BaseResponse>> = Transformations
        .switchMap(_logoutUser) {
            profileRepository.logoutUser()
        }

    private val _changePhoneRequest = MutableLiveData<String>()
    val resultChangePhoneRequest: LiveData<Resource<BaseResponse>> = Transformations
        .switchMap(_changePhoneRequest) {
            profileRepository.changePhoneRequest(it)
        }

    private val _changePhone = MutableLiveData<ChangePhoneBody>()
    val resultChangePhone: LiveData<Resource<BaseResponse>> = Transformations
        .switchMap(_changePhone) {
            profileRepository.changePhone(it.phone, it.code)
        }

    private val _changeName = MutableLiveData<String>()
    val resultChangeName: LiveData<Resource<BaseResponse>> = Transformations
        .switchMap(_changeName) {
            profileRepository.changeName(it)
        }

    private val _changeEmail = MutableLiveData<String>()
    val resultChangeEmail: LiveData<Resource<BaseResponse>> = Transformations
        .switchMap(_changeEmail) {
            profileRepository.changeEmail(it)
        }

    private val _loadCards = MutableLiveData<Unit>()
    val resultLoadCards: LiveData<Resource<Response<CardsResponse>>> = Transformations
        .switchMap(_loadCards) {
            profileRepository.getCards()
        }

    private val _loadAddress = MutableLiveData<Unit>()
    val resultLoadAddress: LiveData<Resource<Response<AddressResponse>>> = Transformations
        .switchMap(_loadAddress) {
            profileRepository.getAddress()
        }

    private val _addCard = MutableLiveData<Unit>()
    val resultAddCard: LiveData<Resource<AddCardResponse>> = Transformations
        .switchMap(_addCard) {
            profileRepository.addCard()
        }

    private val _deleteCard = MutableLiveData<String>()
    val resultDeleteCard: LiveData<Resource<CardsResponse>> = Transformations
        .switchMap(_deleteCard) {
            profileRepository.deleteCard(it)
        }

    private val _loadFaq = MutableLiveData<Unit>()
    val resultLoadFaq: LiveData<Resource<FaqResponse>> = Transformations
        .switchMap(_loadFaq) {
            profileRepository.loadFaq()
        }

    private val _loadAbout = MutableLiveData<Unit>()
    val resultLoadAbout: LiveData<Resource<ApiResponse<AboutResponse>>> = Transformations
        .switchMap(_loadAbout) {
            profileRepository.loadAbout()
        }

    private val _changePhoto = MutableLiveData<MultipartBody.Part>()
    val resultChangePhoto: LiveData<Resource<BaseResponse>> = Transformations
        .switchMap(_changePhoto) {
            profileRepository.changeImage(it)
        }

    private val phone = MutableLiveData<String>()
    private val newPhone = MutableLiveData<String>()

    fun loadProfile() {
        _loadProfile.postValue(Unit)
    }

    fun loadCards() {
        _loadCards.postValue(Unit)
    }

    fun loadAddress() {
        _loadAddress.postValue(Unit)
    }

    fun addCard() {
        _addCard.postValue(Unit)
    }

    fun deleteCard(cardId : String){
        _deleteCard.postValue(cardId)
    }

    fun logoutUser() {
        _logoutUser.postValue(Unit)
    }

    fun changePhoneRequest(phone: String) {
        _changePhoneRequest.postValue(phone)
    }

    fun changePhone(phone: String, code: String) {
        _changePhone.postValue(ChangePhoneBody(phone, code))
    }

    fun changeEmail(email: String) {
        _changeEmail.postValue(email)
    }

    fun changeName(name: String) {
        _changeName.postValue(name)
    }

    fun getPhoneNumber(): LiveData<String> {
        return phone
    }

    fun loadFaq() {
        _loadFaq.postValue(Unit)
    }

    fun loadAbout() {
        _loadAbout.postValue(Unit)
    }

    fun setPhoneNumber(phone: String) {
        this.phone.value = phone
    }

    fun setNewPhoneNumber(phone: String) {
        newPhone.value = phone
    }

    fun getNewPhoneNumber(): LiveData<String> {
        return newPhone
    }

    fun changeImage(photo: MultipartBody.Part) {
        _changePhoto.postValue(photo)
    }

}