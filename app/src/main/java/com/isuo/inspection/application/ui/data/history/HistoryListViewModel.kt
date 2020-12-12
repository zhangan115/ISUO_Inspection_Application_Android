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

    private val positionList =
        listOf("前中", "前下", "左上", "左中", "左下", "右上", "右中", "右下", "后上", "后中", "后下")

    private val _showResult = MutableLiveData<Event<ArrayList<HistoryData>>>()
    val showResult: LiveData<Event<ArrayList<HistoryData>>> = _showResult

    private val _showMoreResult = MutableLiveData<Event<ArrayList<HistoryData>>>()
    val showMoreResult: LiveData<Event<ArrayList<HistoryData>>> = _showMoreResult

    private var okHttpManager = OkHttpManager<ArrayList<HistoryNetData>>()

    fun start() {
        val cell = repository.getHistoryData(
            deviceId!!, checkType, positionId, startTime, endTime
        )
        okHttpManager.requestData(cell, {
            if (it != null && it.isNotEmpty()) {
                dataList.clear()
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
                                        item.frequencyComponent2,
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
                                        item.picNode,
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
                                val type3ItemList = ArrayList<Type3ItemBean>()
                                for (time in timeList) {
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
                _showResult.value = Event(dataList)
            }
        }, {
            toastStr.value = it
        })
    }

    fun getType1Data(): Single<List<Type1Data>> {
        val dataTypeList = ArrayList<Type1Data>()
        for (i in 0..8) {
            val bean =
                Type1Data(i.toLong(), System.currentTimeMillis(), "0.1", "0.2", "0.3", "0.4")
            dataTypeList.add(bean)
        }
        return dataTypeList.toObservable().toList()
    }

    fun getType2Data(): Single<List<Type2Data>> {
        val dataTypeList = ArrayList<Type2Data>()
        for (i in 0..8) {
            val bean =
                Type2Data(i.toLong(), System.currentTimeMillis(), "1.1", "1.2", "1.3")
            dataTypeList.add(bean)
        }
        return dataTypeList.toObservable().toList()
    }

    fun getType3Data(): Single<List<Type3Data>> {
        val dataTypeList = ArrayList<Type3Data>()
        val items = ArrayList<Type3ItemBean>()
        if (positionId != -1L) {
            items.add(Type3ItemBean("0.8", "0.9", positionList[positionId!!.toInt()]))
        } else {
            items.add(Type3ItemBean("0.8", "0.9", positionList[1]))
        }
        for (index in 0..10) {
//            dataTypeList.add(Type3Data(index.toLong(), System.currentTimeMillis(), items))
        }
        return dataTypeList.toObservable().toList()
    }
}