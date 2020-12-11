package com.isuo.inspection.application.model.api

import com.isuo.inspection.application.model.bean.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface TaskApi {

    /**
     *首页统计
     */
    @Headers("Content-Type:application/json;charset=utf-8", "Accept:application/json;")
    @POST("statistics/homepage.json")
    fun getHomePageStatistics(): Call<BaseEntity<HomePageBean>>

    /**
     *变电站列表
     */
    @Headers("Content-Type:application/json;charset=utf-8", "Accept:application/json;")
    @POST("substation/list.json")
    fun getSubstationList(@Body json: String): Call<BaseEntity<ArrayList<SubstationNetBean>>>


    /**
     * 设备列表
     */
    @Headers("Content-Type:application/json;charset=utf-8", "Accept:application/json;")
    @POST("equipment/page/list.json")
    fun getEquipmentList(@Body json: String): Call<BaseEntity<List<EquipmentNetBean>>>

    /**
     * 获取测点
     */
    @Headers("Content-Type:application/json;charset=utf-8", "Accept:application/json;")
    @POST("measuring/page.json")
    fun getMeasuring(@Body json: String): Call<BaseEntity<MeasuringBean>>

    /**
     * 上传数据
     */
    @Headers("Content-Type:application/json;charset=utf-8", "Accept:application/json;")
    @POST("ultrasound/upload/dataInfo.json")
    fun uploadDataInfo(@Body json: String): Call<BaseEntity<String>>

}