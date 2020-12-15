package com.isuo.inspection.application.ui.data.chart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isuo.inspection.application.model.api.OkHttpManager
import com.isuo.inspection.application.model.bean.*
import com.isuo.inspection.application.repository.DataRepository
import com.isuo.inspection.application.utils.Event
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable

class ChartViewModel(val repository: DataRepository) : ViewModel() {

    var toastStr: MutableLiveData<String> = MutableLiveData()
    var showPositionView: MutableLiveData<Boolean> = MutableLiveData(true)

    var checkTypeName: MutableLiveData<String> = MutableLiveData()

    var checkPosition: MutableLiveData<String> = MutableLiveData()

    var requestState: MutableLiveData<Int> = MutableLiveData()

    val nameList = listOf("开关柜超声波局放检测", "开关柜特高频局放检测", "开关柜暂态地电压检测")

    var dataList = ArrayList<HistoryData>()

    var showChooseDate: MutableLiveData<String> = MutableLiveData()

    var showChartView: MutableLiveData<Boolean> = MutableLiveData(false)

    var positionText: MutableLiveData<String> = MutableLiveData("")

    var positionId :Long? = null
    var checkType = 0
    var startTime: String? = null
    var endTime: String? = null
    var deviceId: Long? = null

    private fun getData(it: List<HistoryNetData>?): ArrayList<HistoryData> {
        val dataList = ArrayList<HistoryData>()
        if (it == null || it.isEmpty()) {
            return dataList
        }
        when (checkType - 1) {
            0 -> {
                for ((index, data) in it.withIndex()) {
                    val list = ArrayList<Type1Data>()
                    if (data.dataList != null) {
                        for (item in data.dataList!!) {
                            val bean = Type1Data(
                                item.ultrasounId,
                                item.createTime,
                                item.peakValue,
                                item.backgroundPeakValue,
                                item.frequencyComponent1,
                                item.frequencyComponent2
                            )
                            list.add(bean)
                        }
                    }
                    dataList.add(
                        HistoryData(
                            index.toLong(),
                            System.currentTimeMillis(),
                            0,
                            list,
                            null,
                            null
                        )
                    )
                }
            }
            1 -> {
                for ((index, data) in it.withIndex()) {
                    val list = ArrayList<Type2Data>()
                    if (data.dataList != null) {
                        for (item in data.dataList!!) {
                            val bean = Type2Data(
                                item.ultrasounId,
                                item.createTime,
                                item.peakValue,
                                item.backgroundPeakValue,
                                item.picNode
                            )
                            list.add(bean)
                        }
                    }
                    dataList.add(
                        HistoryData(
                            index.toLong(),
                            System.currentTimeMillis(),
                            1,
                            null,
                            list,
                            null
                        )
                    )
                }
            }
            else -> {
                for ((index, data) in it.withIndex()) {
                    val list = ArrayList<Type3Data>()
                    if (data.dataList != null) {
                        val timeList = ArrayList<Long>()
                        for (item in data.dataList!!) {
                            var canAdd = true
                            for (time in timeList) {
                                if (time == item.uploadDate) {
                                    canAdd = false
                                    break
                                }
                            }
                            if (canAdd) {
                                timeList.add(item.uploadDate)
                            }
                        }
                        for (time in timeList) {
                            val type3ItemList = ArrayList<Type3ItemBean>()
                            for (item in data.dataList!!) {
                                if (item.uploadDate == time) {
                                    type3ItemList.add(
                                        Type3ItemBean(
                                            item.peakValue,
                                            item.backgroundPeakValue,
                                            item.positionName
                                        )
                                    )
                                }
                            }
                            val bean = Type3Data(
                                0,
                                time,
                                type3ItemList
                            )
                            list.add(bean)
                        }
                    }
                    dataList.add(
                        HistoryData(
                            index.toLong(),
                            System.currentTimeMillis(),
                            2,
                            null,
                            null,
                            list
                        )
                    )
                }
            }
        }
        return dataList
    }

    private val _showChooseResult = MutableLiveData<Event<ArrayList<HistoryData>?>>()
    val showChooseResult: LiveData<Event<ArrayList<HistoryData>?>> = _showChooseResult

    private var okHttpManager = OkHttpManager<ArrayList<HistoryNetData>>()

    fun start() {
        val cell = repository.getHistoryData(
            deviceId!!,null, checkType, positionId, startTime, endTime
        )
        okHttpManager.requestData(cell, { it ->
            val data = HistoryData(
                0L,
                System.currentTimeMillis(),
                checkType,
                ArrayList(),
                ArrayList(),
                ArrayList()
            )
            for (item in getData(it)) {
                item.type1DataList?.let {
                    data.type1DataList?.addAll(it)
                }
                item.type2DataList?.let {
                    data.type2DataList?.addAll(it)
                }
                item.type3DataList?.let {
                    data.type3DataList?.addAll(it)
                }
            }
            dataList.clear()
            dataList.add(data)
            _showChooseResult.value = Event(dataList)
        }, {
            toastStr.value = it
            _showChooseResult.value = Event(getData(null))
        })
    }
}