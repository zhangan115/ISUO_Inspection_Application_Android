package com.isuo.inspection.application.repository

import android.content.Context
import com.isuo.inspection.application.app.ISUOApplication
import com.isuo.inspection.application.model.api.DataApi
import com.isuo.inspection.application.model.bean.BaseEntity
import com.isuo.inspection.application.model.bean.HistoryNetData
import com.isuo.inspection.application.utils.DataUtil
import org.json.JSONObject
import retrofit2.Call

class DataRepository(private val content: Context) {

    fun getHistoryData(
        equipmentId: Long,
        lastId: Long?,
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
        lastId?.let {
            jsonObject.put("lastId", it)
        }
        startTime?.let {
            val time = DataUtil.date2TimeStamp(it, "yyyy-MM-dd")
            jsonObject.put("startTime", time)
        }
        endTime?.let {
            val time = DataUtil.date2TimeStamp(it, "yyyy-MM-dd")
            val timeStr = DataUtil.timeFormat(time, "yyyy-MM-dd 23:59:59")
            val time1 = DataUtil.date2TimeStamp(timeStr, "yyyy-MM-dd HH:mm:ss")
            jsonObject.put("endTime", time1)
        }

        return remote.getHistoryData(jsonObject.toString())
    }


}