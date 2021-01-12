package com.isuo.inspection.application.ui.main.input

import android.text.Editable
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isuo.inspection.application.model.api.OkHttpManager
import com.isuo.inspection.application.model.bean.*
import com.isuo.inspection.application.repository.TaskRepository
import com.isuo.inspection.application.utils.Event

class InputViewModel(var taskRepository: TaskRepository) : ViewModel() {

    var toastStr: MutableLiveData<String> = MutableLiveData()
    var canClick: MutableLiveData<Boolean> = MutableLiveData()
    var showPositionView: MutableLiveData<Boolean> = MutableLiveData(true)

    var checkType: MutableLiveData<String> = MutableLiveData()
    var checkTypeId: Int? = null
    var equipmentTypeCode: String? = null
    var checkPosition: MutableLiveData<String> = MutableLiveData()

    private val _showLoadingDialog = MutableLiveData<Event<Boolean>>()
    val showLoadingDialog: LiveData<Event<Boolean>> = _showLoadingDialog

    private val _uploadSuccess = MutableLiveData<Event<Unit>>()
    val uploadSuccess: LiveData<Event<Unit>> = _uploadSuccess

    var dataList1 = ArrayList<InputType1>()
    var dataList2 = ArrayList<InputType2>()
    var dataList3 = ArrayList<InputType3>()

    var inputType3Value: MutableLiveData<String> = MutableLiveData()


    private var okHttpManagerUpload = OkHttpManager<String>()

    fun submitData() {
        _showLoadingDialog.value = Event(true)
        val cell = taskRepository.uploadDataInfo(checkTypeId!!, dataList1, dataList2, dataList3)
        okHttpManagerUpload.requestData(cell, {
            _showLoadingDialog.value = Event(false)
            _uploadSuccess.value = Event(Unit)
        }, {
            _showLoadingDialog.value = Event(false)
            toastStr.value = it
        })
    }

    fun setData(subId: Long, subName: String?, equipmentId: Long, equipmentName: String) {
        taskRepository.substationId = subId
        taskRepository.substationName = subName
        taskRepository.equipmentId = equipmentId
        taskRepository.equipmentName = equipmentName
    }

    private var okHttpManager = OkHttpManager<MeasuringBean>()
    var measuringList = ArrayList<MeasuringListBean>()

    private val _showInputViews = MutableLiveData<Event<Unit>>()
    val showInputViews: LiveData<Event<Unit>> = _showInputViews

    private val _inputType3ValueChange = MutableLiveData<Event<Unit>>()
    val inputType3ValueChange: LiveData<Event<Unit>> = _inputType3ValueChange

    fun textChange(s: Editable) {
        _inputType3ValueChange.value = Event(Unit)
    }

    fun start() {
        measuringList.clear()
        if (!TextUtils.isEmpty(equipmentTypeCode) && checkTypeId != null) {
            val cell = taskRepository.getMeasuring(equipmentTypeCode!!, checkTypeId!!)
            okHttpManager.requestData(cell, {
                it?.let {
                    if (checkTypeId!! < 3) {
                        if (it.list.isNotEmpty()) {
                            checkPosition.value = it.list[0].measuringPointName
                        }
                    }
                    measuringList.addAll(it.list)
                }
                _showInputViews.value = Event(Unit)
            }, {
                toastStr.value = it
            })
        }

    }

    override fun onCleared() {
        super.onCleared()
        okHttpManager.destroyCall()
        okHttpManagerUpload.destroyCall()
    }

}