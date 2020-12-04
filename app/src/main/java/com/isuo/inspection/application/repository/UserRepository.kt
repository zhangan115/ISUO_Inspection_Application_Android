package com.isuo.inspection.application.repository

import android.content.Context
import com.google.gson.Gson
import com.isuo.inspection.application.app.ISUOApplication
import com.isuo.inspection.application.common.ConstantStr
import com.isuo.inspection.application.model.bean.BaseEntity
import com.isuo.inspection.application.model.bean.UserModel
import com.isuo.inspection.application.model.api.UserApi
import com.sito.tool.library.utils.SPHelper
import retrofit2.Call

class UserRepository(private val content: Context) {

    private var mUser: UserModel? = null

    /**
     * 用户登陆
     */
    fun userLogin(userName: String, userPass: String): Call<BaseEntity<UserModel>> {
        val remote = ISUOApplication.retrofit.create(UserApi::class.java)
        return remote.userLogin(name = userName, pass = userPass)
    }

    /**
     * 保存用户登录信息
     */
    fun setUserNameAndPass(userName: String, userPass: String?) {
        UserModel.setUserName(userName)
        UserModel.setUserPass(userPass)
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
     * 清除用户信息
     */
    fun cleanUserInfo() {
        SPHelper.remove(content, ConstantStr.USER_INFO, ConstantStr.NEED_LOGIN)
        SPHelper.remove(content, ConstantStr.USER_INFO, ConstantStr.USER_DATA)
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

    /**
     * 获取当前用户的位置
     */
    fun getUserLocation(): String? {
        return SPHelper.readString(content, ConstantStr.USER_INFO, ConstantStr.USER_LOCATION)
    }

}