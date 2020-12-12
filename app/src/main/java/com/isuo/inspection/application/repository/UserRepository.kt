package com.isuo.inspection.application.repository

import android.content.Context
import com.google.gson.Gson
import com.isuo.inspection.application.app.ISUOApplication
import com.isuo.inspection.application.common.ConstantInt
import com.isuo.inspection.application.common.ConstantStr
import com.isuo.inspection.application.model.bean.BaseEntity
import com.isuo.inspection.application.model.bean.UserModel
import com.isuo.inspection.application.model.api.UserApi
import com.isuo.inspection.application.model.bean.AppVersion
import com.sito.tool.library.utils.SPHelper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import java.io.File

class UserRepository(private val content: Context) {

    private var mUser: UserModel? = null

    /**
     * 用户登陆
     */
    fun userLogin(userName: String, userPass: String): Call<BaseEntity<UserModel>> {
        val remote = ISUOApplication.retrofit.create(UserApi::class.java)
        val jsonObject = JSONObject()
        jsonObject.put("username", userName)
        jsonObject.put("password", userPass)
        return remote.userLogin(json = jsonObject.toString())
    }

    /**
     * 用户修改密码
     */
    fun userChangePass(
        oldPass: String,
        newPass: String,
        confirmPass: String
    ): Call<BaseEntity<String>> {
        val remote = ISUOApplication.retrofit.create(UserApi::class.java)
        val jsonObject = JSONObject()
        jsonObject.put("oldPassword", oldPass)
        jsonObject.put("newPassword", newPass)
        jsonObject.put("confirmPassword", confirmPass)
        return remote.userChangePass(json = jsonObject.toString())
    }

    /**
     * 版本更新
     */
    fun appVersion(): Call<BaseEntity<AppVersion>> {
        val remote = ISUOApplication.retrofit.create(UserApi::class.java)
        val jsonObject = JSONObject()
        jsonObject.put("version", ConstantInt.VERSION)
        return remote.appVersion(json = jsonObject.toString())
    }

    /**
     * 修改用户信息
     */
    fun editUser(name: String, phone: String): Call<BaseEntity<UserModel>> {
        val remote = ISUOApplication.retrofit.create(UserApi::class.java)
        val jsonObject = JSONObject()
        val userModel = ISUOApplication.instance.userRepository.getUser()
        jsonObject.put("userId", userModel.userId)
        jsonObject.put("agentId", "1")
        jsonObject.put("username", userModel.userName)
        jsonObject.put("realName", name)
        jsonObject.put("mobile", phone)
        return remote.editUser(json = jsonObject.toString())
    }

    /**
     * 保存用户登录信息
     */
    fun setUserNameAndPass(userName: String, userPass: String?) {
        UserModel.setUserName(userName)
        UserModel.setUserPass(userPass)
    }

    /**
     * 退出账号
     */
    fun userLogout(): Call<BaseEntity<String>> {
        return ISUOApplication.retrofit.create(UserApi::class.java)
            .logout()
    }

    /**
     * 设置登录状态
     */
    fun setLoginState(isLogin: Boolean) {
        SPHelper.write(content, ConstantStr.APP_DATA, ConstantStr.USER_GUIDE, true)
        SPHelper.write(content, ConstantStr.USER_INFO, ConstantStr.NEED_LOGIN, isLogin)
    }

    /**
     * 判断是否登录
     */
    fun isLogin(): Int {
        if (!SPHelper.readBoolean(content, ConstantStr.APP_DATA, ConstantStr.USER_GUIDE, false)) {
            return 0
        } else if (!SPHelper.readBoolean(
                content,
                ConstantStr.USER_INFO,
                ConstantStr.NEED_LOGIN,
                false
            )
        ) {
            return 1
        }
        return 2
    }

    /**
     * 保存用户信息
     */
    fun saveUser(user: UserModel) {
        mUser = user
        SPHelper.write(content, ConstantStr.USER_INFO, ConstantStr.USER_DATA, Gson().toJson(user))
    }

    /**
     * 获取用户信息
     */
    fun getUser(): UserModel {
        if (mUser != null) return mUser!!
        val jsonStr = SPHelper.readString(content, ConstantStr.USER_INFO, ConstantStr.USER_DATA)
        return Gson().fromJson(jsonStr, UserModel::class.java)
    }


    /**
     * 清除用户信息
     */
    fun cleanUserInfo() {
        SPHelper.remove(content, ConstantStr.USER_INFO, ConstantStr.NEED_LOGIN)
        SPHelper.remove(content, ConstantStr.USER_INFO, ConstantStr.USER_DATA)
    }

    /**
     * 上传用户图像
     */
    fun uploadUserImage(file: File): Call<BaseEntity<UserModel>> {
        val user = getUser()
        val builder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("userId", user.userId.toString())
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        builder.addFormDataPart("file", file.name, requestFile)
        val parts = builder.build().parts
        return ISUOApplication.retrofit.create(UserApi::class.java)
            .postUserPhoto(parts)
    }

    fun getNewVersion() {

    }

    /**
     * 获取忽略的版本
     */
    fun getIgnoreVersion(): Int {
        return SPHelper.readInt(content, ConstantStr.USER_INFO, ConstantStr.USER_VERSION, 0)
    }

    /**
     * 忽略该版本更新
     */
    fun setIgnoreVersion(version: Int) {
        SPHelper.write(content, ConstantStr.USER_INFO, ConstantStr.USER_VERSION, version)
    }


}