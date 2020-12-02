package com.isuo.inspection.application.ui.login.forget_pass

import android.text.Editable
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isuo.inspection.application.repository.UserRepository
import com.isuo.inspection.application.utils.Event

class ForgetPassViewModel(val repository: UserRepository) : ViewModel() {

    val canClick = MutableLiveData(false)
    val isLoading = MutableLiveData(false)
    var toastStr: MutableLiveData<String> = MutableLiveData()
    var oldPass: MutableLiveData<String> = MutableLiveData()
    var newPass: MutableLiveData<String> = MutableLiveData()
    var newPass1: MutableLiveData<String> = MutableLiveData()

    //登陆
    private val _toChangePassEvent = MutableLiveData<Event<Unit>>()
    val toChangePassEvent: LiveData<Event<Unit>> = _toChangePassEvent

    fun textChange(s: Editable) {
        canClick.value =
            !TextUtils.isEmpty(oldPass.value) && !TextUtils.isEmpty(newPass.value) && !TextUtils.isEmpty(
                newPass1.value
            )
    }

    fun toChangePass() {
        isLoading.value = true
    }

    override fun onCleared() {
        super.onCleared()
    }
}