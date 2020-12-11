package com.isuo.inspection.application.ui.main.search

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isuo.inspection.application.model.api.OkHttpManager
import com.isuo.inspection.application.model.bean.BaseEntity
import com.isuo.inspection.application.model.bean.SubstationBean
import com.isuo.inspection.application.model.bean.SubstationNetBean
import com.isuo.inspection.application.repository.TaskRepository
import com.isuo.inspection.application.utils.Event
import retrofit2.Call
import java.util.*

class SearchSubViewModel(val taskRepository: TaskRepository) : ViewModel() {

    var toastStr: MutableLiveData<String> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    var searchText: MutableLiveData<String> = MutableLiveData()

    fun textChange(s: Editable) {
        searchSub(searchText.value)
    }

    private val _showResult = MutableLiveData<Event<List<SubstationBean>>>()
    val showResult: LiveData<Event<List<SubstationBean>>> = _showResult
    private var okHttpManager2 = OkHttpManager<ArrayList<SubstationNetBean>>()

    var dataList = ArrayList<SubstationBean>()

    private fun searchSub(text: String?) {
        dataList.clear()
        isLoading.value = true
        val cell1: Call<BaseEntity<ArrayList<SubstationNetBean>>> =
            taskRepository.getSubstationList(name = text)
        okHttpManager2.requestData(cell1, {
            it?.let {
                for (bean in it) {
                    dataList.add(SubstationBean(bean.substationId, bean.substationName, bean))
                }
            }
            isLoading.value = false
            _showResult.value = Event(dataList)
        }, {
            toastStr.value = it
            isLoading.value = false
        })
    }

    override fun onCleared() {
        super.onCleared()
        okHttpManager2.destroyCall()
    }

}