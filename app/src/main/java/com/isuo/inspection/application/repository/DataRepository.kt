package com.isuo.inspection.application.repository

import android.content.Context
import com.isuo.inspection.application.app.ISUOApplication
import com.isuo.inspection.application.model.api.DataApi
import com.isuo.inspection.application.model.bean.BaseEntity
import com.isuo.inspection.application.model.bean.HistoryNetData
import org.json.JSONObject
import retrofit2.Call

class DataRepository(private val content: Context) {

    fun getHistoryData(
        equipmentId: Long,
        dataType: Int,
        positionId: Long?,
        startTime: String? = null,
        endTime: String? = null
    ): Call<BaseEntity<ArrayList<HistoryNetData>>> {
        val remote = ISUOApplication.retrofit.create(DataApi::class.java)
        val jsonObject = JSONObject()
        jsonObject.put("dataType", dataType)
        jsonObject.put("equipmentId", equipmentId)
        positionId?.let {
            jsonObject.put("positionId", it)
        }
//        startTime?.let {
//            jsonObject.put("startTime", startTime)
//        }
//        endTime?.let {
//            jsonObject.put("endTime", endTime)
//        }
        return remote.getHistoryData(jsonObject.toString())
    }


}