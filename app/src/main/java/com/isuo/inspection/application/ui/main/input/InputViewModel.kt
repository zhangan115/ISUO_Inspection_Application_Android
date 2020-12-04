package com.isuo.inspection.application.ui.main.input

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isuo.inspection.application.model.bean.InputType1
import com.isuo.inspection.application.model.bean.InputType2
import com.isuo.inspection.application.model.bean.InputType3
import com.isuo.inspection.application.repository.TaskRepository

class InputViewModel(var taskRepository: TaskRepository) : ViewModel() {

    var toastStr: MutableLiveData<String> = MutableLiveData()
    var canClick: MutableLiveData<Boolean> = MutableLiveData()

    var checkType: MutableLiveData<String> = MutableLiveData()
    var checkPosition: MutableLiveData<String> = MutableLiveData()

    var inputType1: MutableLiveData<InputType1> = MutableLiveData()
    var inputType2: MutableLiveData<InputType2> = MutableLiveData()
    var inputType3: MutableLiveData<ArrayList<InputType3>> = MutableLiveData()

    fun submitData() {

    }

}