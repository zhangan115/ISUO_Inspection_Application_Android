package com.isuo.inspection.application.ui.login

import android.text.Editable
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isuo.inspection.application.base.ext.async
import com.isuo.inspection.application.model.api.OkHttpManager
import com.isuo.inspection.application.model.bean.BaseEntity
import com.isuo.inspection.application.model.bean.UserModel
import com.isuo.inspection.application.repository.UserRepository
import com.isuo.inspection.application.utils.Event
import com.isuo.inspection.application.utils.PhoneFormatCheckUtils
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.toObservable
import kotlinx.coroutines.Job
import retrofit2.Call
import java.util.concurrent.TimeUnit

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    var name: MutableLiveData<String> = MutableLiveData()
    var pass: MutableLiveData<String> = MutableLiveData()
    val isLoading = MutableLiveData<Boolean>()
    val canClick = MutableLiveData(false)
    var toastStr: MutableLiveData<String> = MutableLiveData()

    //登陆
    private val _toLoginEvent = MutableLiveData<Event<Unit>>()
    val toLoginEvent: LiveData<Event<Unit>> = _toLoginEvent

    fun textChange(s: Editable) {
        canClick.value = !TextUtils.isEmpty(name.value) && !TextUtils.isEmpty(pass.value)
    }

    private var okHttpManager = OkHttpManager<UserModel>()
    var disposable: Disposable? = null
    fun toLogin() {
        if (TextUtils.isEmpty(name.value)) {
            toastStr.value = "请输入手机号码"
            return
        }
        if (TextUtils.isEmpty(pass.value)) {
            toastStr.value = "请输入密码"
            return
        }
        val name = name.value!!
        val pass = pass.value!!
        if (!PhoneFormatCheckUtils.isPhoneLegal(name)) {
            toastStr.value = "请输入合法的手机号码"
            return
        }
        isLoading.value = true
        disposable = arrayOf(0).toObservable()
            .delay(2, TimeUnit.SECONDS).single(0)
            .async().subscribe { it ->
                isLoading.value = false
                userRepository.setLoginState(true)
                _toLoginEvent.value = Event(Unit)
            }
//        val cell: Call<BaseEntity<UserModel>>?
//        cell = userRepository.userLogin(name, pass)
//
//        okHttpManager.requestDataWithNotLogin(cell, {
//            isLoading.value = false
//            if (it != null) {
//                userRepository.saveUser(it)
//                userRepository.setLoginState(true)
//                _toLoginEvent.value = Event(Unit)
//            }
//        }, {
//            isLoading.value = false
//            toastStr.value = it
//        })
    }

    //登陆
    private val _toShowAgreeEvent = MutableLiveData<Event<Unit>>()
    val toShowAgreeEvent: LiveData<Event<Unit>> = _toShowAgreeEvent

    fun showAgreeWeb() {
        _toShowAgreeEvent.value = Event(Unit)
    }

    private val _toForgetPassEvent = MutableLiveData<Event<Unit>>()
    val toForgetPassEvent: LiveData<Event<Unit>> = _toForgetPassEvent

    fun forgetPass() {
        _toForgetPassEvent.value = Event(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        okHttpManager.destroyCall()
        disposable?.dispose()
    }

}
