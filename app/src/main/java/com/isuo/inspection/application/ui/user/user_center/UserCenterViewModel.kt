package com.isuo.inspection.application.ui.user.user_center

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isuo.inspection.application.repository.UserRepository
import com.isuo.inspection.application.utils.Event

class UserCenterViewModel(val repository: UserRepository) : ViewModel() {

    var toastStr: MutableLiveData<String> = MutableLiveData()

    var userName: MutableLiveData<String> = MutableLiveData("张三")
    var userPhone: MutableLiveData<String> = MutableLiveData("18509259203")
    var userImageUrl: MutableLiveData<String> = MutableLiveData()

    private val _toShowUserInfo = MutableLiveData<Event<Unit>>()
    val toShowUserInfo: LiveData<Event<Unit>> = _toShowUserInfo

    fun toShowUserInfo() {
        _toShowUserInfo.value = Event(Unit)
    }

    private val _toChangePass = MutableLiveData<Event<Unit>>()
    val toChangePass: LiveData<Event<Unit>> = _toChangePass

    fun toChangePass() {
        _toChangePass.value = Event(Unit)
    }

    private val _toCheckNewVersion = MutableLiveData<Event<Unit>>()
    val toCheckNewVersion: LiveData<Event<Unit>> = _toCheckNewVersion

    fun toCheckNewVersion() {
        _toCheckNewVersion.value = Event(Unit)
    }

    private val _toSuggest = MutableLiveData<Event<Unit>>()
    val toSuggest: LiveData<Event<Unit>> = _toSuggest


    fun toSuggest() {
        _toSuggest.value = Event(Unit)
    }

    private val _toExitApp = MutableLiveData<Event<Unit>>()
    val toExitApp: LiveData<Event<Unit>> = _toExitApp

    fun toExitApp() {
        _toExitApp.value = Event(Unit)
    }

    private val _toShowUserPhoto = MutableLiveData<Event<Unit>>()
    val toShowUserPhoto: LiveData<Event<Unit>> = _toShowUserPhoto

    fun toShowUserPhoto() {
        _toShowUserPhoto.value = Event(Unit)
    }


    override fun onCleared() {
        super.onCleared()
    }
}