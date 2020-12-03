package com.isuo.inspection.application.ui.user.user_center

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.isuo.inspection.application.R
import com.isuo.inspection.application.app.ISUOApplication
import com.isuo.inspection.application.base.AbsBaseActivity
import com.isuo.inspection.application.base.ext.getViewModelFactory
import com.isuo.inspection.application.databinding.UserCenterDataBinding
import com.isuo.inspection.application.ui.user.forget_pass.ForgetPassActivity
import com.isuo.inspection.application.ui.user.user_info.UserInfoActivity
import com.isuo.inspection.application.utils.EventObserver

class UserCenterActivity : AbsBaseActivity<UserCenterDataBinding>() {

    private val viewModel by viewModels<UserCenterViewModel> { getViewModelFactory() }

    override fun getToolBarTitle(): String? {
        return "个人中心"
    }

    override fun initView(savedInstanceState: Bundle?) {
        viewModel.toShowUserInfo.observe(this, EventObserver {
            startActivity(Intent(this, UserInfoActivity::class.java))
        })
        viewModel.toChangePass.observe(this, EventObserver {
            startActivity(Intent(this, ForgetPassActivity::class.java))
        })
        viewModel.toSuggest.observe(this, EventObserver {

        })
        viewModel.toCheckNewVersion.observe(this, EventObserver {

        })
        viewModel.toExitApp.observe(this, EventObserver {
            ISUOApplication.instance.needLogin()
        })
    }

    override fun initData(savedInstanceState: Bundle?) {
        dataBinding.viewModel = viewModel
    }

    override fun getContentView(): Int {
        return R.layout.activity_user_center
    }

}