package com.isuo.inspection.application.model.bean

import com.google.gson.Gson
import com.isuo.inspection.application.app.ISUOApplication
import com.isuo.inspection.application.common.ConstantStr

import com.sito.tool.library.utils.SPHelper

class UserModel {

    var deptName: String? = null
    var iomUserType: Int? = null
    var checkStatus: Boolean? = null
    var isLocked: Int? = null
    var registerTime: Long? = null
    var roleId: Long? = null
    var sex: Int? = null
    var mobile: String = ""
    var userName: String? = null
    var userId: Long? = null
    var realName: String = ""
    var lastModifyTime: Long? = null
    var position: String? = null
    var userType: Int? = null
    var headPic: String? = null

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

        fun isLogin(): Boolean {
            return SPHelper.readBoolean(
                ISUOApplication.instance,
                ConstantStr.USER_INFO,
                ConstantStr.NEED_LOGIN,
                false
            )
        }
    }
}




