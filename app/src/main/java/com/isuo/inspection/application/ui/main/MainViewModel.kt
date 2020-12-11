package com.isuo.inspection.application.ui.main

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isuo.inspection.application.common.ConstantInt
import com.isuo.inspection.application.model.api.OkHttpManager
import com.isuo.inspection.application.model.bean.BaseEntity
import com.isuo.inspection.application.model.bean.HomePageBean
import com.isuo.inspection.application.model.bean.SubstationBean
import com.isuo.inspection.application.repository.TaskRepository
import com.isuo.inspection.application.utils.Event
import retrofit2.Call

class MainViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    var toastStr: MutableLiveData<String> = MutableLiveData()
    var requestState: MutableLiveData<Int> = MutableLiveData()

    var count1Str: MutableLiveData<String> = MutableLiveData("-")
    var count2Str: MutableLiveData<String> = MutableLiveData("-")
    var count3Str: MutableLiveData<String> = MutableLiveData("-")

    var dataList = ArrayList<SubstationBean>()

    fun textChange(s: Editable) {

    }

    private val _showUserCenter = MutableLiveData<Event<Unit>>()
    val showUserCenter: LiveData<Event<Unit>> = _showUserCenter

    fun showUserCenter() {
        _showUserCenter.value = Event(Unit)
    }

    private val _showSearchSub = MutableLiveData<Event<Unit>>()
    val showSearchSub: LiveData<Event<Unit>> = _showSearchSub

    fun showSearchSub() {
        _showSearchSub.value = Event(Unit)
    }

    private var okHttpManager1 = OkHttpManager<HomePageBean>()

    private val _showSubList = MutableLiveData<Event<List<SubstationBean>?>>()
    val showSubList: LiveData<Event<List<SubstationBean>?>> = _showSubList

    fun start() {
        dataList.clear()
        val cell1: Call<BaseEntity<HomePageBean>> = taskRepository.getHomePage()
        okHttpManager1.requestData(cell1, {
            requestState.value = ConstantInt.REQUEST_STATE_DATA
            if (it != null) {
                count1Str.value = it.substationCount.toString()
                count2Str.value = it.equipmentCount.toString()
                count3Str.value = it.dataCount.toString()
                for (bean in it.substationList) {
                    dataList.add(SubstationBean(bean.substationId, bean.substationName, bean))
                }
            }
            _showSubList.value = Event(dataList)
        }, {
            requestState.value = ConstantInt.REQUEST_STATE_DATA
            toastStr.value = it
            _showSubList.value = Event(dataList)
        }, {
            requestState.value = ConstantInt.REQUEST_STATE_ERROR
            _showSubList.value = Event(dataList)
        })
    }

    override fun onCleared() {
        super.onCleared()
        okHttpManager1.destroyCall()
    }

}