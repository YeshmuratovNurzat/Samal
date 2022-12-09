package kz.fime.samal.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object MessageUtils {

    private val _messages = MutableLiveData<Event<String>>()
    val messages: LiveData<Event<String>> = _messages

    fun postMessage(message: String?){
        _messages.postValue(Event(message))
    }

}