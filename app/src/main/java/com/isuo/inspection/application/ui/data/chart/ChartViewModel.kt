package com.isuo.inspection.application.ui.data.chart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isuo.inspection.application.model.bean.*
import com.isuo.inspection.application.repository.TaskRepository
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable

class ChartViewModel(val taskRepository: TaskRepository) : ViewModel() {

    var showPositionView: MutableLiveData<Boolean> = MutableLiveData(true)

    var checkType: MutableLiveData<String> = MutableLiveData()

    var checkPosition: MutableLiveData<String> = MutableLiveData()

    var requestState: MutableLiveData<Int> = MutableLiveData()

    val nameList = listOf("开关柜超声波局放检测", "开关柜特高频局放检测", "开关柜暂态地电压检测")

    var dataList = ArrayList<HistoryData>()

    var showChooseDate: MutableLiveData<String> = MutableLiveData()

    var showChartView: MutableLiveData<Boolean> = MutableLiveData(false)

    var positionText: MutableLiveData<String> = MutableLiveData("")

    var positionId = -1L

    private val positionList =
        listOf("前中", "前下", "左上", "左中", "左下", "右上", "右中", "右下", "后上", "后中", "后下")


    fun start(type: Int): Single<List<HistoryData>> {
        dataList.clear()
        val list1 = ArrayList<Type1Data>()
        for (i in 0..11) {

            val bean =
                Type1Data(
                    i.toLong(),
                    System.currentTimeMillis(),
                    (2 * Math.random().toFloat() + 1).toString(),
                    (2 * Math.random().toFloat() + 3).toString(),
                    (2 * Math.random().toFloat() + 5).toString(),
                    (2 * Math.random().toFloat() + 7).toString()
                )
            list1.add(bean)
        }
        val list2 = ArrayList<Type2Data>()
        for (i in 0..11) {
            val bean =
                Type2Data(
                    i.toLong(), System.currentTimeMillis(),
                    (2 * Math.random().toFloat() + 1).toString(),
                    (2 * Math.random().toFloat() + 3).toString(),
                    (2 * Math.random().toFloat() + 5).toString()
                )
            list2.add(bean)
        }
        if (positionId != -1L) {
            positionText.value = positionList[positionId.toInt()]
        } else {
            positionText.value = ""
        }
        val list3 = ArrayList<Type3Data>()
        for (index in 0..11) {
            val items = ArrayList<Type3ItemBean>()
            items.add(
                Type3ItemBean(
                    (2 * Math.random().toFloat() + 1).toString(),
                    (2 * Math.random().toFloat() + 3).toString(),
                    positionText.value!!
                )
            )
//            list3.add(Type3Data(index.toLong(), System.currentTimeMillis(), items))
        }
        dataList.add(
            HistoryData(
                type.toLong(),
                System.currentTimeMillis(),
                0,
                list1,
                list2,
                list3
            )
        )
        return dataList.toObservable().toList()
    }
}