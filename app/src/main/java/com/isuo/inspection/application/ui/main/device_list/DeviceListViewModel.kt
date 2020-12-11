package com.isuo.inspection.application.ui.main.device_list

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isuo.inspection.application.common.ConstantInt
import com.isuo.inspection.application.model.api.OkHttpManager
import com.isuo.inspection.application.model.bean.DeviceBean
import com.isuo.inspection.application.model.bean.EquipmentNetBean
import com.isuo.inspection.application.repository.TaskRepository
import com.isuo.inspection.application.utils.Event

class DeviceListViewModel(var taskRepository: TaskRepository) : ViewModel() {

    var toastStr: MutableLiveData<String> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    var searchText: MutableLiveData<String> = MutableLiveData()
    var requestState: MutableLiveData<Int> = MutableLiveData()
    var subId: Long = -1

    private val _showResult = MutableLiveData<Event<List<DeviceBean>>>()
    val showResult: LiveData<Event<List<DeviceBean>>> = _showResult

    private val _showMoreResult = MutableLiveData<Event<List<DeviceBean>>>()
    val showMoreResult: LiveData<Event<List<DeviceBean>>> = _showMoreResult

    fun textChange(s: Editable) {
        isLoading.value = true
        start()
    }

    var dataList = ArrayList<DeviceBean>()
    private var okHttpManager = OkHttpManager<List<EquipmentNetBean>>()

    fun start() {
        dataList.clear()
        val cell = taskRepository.getEquipmentList(this.subId, null, searchText.value)
        okHttpManager.requestData(cell, {
            isLoading.value = false
            if (it == null || it.isEmpty()) {
                requestState.value = ConstantInt.REQUEST_STATE_EMPTY
            } else {
                requestState.value = ConstantInt.REQUEST_STATE_DATA
                for (item in it) {
                    var serialNumber = ""
                    item.serialNumber?.let { it1 ->
                        serialNumber = it1
                    }
                    dataList.add(
                        DeviceBean(
                            item.equipmentId,
                            item.equipmentName,
                            "运行编号：$serialNumber",
                            item.equipmentTypeCode
                        )
                    )
                }
                _showResult.value = Event(dataList)
            }
        }, {
            isLoading.value = false
            toastStr.value = it
            requestState.value = ConstantInt.REQUEST_STATE_ERROR
            _showResult.value = Event(dataList)
        })
    }

    fun loadMore(lastId: Long?) {
        dataList.clear()
        val cell = taskRepository.getEquipmentList(this.subId, lastId, searchText.value)
        okHttpManager.requestData(cell, {
            if (it != null && it.isNotEmpty()) {
                requestState.value = ConstantInt.REQUEST_STATE_DATA
                for (item in it) {
                    var serialNumber = ""
                    item.serialNumber?.let { it1 ->
                        serialNumber = it1
                    }
                    dataList.add(
                        DeviceBean(
                            item.equipmentId,
                            item.equipmentName,
                            "运行编号：$serialNumber",
                            item.equipmentTypeCode
                        )
                    )
                }
            }
            _showMoreResult.value = Event(dataList)
        }, {
            toastStr.value = it
            _showMoreResult.value = Event(dataList)
        })
    }

    override fun onCleared() {
        super.onCleared()
        okHttpManager.destroyCall()
    }
}