package com.isuo.inspection.application.model.api

import androidx.annotation.Nullable
import com.isuo.inspection.application.app.ISUOApplication
import com.isuo.inspection.application.common.ConstantStr
import com.isuo.inspection.application.model.bean.BaseEntity
import com.isuo.inspection.application.model.bean.UserModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class OkHttpManager<T> {

    private var callList: ArrayList<Call<BaseEntity<T>>> = ArrayList()

    @Nullable
    fun requestDataWithNotLogin(
        call: Call<BaseEntity<T>>,
        successCallBack: (T?) -> Unit,
        failCallBack: (String?) -> Unit
    ) {
        callList.add(call)
        call.enqueue(object : Callback<BaseEntity<T>> {

            override fun onFailure(call: Call<BaseEntity<T>>?, t: Throwable?) {
                requestFail(failCallBack, t?.message)
            }

            override fun onResponse(
                call: Call<BaseEntity<T>>?,
                response: retrofit2.Response<BaseEntity<T>>?
            ) {
                if (response != null && response.isSuccessful) {
                    val result = response.body()
                    when (result?.errorCode) {
                        ConstantStr.SUCCESS -> requestSuccess(
                            successCallBack,
                            result.data
                        )
                        else -> requestFail(failCallBack, result?.message)
                    }
                } else {
                    requestFail(failCallBack, response?.message())
                }
            }
        })
    }

    @Nullable
    fun requestData(
        call: Call<BaseEntity<T>>,
        successCallBack: (T?) -> Unit,
        failCallBack: (String?) -> Unit
    ) {
        requestData(call, successCallBack, failCallBack, {
            failCallBack("")
        })
    }


    @Nullable
    fun requestData(
        call: Call<BaseEntity<T>>,
        successCallBack: (T?) -> Unit,
        failCallBack: (String?) -> Unit,
        netWorkError: () -> Unit
    ) {
        callList.add(call)
        call.enqueue(object : Callback<BaseEntity<T>> {

            override fun onFailure(call: Call<BaseEntity<T>>?, t: Throwable?) {
                netWorkError()
            }

            override fun onResponse(
                call: Call<BaseEntity<T>>?,
                response: retrofit2.Response<BaseEntity<T>>?
            ) {
                if (response != null && response.isSuccessful) {
                    val result = response.body()
                    when (result?.errorCode) {
                        ConstantStr.SUCCESS -> requestSuccess(
                            successCallBack,
                            result.data
                        )
                        ConstantStr.TIMEOUT -> //请求过期
                            ISUOApplication.instance.needLogin()
                        ConstantStr.ERROR_PASS -> //密码错误
                            ISUOApplication.instance.needLogin()
                        else -> requestFail(failCallBack, result?.message)
                    }
                } else {
                    netWorkError()
                }
            }
        })
    }

    fun userLogin(successCallBack: (UserModel?) -> Unit) {
        val jsonObject = JSONObject()
        val call =
            ISUOApplication.retrofit.create(UserApi::class.java)
                .userLogin(jsonObject.toString())
        call.enqueue(object : Callback<BaseEntity<UserModel>> {

            override fun onFailure(call: Call<BaseEntity<UserModel>>?, t: Throwable?) {
                ISUOApplication.instance.needLogin()
            }

            override fun onResponse(
                call: Call<BaseEntity<UserModel>>?,
                response: retrofit2.Response<BaseEntity<UserModel>>?
            ) {
                if (response != null && response.isSuccessful && response.body() != null) {
                    if (response.body()!!.errorCode == ConstantStr.SUCCESS) {
                        successCallBack(response.body()!!.data)
                    } else {
                        ISUOApplication.instance.needLogin()
                    }
                } else {
                    ISUOApplication.instance.needLogin()
                }
            }
        })
    }

    /**
     * 下载文件
     */
    fun loadFile(fileUrl: String, callBack: Callback<ResponseBody>) {
        val call = ISUOApplication.retrofit.create(UserApi::class.java).loadPdfFile(fileUrl)
        call.enqueue(callBack)
    }

    fun destroyCall() {
        for (call in callList) {
            if (!call.isCanceled) {
                call.cancel()
            }
        }
    }

    private fun requestSuccess(successCallBack: (T?) -> Unit, t: T) {
        GlobalScope.launch(Dispatchers.Main) {
            successCallBack(t)
        }
    }

    private fun requestFail(failCallBack: (String?) -> Unit, message: String?) {
        GlobalScope.launch(Dispatchers.Main) {
            failCallBack(message)
        }
    }

}