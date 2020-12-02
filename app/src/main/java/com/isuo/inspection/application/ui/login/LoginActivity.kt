package com.isuo.inspection.application.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import com.isuo.inspection.application.R
import com.isuo.inspection.application.app.ISUOApplication
import com.isuo.inspection.application.base.AbsBaseActivity
import com.isuo.inspection.application.base.ext.getViewModelFactory
import com.isuo.inspection.application.common.ConstantStr
import com.isuo.inspection.application.databinding.LoginDataBinding
import com.isuo.inspection.application.ui.login.forget_pass.ForgetPassActivity
import com.isuo.inspection.application.ui.main.MainActivity
import com.isuo.inspection.application.utils.EventObserver


class LoginActivity : AbsBaseActivity<LoginDataBinding>() {

    private val viewModel by viewModels<LoginViewModel> { getViewModelFactory() }

    private fun showMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun initView(savedInstanceState: Bundle?) {
        setDarkStatusIcon(true)
        viewModel.toLoginEvent.observe(this, EventObserver {
            showMainActivity()
        })
        viewModel.toForgetPassEvent.observe(this, EventObserver {
            startActivity(Intent(this, ForgetPassActivity::class.java))
        })
        viewModel.toShowAgreeEvent.observe(this, EventObserver {

        })
    }

    override fun initData(savedInstanceState: Bundle?) {
        dataBinding.viewModel = viewModel
    }

    override fun getContentView(): Int {
        return R.layout.activity_login
    }

    fun showInfo(view: View) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ISUOApplication.instance.isLoginOpen = true
    }

    override fun onDestroy() {
        super.onDestroy()
        ISUOApplication.instance.isLoginOpen = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10000 && resultCode == Activity.RESULT_OK) {
            if (TextUtils.isEmpty(viewModel.name.value)) {
                val phoneCode = data?.getStringExtra(ConstantStr.KEY_BUNDLE_STR)
                if (!TextUtils.isEmpty(phoneCode)) {
                    viewModel.name.value = phoneCode
                }
            }
        }
    }
}