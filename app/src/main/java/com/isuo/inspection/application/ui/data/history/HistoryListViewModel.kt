package com.isuo.inspection.application.ui.data.history

import androidx.lifecycle.ViewModel
import com.isuo.inspection.application.model.bean.*
import com.isuo.inspection.application.repository.TaskRepository
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable
import java.util.*

class HistoryListViewModel(val taskRepository: TaskRepository) : ViewModel() {

    var dataList = ArrayList<HistoryData>()
    private val positionList =
        listOf("前中", "前下", "左上", "左中", "左下", "右上", "右中", "右下", "后上", "后中", "后下")

    fun start(): Single<List<HistoryData>> {
        dataList.clear()
        for (index in 0..5) {
            val list1 = ArrayList<Type1Data>()
            for (i in 0..8) {
                val bean =
                    Type1Data(i.toLong(), System.currentTimeMillis(), "0.1", "0.2", "0.3", "0.4")
                list1.add(bean)
            }
            val list2 = ArrayList<Type2Data>()
            for (i in 0..8) {
                val bean =
                    Type2Data(i.toLong(), System.currentTimeMillis(), "1.1", "1.2", "1.3")
                list2.add(bean)
            }
            val list3 = ArrayList<Type3Data>()
            val items = ArrayList<Type3ItemBean>()
            for (i in positionList) {
                items.add(Type3ItemBean("0.8", "0.9", i))
            }
            for (index in 0..3) {
                list3.add(Type3Data(index.toLong(), System.currentTimeMillis(), items))
            }

            dataList.add(
                HistoryData(
                    index.toLong(),
                    System.currentTimeMillis(),
                    0,
                    list1,
                    list2,
                    list3
                )
            )
        }
        return dataList.toObservable().toList()
    }
}