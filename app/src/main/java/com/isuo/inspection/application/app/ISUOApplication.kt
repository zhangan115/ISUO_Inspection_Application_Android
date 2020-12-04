package com.isuo.inspection.application.app

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Build
import android.os.StrictMode
import android.text.TextUtils
import android.util.Log
import androidx.annotation.NonNull
import com.isuo.inspection.application.BuildConfig
import com.isuo.inspection.application.R
import com.isuo.inspection.application.common.ConstantStr
import com.isuo.inspection.application.repository.DataRepository
import com.isuo.inspection.application.repository.TaskRepository
import com.isuo.inspection.application.repository.UserRepository
import com.isuo.inspection.application.ui.login.LoginActivity
import com.qw.soul.permission.SoulPermission
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.sito.tool.library.utils.SPHelper
import io.objectbox.BoxStore
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

open class ISUOApplication : Application() {

    private val tag = "ChargingApplication"

    val userRepository: UserRepository
        get() = UserRepository(this)
    val taskRepository: TaskRepository
        get() = TaskRepository(this)
    val dataRepository: DataRepository
        get() = DataRepository(this)

    companion object {
        lateinit var instance: ISUOApplication
            private set
        lateinit var boxStore: BoxStore
        private lateinit var builder: OkHttpClient.Builder
        lateinit var retrofit: Retrofit
        private var cookie: Cookie? = null

        fun appHost(): String {
            return SPHelper.readString(
                instance,
                ConstantStr.USER_INFO,
                ConstantStr.APP_HOST,
                BuildConfig.HOST
            )
        }

        init {
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.setPrimaryColorsId(
                    R.color.colorWhite,
                    R.color.colorAccent
                )//全局设置主题颜色
                ClassicsHeader(context)
                //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));
                // 指定为经典Header，默认是 贝塞尔雷达Header
            }
            //设置全局的Footer构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
                //指定为经典Footer，默认是 BallPulseFooter
                ClassicsFooter(context).setDrawableSize(20f)
            }
            initRetrofit()
        }

        private fun initDataBase() {
//            boxStore = MyObjectBox.builder().androidContext(instance.applicationContext).build()
        }

        private fun initRetrofit() {
            builder = OkHttpClient().newBuilder()
            builder.connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .cookieJar(object : CookieJar {

                    override fun loadForRequest(url: HttpUrl): List<Cookie> {
                        val cookies = ArrayList<Cookie>()
                        if (cookie == null) {
                            val doMin = SPHelper.readString(
                                instance.applicationContext,
                                ConstantStr.USER_INFO,
                                ConstantStr.COOKIE_DOMAIN
                            )
                            val name = SPHelper.readString(
                                instance.applicationContext,
                                ConstantStr.USER_INFO,
                                ConstantStr.COOKIE_NAME
                            )
                            val value = SPHelper.readString(
                                instance.applicationContext,
                                ConstantStr.USER_INFO,
                                ConstantStr.COOKIE_VALUE
                            )
                            if (!TextUtils.isEmpty(doMin) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(
                                    value
                                )
                            ) {
                                cookie =
                                    Cookie.Builder().domain(doMin).name(name).value(value).build()
                            }
                        }
                        if (cookie != null) {
                            cookies.add(
                                Cookie.Builder().domain(cookie!!.domain).name(
                                    cookie!!.name
                                ).value(
                                    cookie!!.value
                                ).build()
                            )
                        }
                        return cookies
                    }

                    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                        if (cookies.isNotEmpty()) {
                            cookie = cookies[0]
                            SPHelper.write(
                                instance.applicationContext,
                                ConstantStr.USER_INFO,
                                ConstantStr.COOKIE_DOMAIN,
                                cookie?.domain
                            )
                            SPHelper.write(
                                instance.applicationContext,
                                ConstantStr.USER_INFO,
                                ConstantStr.COOKIE_NAME,
                                cookie?.name
                            )
                            SPHelper.write(
                                instance.applicationContext,
                                ConstantStr.USER_INFO,
                                ConstantStr.COOKIE_VALUE,
                                cookie?.value
                            )
                        }
                    }
                })
            if (BuildConfig.DEBUG) {
                builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            }
            val okHttpClient = builder.build()
            retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.HOST)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        activityList = ArrayList()
        initDataBase()
        SoulPermission.init(this)
        Log.d(tag, "onCreate")
        //初始化推送
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
        }
    }

    private var activityList: ArrayList<Activity>? = null

    /**
     * 打开一个页面
     *
     * @param activity activity
     */
    fun openActivity(@NonNull activity: Activity?) {
        activity?.let { activityList?.add(0, it) } //最新activity的加入第一个位置
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity? {
        return activityList?.get(0)
    }

    /**
     * 关闭一个页面
     *
     * @param activity activity
     */
    fun closeActivity(@NonNull activity: Activity?) {
        activity?.let { activityList?.remove(it) }
    }

    /**
     * 退出App
     */
    private fun exitApp() {
        if (activityList!!.size > 0) {
            for (activity in activityList!!) {
                activity.finish()
            }
        }
    }

    var isLoginOpen = false

    /**
     * 需要登录
     */
    fun needLogin() {
        SPHelper.remove(this, ConstantStr.USER_INFO, ConstantStr.NEED_LOGIN)
        SPHelper.remove(this, ConstantStr.USER_INFO, ConstantStr.USER_DATA)
        SPHelper.write(this, ConstantStr.USER_INFO, ConstantStr.NEED_LOGIN, false)
        if (!isLoginOpen) {
            exitApp()
            if (activityList!!.size > 0) {
                val intent = Intent(this, LoginActivity::class.java)
                if (currentActivity() != null) {
                    currentActivity()!!.startActivity(intent)
                } else {
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }
    }

    fun imageCacheFile(): String {
        return externalCacheDir!!.absolutePath
    }

}