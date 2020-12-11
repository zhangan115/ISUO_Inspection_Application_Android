package com.isuo.inspection.application.ui.main.check_item

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isuo.inspection.application.base.ext.async
import com.isuo.inspection.application.model.bean.SubCheckItemBean
import com.isuo.inspection.application.repository.TaskRepository
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable
import java.util.*

class SubCheckItemViewMode(var taskRepository: TaskRepository) : ViewModel() {

    var toastStr: MutableLiveData<String> = MutableLiveData()
    var requestState: MutableLiveData<Int> = MutableLiveData()

    var dataList = ArrayList<SubCheckItemBean>()

    fun start(): Single<List<SubCheckItemBean>> {
        dataList.clear()
        dataList.add(SubCheckItemBean(0L, "开关柜超声波局放检测"))
        dataList.add(SubCheckItemBean(1L, "开关柜特高频局放检测"))
        dataList.add(SubCheckItemBean(2L, "开关柜暂态地电压检测"))

        return dataList.toObservable().toList().async()
    }

    fun setData(substationId: Long, substationName: String) {
        taskRepository.substationId = substationId
        taskRepository.substationName = substationName
    }

}