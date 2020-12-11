package com.isuo.inspection.application.model.bean

import android.text.Editable
import android.text.TextUtils
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData

class SubstationBean(id: Long, name: String, val subBean: SubstationNetBean?) {
    val id: ObservableField<Long> = ObservableField(id)
    val name: ObservableField<String> = ObservableField(name)
}

class SubCheckItemBean(id: Long, name: String) {
    val id: ObservableField<Long> = ObservableField(id)
    val name: ObservableField<String> = ObservableField(name)
}

class DeviceBean(id: Long, name: String, code: String, equipmentTypeCode: Int) {
    val id: ObservableField<Long> = ObservableField(id)
    val name: ObservableField<String> = ObservableField(name)
    val code: ObservableField<String> = ObservableField(code)
    val equipmentTypeCode: String = equipmentTypeCode.toString()
}

class InputType1 {

    val value1: ObservableField<String> = ObservableField()
    val value2: ObservableField<String> = ObservableField()
    val value3: ObservableField<String> = ObservableField()
    val value4: ObservableField<String> = ObservableField()
    val isFinish: MutableLiveData<Boolean> = MutableLiveData()

    var positionId: Long? = null
    var positionName: String? = null

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

    var positionId: Long? = null
    var positionName: String? = null

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

    var positionId: Long? = null
    var positionName: String? = null

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

data class HistoryData(
    val id: Long,
    val time: Long,
    val type: Int,
    val type1DataList: ArrayList<Type1Data>?,
    val type2DataList: ArrayList<Type2Data>?,
    val type3DataList: ArrayList<Type3Data>?
)

data class Type1Data(
    val id: Long,
    val time: Long,
    val value1: String?,
    val value2: String?,
    val value3: String?,
    val value4: String?
)

data class Type2Data(
    val id: Long,
    val time: Long,
    val value1: String?,
    val value2: String?,
    val value3: String?
)

data class Type3Data(
    val id: Long,
    val time: Long,
    val value1: String?,
    val value2: String?,
    val positionText: String?
)

data class Type3ItemBean(
    val value1: String,
    val value2: String,
    val positionText: String
)
