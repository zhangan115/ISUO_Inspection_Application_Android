package com.isuo.inspection.application.model.bean

import android.text.Editable
import android.text.TextUtils
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger

class SubstationBean(id: Long, name: String) {

    val id: ObservableField<Long> = ObservableField(id)
    val name: ObservableField<String> = ObservableField(name)
}

class SubCheckItemBean(id: Long, name: String) {
    val id: ObservableField<Long> = ObservableField(id)
    val name: ObservableField<String> = ObservableField(name)
}

class DeviceBean(id: Long, name: String, code: String) {
    val id: ObservableField<Long> = ObservableField(id)
    val name: ObservableField<String> = ObservableField(name)
    val code: ObservableField<String> = ObservableField(code)
}

class InputType1 {

    val value1: ObservableField<String> = ObservableField()
    val value2: ObservableField<String> = ObservableField()
    val value3: ObservableField<String> = ObservableField()
    val value4: ObservableField<String> = ObservableField()
    val isFinish: MutableLiveData<Boolean> = MutableLiveData()

    fun textChangeListener(s: Editable) {
        val text1 = value1.get()
        val text2 = value2.get()
        val text3 = value3.get()
        val text4 = value4.get()
        isFinish.value = !TextUtils.isEmpty(text1) && !TextUtils.isEmpty(text2)
                && !TextUtils.isEmpty(text3) && !TextUtils.isEmpty(text4)
    }

}

class InputType2() {

    val value1: ObservableField<String> = ObservableField()
    val value2: ObservableField<String> = ObservableField()
    val value3: ObservableField<String> = ObservableField()
    val chooseId: ObservableField<Int> = ObservableField()
    val isFinish: MutableLiveData<Boolean> = MutableLiveData()

    fun textChangeListener(s: Editable) {
        val text1 = value1.get()
        val text2 = value2.get()
        val text3 = value3.get()
        isFinish.value =
            !TextUtils.isEmpty(text1) && !TextUtils.isEmpty(text2) && !TextUtils.isEmpty(text3)
    }
}

class InputType3() {

    val positionText: ObservableField<String> = ObservableField()
    val value1: ObservableField<String> = ObservableField()
    val value2: ObservableField<String> = ObservableField()

    val isFinish: MutableLiveData<Type3StateBean> = MutableLiveData()

    fun textChangeListener(s: Editable) {
        val text1 = value1.get()
        val text2 = value2.get()
        val bean = isFinish.value
        val isFinish = !TextUtils.isEmpty(text1) && !TextUtils.isEmpty(text2)
        if (bean != null && bean.isFinish.value != isFinish) {
            bean.isFinish.value = isFinish
            this.isFinish.postValue(bean)
        }
    }
}

class Type3StateBean {
    val chooseId: ObservableField<Int> = ObservableField()
    val isFinish: MutableLiveData<Boolean> = MutableLiveData()
}