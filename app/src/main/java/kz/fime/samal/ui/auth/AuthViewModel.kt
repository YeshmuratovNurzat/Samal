package kz.fime.samal.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.fime.samal.data.repositories.AuthRepository
import kz.fime.samal.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {


    private val checkAccountRequest = NetworkRequestEvent<HashMap<String, Any>>()
    val isAccountChecked = checkAccountRequest.liveData

    private val getOTPRequest = NetworkRequestEvent<HashMap<String, Any>>()
    val getOTP = getOTPRequest.liveData

    private val checkOTPRequest = NetworkRequestEvent<HashMap<String, Any>>()
    val checkOTP = checkOTPRequest.liveData

    private val registerUserRequest = NetworkRequestEvent<HashMap<String, Any>>()
    val registerUser = registerUserRequest.liveData

    private val loginUserRequest = NetworkRequestEvent<HashMap<String, Any>>()
    val loginUser = loginUserRequest.liveData

    private val resetPasswordRequest = NetworkRequestEvent<HashMap<String, Any>>()
    val resetPassword = resetPasswordRequest.liveData


    private val phone = MutableLiveData<String>()
    private val username = MutableLiveData<String>()
    private val otp = MutableLiveData<String>()
    private val isForgotPasword = MutableLiveData<Boolean>()

    fun isForgotPassword(isForgot: Boolean) {
        isForgotPasword.value = isForgot
    }

    fun loginUser(phone: String, password: String) {
        loginUserRequest.call {
            authRepository.loginUser("7$phone", password)
        }
    }

    fun registerUser(phone: String, name: String, code: String, password: String) {
        registerUserRequest.call {
            authRepository.registerUser("7$phone", code, name, password)
        }
    }

    fun resetPassword(phone: String, code: String, password: String) {
        resetPasswordRequest.call {
            authRepository.resetPassword("7$phone", code, password)
        }
    }

    fun checkAccount(phoneNumber: String) {
        checkAccountRequest.call {
            authRepository.checkAccount("7$phoneNumber")
        }
        phone.value = phoneNumber
    }

    fun getOTP(phoneNumber: String) {
        getOTPRequest.call {
            authRepository.getOTP("7$phoneNumber")
        }
    }

    fun checkOTP(phoneNumber: String, otp: String) {
        checkOTPRequest.call {
            authRepository.checkOTP("7$phoneNumber", otp)
        }
    }

    fun saveOTP(code: String) {
        otp.value = code
    }

    fun setUserName(name: String) {
        username.value = name
    }

    fun getOTP(): LiveData<String> {
        return otp
    }

    fun getUserName(): LiveData<String> {
        return username
    }

    fun getPhoneNumber(): LiveData<String> {
        return phone
    }

    fun isForgot() = isForgotPasword

    fun isEnteredValidPhone(phoneNumber: String): Boolean {
        return phoneNumber.length == 10
    }

}