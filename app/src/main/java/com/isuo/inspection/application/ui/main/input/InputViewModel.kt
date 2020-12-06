package com.isuo.inspection.application.ui.main.input

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isuo.inspection.application.base.ext.async
import com.isuo.inspection.application.model.bean.InputType1
import com.isuo.inspection.application.model.bean.InputType2
import com.isuo.inspection.application.model.bean.InputType3
import com.isuo.inspection.application.repository.TaskRepository
import com.isuo.inspection.application.utils.Event
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.toObservable

class InputViewModel(var taskRepository: TaskRepository) : ViewModel() {

    var toastStr: MutableLiveData<String> = MutableLiveData()
    var canClick: MutableLiveData<Boolean> = MutableLiveData()
    var showPositionView: MutableLiveData<Boolean> = MutableLiveData(true)

    var checkType: MutableLiveData<String> = MutableLiveData()
    var checkPosition: MutableLiveData<String> = MutableLiveData()

    var inputType1: MutableLiveData<InputType1> = MutableLiveData()
    var inputType2: MutableLiveData<InputType2> = MutableLiveData()
    var inputType3: MutableLiveData<ArrayList<InputType3>> = MutableLiveData()

    private val _showLoadingDialog = MutableLiveData<Event<Boolean>>()
    val showLoadingDialog: LiveData<Event<Boolean>> = _showLoadingDialog

    var disposable: Disposable? = null

    fun submitData() {
        _showLoadingDialog.value = Event(true)
        disposable = arrayOf(false).toObservable().async(2000).subscribe {
            toastStr.value = "提交成功"
            _showLoadingDialog.value = Event(it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

}