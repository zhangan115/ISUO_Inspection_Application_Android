package com.isuo.inspection.application.ui.main.device_list

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isuo.inspection.application.base.ext.async
import com.isuo.inspection.application.model.bean.DeviceBean
import com.isuo.inspection.application.repository.TaskRepository
import com.isuo.inspection.application.utils.Event
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.toObservable

class DeviceListViewModel(taskRepository: TaskRepository) : ViewModel() {

    var toastStr: MutableLiveData<String> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    var searchText: MutableLiveData<String> = MutableLiveData()

    private val _showResult = MutableLiveData<Event<List<DeviceBean>>>()
    val showResult: LiveData<Event<List<DeviceBean>>> = _showResult
    var disposable: Disposable? = null
    fun textChange(s: Editable) {
        isLoading.value = true
        dataList.clear()
        val searchList = ArrayList<DeviceBean>()
        for (item in dataList) {
            if (item.name.get()?.contains(searchText.value.toString())!!) {
                searchList.add(item)
            }
        }
        isLoading.value = true
        disposable = searchList.toObservable().toList().async(1000).subscribe { list ->
            isLoading.value = false
            _showResult.value = Event(list)
        }
    }

    var dataList = ArrayList<DeviceBean>()

    fun start(): Single<List<DeviceBean>> {
        dataList.clear()
        dataList.add(DeviceBean(0L, "110kV PT间隔", "运行编号：STC0002"))
        dataList.add(DeviceBean(1L, "1100进线柜", "运行编号：STC0001"))
        dataList.add(DeviceBean(2L, "2200进线柜", "运行编号：STC0009"))
        dataList.add(DeviceBean(3L, "出线柜", "运行编号：STC0011"))
        return dataList.toObservable().toList()
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }
}