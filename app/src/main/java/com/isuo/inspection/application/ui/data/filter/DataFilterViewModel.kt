package com.isuo.inspection.application.ui.data.filter

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isuo.inspection.application.model.api.OkHttpManager
import com.isuo.inspection.application.model.bean.MeasuringBean
import com.isuo.inspection.application.model.bean.MeasuringPointBean
import com.isuo.inspection.application.repository.TaskRepository
import com.isuo.inspection.application.utils.Event

class DataFilterViewModel(val taskRepository: TaskRepository) : ViewModel() {

    var position: MutableLiveData<Long> = MutableLiveData(-1L)
    var checkType: MutableLiveData<Int> = MutableLiveData()

    var equipmentTypeCode: String? = null
    var measuringName: String? = null

    var toastStr: MutableLiveData<String> = MutableLiveData()

    var startDate: MutableLiveData<String> = MutableLiveData()
    var endDate: MutableLiveData<String> = MutableLiveData()

    private val _showDataEvent = MutableLiveData<Event<ArrayList<MeasuringPointBean>>>()
    val showDataEvent: LiveData<Event<ArrayList<MeasuringPointBean>>> = _showDataEvent

    private var okHttpManager = OkHttpManager<MeasuringBean>()
    fun start() {
        if (!TextUtils.isEmpty(equipmentTypeCode) && checkType.value != null) {
            val cell = taskRepository.getMeasuring(equipmentTypeCode!!, checkType.value!! + 1)
            val list = ArrayList<MeasuringPointBean>()
            okHttpManager.requestData(cell, {
                it?.let {
                    for (item in it.list) {
                        list.add(
                            MeasuringPointBean(
                                item.measuringPointId,
                                item.measuringPointName,
                                -1
                            )
                        )
                    }
                }
                _showDataEvent.value = Event(list)
            }, {
                toastStr.value = it
            })
        }
    }

}