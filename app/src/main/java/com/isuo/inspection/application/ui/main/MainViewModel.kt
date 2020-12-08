package com.isuo.inspection.application.ui.main

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isuo.inspection.application.model.bean.SubstationBean
import com.isuo.inspection.application.repository.TaskRepository
import com.isuo.inspection.application.repository.UserRepository
import com.isuo.inspection.application.utils.Event
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.toObservable
import java.util.*

class MainViewModel(
    private val userRepository: UserRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    var toastStr: MutableLiveData<String> = MutableLiveData()
    var requestState: MutableLiveData<Int> = MutableLiveData()

    var count1Str: MutableLiveData<String> = MutableLiveData("2")
    var count2Str: MutableLiveData<String> = MutableLiveData("8")
    var count3Str: MutableLiveData<String> = MutableLiveData("1120")

    var searchText: MutableLiveData<String> = MutableLiveData()

    var dataList = ArrayList<SubstationBean>()

    fun textChange(s: Editable) {

    }

    private val _showUserCenter = MutableLiveData<Event<Unit>>()
    val showUserCenter: LiveData<Event<Unit>> = _showUserCenter

    fun showUserCenter() {
        _showUserCenter.value = Event(Unit)
    }

    private val _showSearchSub = MutableLiveData<Event<Unit>>()
    val showSearchSub: LiveData<Event<Unit>> = _showSearchSub

    fun showSearchSub() {
        _showSearchSub.value = Event(Unit)
    }

    var disposable: Disposable? = null

    fun start(): Single<List<SubstationBean>> {
        dataList.clear()
//        for (index in 0..10) {
//            dataList.add(SubstationBean(index.toLong(), "变电站$index"))
//        }
        dataList.add(SubstationBean(1L, "西安沣东110kV变电站"))
        dataList.add(SubstationBean(2L, "西安丈八110kV变电站"))
        return dataList.toObservable().toList()
    }

    fun loadMore(): Single<List<SubstationBean>> {
        val oldSize = dataList.size
//        for (index in 0..10) {
//            dataList.add(SubstationBean((oldSize + index).toLong(), "变电站" + (oldSize + index)))
//        }
        return dataList.toObservable().toList()
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

}