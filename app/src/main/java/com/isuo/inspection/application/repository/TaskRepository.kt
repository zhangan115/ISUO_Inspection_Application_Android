package com.isuo.inspection.application.repository

import android.content.Context
import android.text.TextUtils
import com.google.gson.Gson
import com.isuo.inspection.application.app.ISUOApplication
import com.isuo.inspection.application.common.ConstantInt
import com.isuo.inspection.application.model.api.TaskApi
import com.isuo.inspection.application.model.bean.*
import org.json.JSONObject
import retrofit2.Call

class TaskRepository(private val content: Context) {

    fun getHomePage(): Call<BaseEntity<HomePageBean>> {
        val remote = ISUOApplication.retrofit.create(TaskApi::class.java)
        return remote.getHomePageStatistics()
    }

    fun getSubstationList(name: String?): Call<BaseEntity<ArrayList<SubstationNetBean>>> {
        val remote = ISUOApplication.retrofit.create(TaskApi::class.java)
        val jsonObject = JSONObject()
        name?.let {
            jsonObject.put("substationName", name)
        }
        return remote.getSubstationList(jsonObject.toString())
    }

    fun getEquipmentList(
        id: Long,
        lastId: Long? = null,
        name: String? = null
    ): Call<BaseEntity<List<EquipmentNetBean>>> {
        val remote = ISUOApplication.retrofit.create(TaskApi::class.java)
        val jsonObject = JSONObject()
        jsonObject.put("substationId", id)
        jsonObject.put("count", ConstantInt.PAGE_SIZE)
        lastId?.let {
            jsonObject.put("lastId", lastId)
        }
        name?.let {
            if (!TextUtils.isEmpty(name)) {
                jsonObject.put("equipmentName", name)
            }
        }
        return remote.getEquipmentList(jsonObject.toString())
    }

    fun getMeasuring(equipmentTypeCode: String, checkTypeId: Int): Call<BaseEntity<MeasuringBean>> {
        val remote = ISUOApplication.retrofit.create(TaskApi::class.java)
        val jsonObject = JSONObject()
        jsonObject.put("equipmentTypeCode", equipmentTypeCode)
        jsonObject.put("checkTypeId", checkTypeId)
        return remote.getMeasuring(jsonObject.toString())
    }

    var substationId: Long? = null
    var substationName: String? = null
    var equipmentId: Long? = null
    var equipmentName: String? = null

    fun uploadDataInfo(
        dataType: Int,
        list1: List<InputType1>? = null,
        list2: List<InputType2>? = null,
        list3: List<InputType3>? = null
    ): Call<BaseEntity<String>> {
        val remote = ISUOApplication.retrofit.create(TaskApi::class.java)
        val uploadBean = UploadBean()
        val uploadList = ArrayList<UploadDataInfo>()
        if (substationId != null && equipmentId != null) {
            if (list1 != null && list1.isNotEmpty()) {
                for (item in list1) {
                    val bean = UploadDataInfo()
                    bean.substationId = substationId
                    bean.dataType = dataType
                    bean.substationName = substationName
                    bean.equipmentId = equipmentId
                    bean.equipmentName = equipmentName
                    bean.positionId = item.positionId
                    bean.positionName = item.positionName
                    bean.peakValue = item.value1.get()
                    bean.backgroundPeakValue = item.value2.get()
                    bean.frequencyComponent1 = item.value3.get()
                    bean.frequencyComponent2 = item.value4.get()
                    uploadList.add(bean)
                }
                uploadBean.dataInfo = uploadList
            }
            if (list2 != null && list2.isNotEmpty()) {
                for (item in list2) {
                    val bean = UploadDataInfo()
                    bean.substationId = substationId
                    bean.dataType = dataType
                    bean.substationName = substationName
                    bean.equipmentId = equipmentId
                    bean.equipmentName = equipmentName
                    bean.positionId = item.positionId
                    bean.positionName = item.positionName
                    bean.peakValue = item.value1.get()
                    bean.backgroundPeakValue = item.value2.get()
                    bean.picNode = item.value3.get()
                    uploadList.add(bean)
                }
                uploadBean.dataInfo = uploadList
            }
            if (list3 != null && list3.isNotEmpty()) {
                for (item in list3) {
                    val bean = UploadDataInfo()
                    bean.substationId = substationId
                    bean.substationName = substationName
                    bean.dataType = dataType
                    bean.equipmentId = equipmentId
                    bean.equipmentName = equipmentName
                    bean.positionId = item.positionId
                    bean.positionName = item.positionName
                    bean.peakValue = item.value1.get()
                    bean.backgroundPeakValue = item.value2.get()
                    uploadList.add(bean)
                }
                uploadBean.dataInfo = uploadList
            }
        }
        return remote.uploadDataInfo(Gson().toJson(uploadBean))
    }
}