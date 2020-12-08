package com.isuo.inspection.application.ui.user.suggest

import android.text.Editable
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isuo.inspection.application.repository.UserRepository
import com.isuo.inspection.application.utils.Event

class SuggestViewModel(userRepository: UserRepository) : ViewModel() {

    var toastStr: MutableLiveData<String> = MutableLiveData()

    var contextText: MutableLiveData<String> = MutableLiveData()

    var textCount: MutableLiveData<String> = MutableLiveData("256")

    var canClick: MutableLiveData<Boolean> = MutableLiveData()

    private val _toSub = MutableLiveData<Event<Unit>>()
    val toSub: LiveData<Event<Unit>> = _toSub

    fun toSub() {
        toastStr.value = "提交成功"

        _toSub.value = Event(Unit)
    }

    fun textChange(s: Editable) {
        canClick.value =
            !TextUtils.isEmpty(contextText.value)
        if (contextText.value == null) {
            textCount.value = (256).toString()
        } else {
            textCount.value = (256 - contextText.value!!.length).toString()
        }

    }

}