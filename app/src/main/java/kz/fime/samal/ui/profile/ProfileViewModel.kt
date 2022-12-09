package kz.fime.samal.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.fime.samal.data.entities.Profile
import kz.fime.samal.data.repositories.MainRepository
import kz.fime.samal.data.repositories.ProfileRepository
import kz.fime.samal.ui.base.BaseViewModel
import kz.fime.samal.utils.Event
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val mainRepository: MainRepository
): BaseViewModel() {

    private val profileRequest = NetworkRequest<Profile>()
    val profile = profileRequest.liveData

    private val _logOut = MutableLiveData<Event<Unit>>()
    val logOut: LiveData<Event<Unit>> = _logOut

    fun logOut(){
        _logOut.value = Event(Unit)
    }

    fun loadData(fromServer: Boolean) {
        profileRequest.call { mainRepository.getProfile(fromServer) }
    }

}