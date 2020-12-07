package com.isuo.inspection.application.ui.data.filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataFilterViewModel() : ViewModel() {

    var position: MutableLiveData<Int> = MutableLiveData(-1)
    var checkType: MutableLiveData<Int> = MutableLiveData()

    var startDate: MutableLiveData<String> = MutableLiveData()
    var endDate: MutableLiveData<String> = MutableLiveData()

}