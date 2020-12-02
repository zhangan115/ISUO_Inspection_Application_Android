package com.isuo.inspection.application.ui.login.forget_pass

import android.os.Bundle
import androidx.activity.viewModels
import com.isuo.inspection.application.R
import com.isuo.inspection.application.base.AbsBaseActivity
import com.isuo.inspection.application.base.ext.getViewModelFactory
import com.isuo.inspection.application.databinding.ForgetPassDataBinding
import com.isuo.inspection.application.utils.EventObserver

class ForgetPassActivity : AbsBaseActivity<ForgetPassDataBinding>() {

    private val viewModel by viewModels<ForgetPassViewModel> { getViewModelFactory() }

    override fun getToolBarTitle(): String? {
        return "密码修改"
    }

    override fun initView(savedInstanceState: Bundle?) {
        viewModel.toChangePassEvent.observe(this, EventObserver {
            finish()
        })
    }

    override fun initData(savedInstanceState: Bundle?) {
        dataBinding.viewModel = viewModel
    }

    override fun getContentView(): Int {
        return R.layout.activity_forget_pass
    }


}