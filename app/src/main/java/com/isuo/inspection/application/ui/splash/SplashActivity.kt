package com.isuo.inspection.application.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.isuo.inspection.application.R
import com.isuo.inspection.application.base.AbsBaseActivity
import com.isuo.inspection.application.base.ext.bindLifeCycle
import com.isuo.inspection.application.base.ext.getViewModelFactory
import com.isuo.inspection.application.databinding.SplashDataBinding
import com.isuo.inspection.application.ui.login.LoginActivity
import com.isuo.inspection.application.ui.main.MainActivity

class SplashActivity : AbsBaseActivity<SplashDataBinding>() {

    private val viewModel by viewModels<SplashViewModel> { getViewModelFactory() }

    private fun showMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }


    override fun initView(savedInstanceState: Bundle?) {
        setDarkStatusIcon(true)
    }

    override fun requestData() {
        viewModel.start().bindLifeCycle(this).subscribe { it ->
            when (it) {
                0, 1 -> {
                    showLogin()
                }
                else -> {
                    showMain()
                }
            }
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        dataBinding.vm = viewModel
    }

    override fun getContentView(): Int {
        return R.layout.activity_splash
    }

}
