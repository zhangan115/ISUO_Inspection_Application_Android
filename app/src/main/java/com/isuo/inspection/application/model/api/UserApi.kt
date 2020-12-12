package com.isuo.inspection.application.model.api

import com.isuo.inspection.application.model.bean.AppVersion
import com.isuo.inspection.application.model.bean.BaseEntity
import com.isuo.inspection.application.model.bean.UserModel
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface UserApi {

    /**
     * 登录
     */
    @Headers("Content-Type:application/json;charset=utf-8", "Accept:application/json;")
    @POST("user/login.json")
    fun userLogin(@Body json: String): Call<BaseEntity<UserModel>>

    /**
     * 修改用户密码（app）
     */
    @Headers("Content-Type:application/json;charset=utf-8", "Accept:application/json;")
    @POST("user/password/update.json")
    fun userChangePass(@Body json: String): Call<BaseEntity<String>>

    /**
     *修改用户
     */
    @Headers("Content-Type:application/json;charset=utf-8", "Accept:application/json;")
    @POST("user/update.json")
    fun editUser(@Body json: String): Call<BaseEntity<UserModel>>

    /**
     * 账号退出
     */
    @Headers("Content-Type:application/json;charset=utf-8", "Accept:application/json;")
    @POST("user/logout.json")
    fun logout(): Call<BaseEntity<String>>


    /**
     * 文件下载
     */
    @GET
    fun loadPdfFile(@Url fileUrl: String): Call<ResponseBody>

    /**
     * app更新
     */
    @Headers(
        "Content-Type:application/json;charset=utf-8",
        "Accept:application/json;"
    )
    @POST("system/getVersion.json")
    fun appVersion(@Body json: String): Call<BaseEntity<AppVersion>>

    /**
     * 上传用户图像
     */
    @POST("user/headpic/upload2.json")
    @Multipart
    fun postUserPhoto(
        @Part partList: List<MultipartBody.Part>
    ): Call<BaseEntity<UserModel>>

}