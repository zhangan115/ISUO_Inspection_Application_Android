package com.isuo.inspection.application.ui.data.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isuo.inspection.application.model.api.OkHttpManager
import com.isuo.inspection.application.model.bean.*
import com.isuo.inspection.application.repository.DataRepository
import com.isuo.inspection.application.utils.Event
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable
import kotlin.collections.ArrayList

class HistoryListViewModel(val repository: DataRepository) : ViewModel() {

    var showMeasuringView: MutableLiveData<Boolean> = MutableLiveData(true)

    var checkName: MutableLiveData<String> = MutableLiveData()

    var checkType = 0

    var toastStr: MutableLiveData<String> = MutableLiveData()

    var measuringName: MutableLiveData<String> = MutableLiveData()

    val nameList = listOf("开关柜超声波局放检测", "开关柜特高频局放检测", "开关柜暂态地电压检测")

    var dataList = ArrayList<HistoryData>()

    var showChooseDate: MutableLiveData<String> = MutableLiveData()

    var showChooseListView: MutableLiveData<Boolean> = MutableLiveData(false)

    var positionId: Long? = null

    var startTime: String? = null
    var endTime: String? = null

    var deviceId: Long? = null

    private var lastId: Long? = null

    private val _showResult = MutableLiveData<Event<ArrayList<HistoryData>?>>()
    val showResult: LiveData<Event<ArrayList<HistoryData>?>> = _showResult

    private val _showMoreResult = MutableLiveData<Event<ArrayList<HistoryData>?>>()
    val showMoreResult: LiveData<Event<ArrayList<HistoryData>?>> = _showMoreResult

    private var okHttpManager = OkHttpManager<ArrayList<HistoryNetData>>()

    private fun getData(it: List<HistoryNetData>?): ArrayList<HistoryData> {
        val dataList = ArrayList<HistoryData>()
        if (it == null || it.isEmpty()) {
            return dataList
        }
        val historySize: Int = it.size - 1
        if (it[historySize].dataList != null && it[historySize].dataList!!.isNotEmpty()) {
            val dataListSize = it[historySize].dataList!!.size -1
            lastId = it[historySize].dataList?.get(dataListSize)?.ultrasounId
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

    fun start() {
        val cell = repository.getHistoryData(
            deviceId!!, null, checkType, positionId, startTime, endTime
        )
        okHttpManager.requestData(cell, {
            _showResult.value = Event(getData(it))
        }, {
            toastStr.value = it
            _showResult.value = Event(getData(null))
        })
    }

    fun getMoreNormalData() {
        val cell = repository.getHistoryData(
            deviceId!!, lastId, checkType, positionId, startTime, endTime
        )
        okHttpManager.requestData(cell, {
            _showMoreResult.value = Event(getData(it))
        }, {
            toastStr.value = it
            _showMoreResult.value = Event(getData(null))
        })
    }

    private val _showChooseResult = MutableLiveData<Event<ArrayList<HistoryData>?>>()
    val showChooseResult: LiveData<Event<ArrayList<HistoryData>?>> = _showChooseResult

    private val _showMoreChooseResult = MutableLiveData<Event<ArrayList<HistoryData>?>>()
    val showMoreChooseResult: LiveData<Event<ArrayList<HistoryData>?>> = _showMoreChooseResult


    fun getChooseData() {
        val cell = repository.getHistoryData(
            deviceId!!, null, checkType, positionId, startTime, endTime
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

    fun getMoreChooseData() {
        val cell = repository.getHistoryData(
            deviceId!!, lastId, checkType, positionId, startTime, endTime
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
            _showMoreChooseResult.value = Event(dataList)
        }, {
            toastStr.value = it
            _showMoreChooseResult.value = Event(getData(null))
        })
    }

}