package com.isuo.inspection.application.model.api

import com.isuo.inspection.application.model.bean.AppVersion
import com.isuo.inspection.application.model.bean.BaseEntity
import com.isuo.inspection.application.model.bean.UserModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface UserApi {

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("/login")
    fun userLogin(
        @Field("username") name: String,
        @Field("password") pass: String
    ): Call<BaseEntity<UserModel>>

    /**
     * 验证码登录
     */
    @FormUrlEncoded
    @POST("/login_by_code")
    fun userLoginByCode(
        @Field("mobile") name: String,
        @Field("code") pass: String
    ): Call<BaseEntity<UserModel>>

    /**
     * 修改用户密码（app）
     */
    @Headers("Content-Type:application/json;charset=utf-8", "Accept:application/json;")
    @POST("user/edit/password")
    fun userChangePass(@Body json: String): Call<BaseEntity<String>>

    /**
     * 获取手机验证码
     */
    @Headers("Content-Type:application/json;charset=utf-8", "Accept:application/json;")
    @POST("user/send/vcode")
    fun getPhoneCode(@Body json: String): Call<BaseEntity<String>>

    /**
     *用户详情
     */
    @Headers("Content-Type:application/json;charset=utf-8", "Accept:application/json;")
    @POST("/user/get")
    fun getUserDetail(@Body json: String): Call<BaseEntity<UserModel>>

    /**
     *修改用户
     */
    @Headers("Content-Type:application/json;charset=utf-8", "Accept:application/json;")
    @POST("/user/edit")
    fun editUser(@Body json: String): Call<BaseEntity<UserModel>>

    /**
     * 账号退出
     */
    @Headers("Content-Type:application/json;charset=utf-8", "Accept:application/json;")
    @POST("/logout")
    fun logout(): Call<BaseEntity<String>>


    /**
     * 文件下载
     */
    @GET
    fun loadPdfFile(@Url fileUrl: String): Call<ResponseBody>

    /**
     * 绑定客户cid 推送
     * @param cid
     * @return
     */
    @Headers(
        "Content-Type:application/json;charset=utf-8",
        "Accept:application/json;"
    )
    @POST("/user/bind_cid")
    fun postCid(@Body json: String): Call<BaseEntity<String>>

    /**
     * app更新
     */
    @Headers(
        "Content-Type:application/json;charset=utf-8",
        "Accept:application/json;"
    )
    @POST("/update/appVersion")
    fun appVersion(@Body json: String): Call<BaseEntity<AppVersion>>

}