package com.isuo.inspection.application.ui.user.user_info

import android.text.Editable
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isuo.inspection.application.repository.UserRepository
import com.isuo.inspection.application.utils.Event
import com.isuo.inspection.application.utils.PhoneFormatCheckUtils

class UserInfoViewModel(val repository: UserRepository) : ViewModel() {

    val canClick = MutableLiveData(false)
    val isLoading = MutableLiveData(false)
    var toastStr: MutableLiveData<String> = MutableLiveData()
    var name: MutableLiveData<String> = MutableLiveData()
    var phone: MutableLiveData<String> = MutableLiveData()

    //登陆
    private val _toChangePassEvent = MutableLiveData<Event<Unit>>()
    val toChangePassEvent: LiveData<Event<Unit>> = _toChangePassEvent

    fun textChange(s: Editable) {
        canClick.value =
            !TextUtils.isEmpty(name.value) && !TextUtils.isEmpty(phone.value)
    }

    fun toChangePass() {
        if (!PhoneFormatCheckUtils.isChinaPhoneLegal(phone.value)) {
            toastStr.value = "请输入合法手机号码"
            return
        }
        isLoading.value = true
    }

    override fun onCleared() {
        super.onCleared()
    }
}