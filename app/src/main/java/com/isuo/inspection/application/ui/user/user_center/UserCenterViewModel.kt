package com.isuo.inspection.application.ui.user.user_center

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isuo.inspection.application.app.ISUOApplication
import com.isuo.inspection.application.common.ConstantInt
import com.isuo.inspection.application.common.ConstantStr
import com.isuo.inspection.application.model.api.OkHttpManager
import com.isuo.inspection.application.model.bean.AppVersion
import com.isuo.inspection.application.model.bean.UserModel
import com.isuo.inspection.application.repository.UserRepository
import com.isuo.inspection.application.utils.Event
import java.io.File

class UserCenterViewModel(val repository: UserRepository) : ViewModel() {

    var toastStr: MutableLiveData<String> = MutableLiveData()

    var currentVersion: MutableLiveData<String> = MutableLiveData(ConstantStr.CURRENT_VERSION)

    var userName: MutableLiveData<String> =
        MutableLiveData(ISUOApplication.instance.userRepository.getUser().realName)
    var userPhone: MutableLiveData<String> =
        MutableLiveData(ISUOApplication.instance.userRepository.getUser().mobile)
    var userImageUrl: MutableLiveData<String> = MutableLiveData()

    private val _toShowUserInfo = MutableLiveData<Event<Unit>>()
    val toShowUserInfo: LiveData<Event<Unit>> = _toShowUserInfo

    fun toShowUserInfo() {
        _toShowUserInfo.value = Event(Unit)
    }

    private var okHttpManager = OkHttpManager<UserModel>()

    fun toUploadUserPhoto(file: File) {
        okHttpManager.requestData(repository.uploadUserImage(file), {
            it?.let {
                toastStr.value = "修改成功"
                repository.saveUser(it)
                userImageUrl.value = it.headPic
            }
        }, {
            toastStr.value = it
        })
    }

    private val _toChangePass = MutableLiveData<Event<Unit>>()
    val toChangePass: LiveData<Event<Unit>> = _toChangePass

    fun toChangePass() {
        _toChangePass.value = Event(Unit)
    }

    private val _toCheckNewVersion = MutableLiveData<Event<AppVersion>>()
    val toCheckNewEvenVersion: LiveData<Event<AppVersion>> = _toCheckNewVersion
    private var okHttpManagerVersion = OkHttpManager<AppVersion>()

    fun toCheckNewVersion() {
        val cell = repository.appVersion()
        okHttpManagerVersion.requestData(cell, {
            it?.let {
                if (it.version > ConstantInt.VERSION) {
                    _toCheckNewVersion.value = Event(it)
                } else {
                    toastStr.value = "当前为最新版本"
                }
            }
        }, {
            toastStr.value = it
        })

    }

    private val _toSuggest = MutableLiveData<Event<Unit>>()
    val toSuggest: LiveData<Event<Unit>> = _toSuggest


    fun toSuggest() {
        _toSuggest.value = Event(Unit)
    }

    private val _toExitEvent = MutableLiveData<Event<Unit>>()
    val toExitEvent: LiveData<Event<Unit>> = _toExitEvent
    private val logoutManager = OkHttpManager<String>()

    fun userExit() {
        logoutManager.requestData(repository.userLogout(), {
            repository.cleanUserInfo()
            _toExitEvent.value = Event(Unit)
        }, {
            repository.cleanUserInfo()
            _toExitEvent.value = Event(Unit)
        })
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

    /**
     * 忽略本次版本更新
     */
    fun ignoreVersion(app: AppVersion) {
        repository.setIgnoreVersion(app.version)
    }

    override fun onCleared() {
        super.onCleared()
        okHttpManagerVersion.destroyCall()
        okHttpManager.destroyCall()
    }
}