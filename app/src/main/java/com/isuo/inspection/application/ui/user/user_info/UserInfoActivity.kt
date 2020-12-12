package com.isuo.inspection.application.ui.user.user_info

import android.os.Bundle
import androidx.activity.viewModels
import com.isuo.inspection.application.R
import com.isuo.inspection.application.app.ISUOApplication
import com.isuo.inspection.application.base.AbsBaseActivity
import com.isuo.inspection.application.base.ext.getViewModelFactory
import com.isuo.inspection.application.databinding.UserInfoDataBinding
import com.isuo.inspection.application.ui.user.forget_pass.ForgetPassViewModel
import com.isuo.inspection.application.utils.EventObserver

class UserInfoActivity : AbsBaseActivity<UserInfoDataBinding>() {

    private val viewModel by viewModels<UserInfoViewModel> { getViewModelFactory() }

    override fun getToolBarTitle(): String? {
        return "个人信息"
    }

    override fun initView(savedInstanceState: Bundle?) {
        val user = ISUOApplication.instance.userRepository.getUser()
        viewModel.name.value = user.realName
        viewModel.phone.value = user.mobile
        viewModel.toChangePassEvent.observe(this, EventObserver {
            setResult(RESULT_OK)
            finish()
        })
    }

    override fun initData(savedInstanceState: Bundle?) {
        dataBinding.viewModel = viewModel
    }

    override fun getContentView(): Int {
        return R.layout.activity_user_info
    }

}