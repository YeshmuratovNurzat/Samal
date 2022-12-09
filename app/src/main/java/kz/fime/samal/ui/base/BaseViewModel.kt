package kz.fime.samal.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kz.fime.samal.data.base.State
import kz.fime.samal.utils.Event

open class BaseViewModel: ViewModel() {

    inner class NetworkRequest<T>(
        private val scope: CoroutineScope = viewModelScope,
        private val action: suspend () -> State<T>? = { null }) {

        private val _liveData = MutableLiveData<State<T>>()
        val liveData: LiveData<State<T>> = _liveData

        fun call(action: suspend () -> State<T>?) {
            scope.launch {
                _liveData.postValue(State.Loading)
                _liveData.postValue(action.invoke())
            }
        }

        fun call() {
            scope.launch {
                _liveData.postValue(State.Loading)
                _liveData.postValue(action.invoke())
            }
        }

        fun reset(){
            _liveData.value = State.Loading
        }
    }

    inner class NetworkRequestEvent<T>(
        private val scope: CoroutineScope = viewModelScope,
        private val onComplete: () -> Unit = {},
        private val action: suspend () -> State<T>? = { null }) {

        private val _liveData = MutableLiveData<Event<State<T>>>()
        val liveData: LiveData<Event<State<T>>> = _liveData

        fun call(action: suspend () -> State<T>?) {
            scope.launch {
                _liveData.postValue(Event(State.Loading))
                _liveData.postValue(Event(action.invoke()))
                onComplete.invoke()
            }
        }

        fun call() {
            scope.launch {
                _liveData.postValue(Event(State.Loading))
                _liveData.postValue(Event(action.invoke()))
                onComplete.invoke()
            }
        }

        fun reset(){
            _liveData.value = Event(State.Loading)
        }
    }
}