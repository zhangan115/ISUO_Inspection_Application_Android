package com.isuo.inspection.application.ui.main.search

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isuo.inspection.application.base.ext.async
import com.isuo.inspection.application.model.bean.SubstationBean
import com.isuo.inspection.application.repository.TaskRepository
import com.isuo.inspection.application.utils.Event
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.toObservable
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class SearchSubViewModel(val taskRepository: TaskRepository) : ViewModel() {

    var toastStr: MutableLiveData<String> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    var searchText: MutableLiveData<String> = MutableLiveData()

    fun textChange(s: Editable) {
        searchSub(searchText.value)
    }

    private val _showResult = MutableLiveData<Event<List<SubstationBean>>>()
    val showResult: LiveData<Event<List<SubstationBean>>> = _showResult

    var dataList = ArrayList<SubstationBean>()

    private fun searchSub(text: String?) {
        dataList.clear()
//        for (index in 0..3) {
//            dataList.add(SubstationBean(index.toLong(), "变电站$index"))
//        }
        dataList.add(SubstationBean(1L, "西安沣东110kV变电站"))
        dataList.add(SubstationBean(2L, "西安丈八110kV变电站"))
        isLoading.value = true
        disposable = dataList.toObservable().toList().async(1000).subscribe { list ->
            isLoading.value = false
            _showResult.value = Event(list)
        }
    }

    var disposable: Disposable? = null

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

}