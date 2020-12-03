package com.isuo.inspection.application.ui.main

import android.text.Editable
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isuo.inspection.application.base.ext.async
import com.isuo.inspection.application.model.bean.SubstationBean
import com.isuo.inspection.application.repository.TaskRepository
import com.isuo.inspection.application.repository.UserRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.toObservable
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val userRepository: UserRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    var toastStr: MutableLiveData<String> = MutableLiveData()
    var requestState: MutableLiveData<Int> = MutableLiveData()

    var count1Str: MutableLiveData<String> = MutableLiveData()
    var count2Str: MutableLiveData<String> = MutableLiveData()
    var count3Str: MutableLiveData<String> = MutableLiveData()

    var searchText: MutableLiveData<String> = MutableLiveData()

    var dataList = ArrayList<SubstationBean>()

    fun textChange(s: Editable) {

    }

    var disposable: Disposable? = null

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

}