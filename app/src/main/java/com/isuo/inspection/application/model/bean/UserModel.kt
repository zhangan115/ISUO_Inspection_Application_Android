package com.isuo.inspection.application.model.bean

import com.google.gson.Gson
import com.isuo.inspection.application.app.ISUOApplication
import com.isuo.inspection.application.common.ConstantStr

import com.sito.tool.library.utils.SPHelper

class UserModel {

    var userId: Long? = null
    var userRole: String? = null
    var username: String? = null
    var portraitUrl: String? = null
    var realName: String? = null
    var userAddress: String? = null
    var userMobile: String? = null
    var workYear: String? = null
    var hasPassword: Boolean = false

    companion object {

        fun getUserName(): String {
            return SPHelper.readString(
                ISUOApplication.instance,
                ConstantStr.USER,
                ConstantStr.USER_NAME
            )
        }

        fun setUserName(userName: String) {
            SPHelper.write(
                ISUOApplication.instance,
                ConstantStr.USER,
                ConstantStr.USER_NAME,
                userName
            )
        }

        fun getUserPass(): String? {
            return SPHelper.readString(
                ISUOApplication.instance,
                ConstantStr.USER,
                ConstantStr.USER_PASS
            )
        }

        fun setUserPass(userPass: String?) {
            userPass?.let {
                SPHelper.write(
                    ISUOApplication.instance,
                    ConstantStr.USER,
                    ConstantStr.USER_PASS,
                    it
                )
            }
        }

        fun setCurrentUser(user: UserModel) {
            SPHelper.write(
                ISUOApplication.instance,
                ConstantStr.USER,
                ConstantStr.USER_DATA,
                Gson().toJson(user)
            )
        }

        fun isLogin():Boolean{
            return SPHelper.readBoolean(
                ISUOApplication.instance,
                ConstantStr.USER_INFO,
                ConstantStr.NEED_LOGIN,
                false
            )
        }
    }
}




