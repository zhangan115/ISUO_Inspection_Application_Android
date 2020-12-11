package com.isuo.inspection.application.model.api

import com.isuo.inspection.application.model.bean.BaseEntity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface DataApi {
    /**
     * 获取历史数据
     */
    @Headers("Content-Type:application/json;charset=utf-8", "Accept:application/json;")
    @POST("ultrasound/page.json")
    fun getHistoryData(@Body json: String): Call<BaseEntity<String>>
}