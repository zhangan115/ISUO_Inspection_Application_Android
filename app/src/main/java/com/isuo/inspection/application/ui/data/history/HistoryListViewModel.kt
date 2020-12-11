package com.isuo.inspection.application.ui.data.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isuo.inspection.application.model.api.OkHttpManager
import com.isuo.inspection.application.model.bean.*
import com.isuo.inspection.application.repository.DataRepository
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

    var positionId = -1

    var deviceId: Long? = null

    private val positionList =
        listOf("前中", "前下", "左上", "左中", "左下", "右上", "右中", "右下", "后上", "后中", "后下")
    private var okHttpManager = OkHttpManager<String>()
    fun start() {
        val cell = repository.getHistoryData(
            deviceId!!, checkType
            , null, null, null
        )
        okHttpManager.requestData(cell, {

        }, {
            toastStr.value = it
        })
//        dataList.clear()
//        for (index in 0..5) {
//            val list1 = ArrayList<Type1Data>()
//            for (i in 0..8) {
//                val bean =
//                    Type1Data(i.toLong(), System.currentTimeMillis(), "0.1", "0.2", "0.3", "0.4")
//                list1.add(bean)
//            }
//            val list2 = ArrayList<Type2Data>()
//            for (i in 0..8) {
//                val bean =
//                    Type2Data(i.toLong(), System.currentTimeMillis(), "1.1", "1.2", "无明显特征")
//                list2.add(bean)
//            }
//            val list3 = ArrayList<Type3Data>()
//            val items = ArrayList<Type3ItemBean>()
//            for (i in positionList) {
//                items.add(Type3ItemBean("0.8", "0.9", i))
//            }
//            for (index in 0..3) {
//                list3.add(Type3Data(index.toLong(), System.currentTimeMillis(), items))
//            }
//            dataList.add(
//                HistoryData(
//                    index.toLong(),
//                    System.currentTimeMillis(),
//                    0,
//                    list1,
//                    list2,
//                    list3
//                )
//            )
//        }
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
        if (positionId != -1) {
            items.add(Type3ItemBean("0.8", "0.9", positionList[positionId]))
        } else {
            items.add(Type3ItemBean("0.8", "0.9", positionList[1]))
        }
        for (index in 0..10) {
            dataTypeList.add(Type3Data(index.toLong(), System.currentTimeMillis(), items))
        }
        return dataTypeList.toObservable().toList()
    }
}