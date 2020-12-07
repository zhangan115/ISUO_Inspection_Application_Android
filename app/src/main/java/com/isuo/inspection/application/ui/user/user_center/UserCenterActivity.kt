package com.isuo.inspection.application.ui.user.user_center

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.isuo.inspection.application.R
import com.isuo.inspection.application.app.ISUOApplication
import com.isuo.inspection.application.base.AbsBaseActivity
import com.isuo.inspection.application.base.ext.getViewModelFactory
import com.isuo.inspection.application.base.ext.showChoosePhotoDialog
import com.isuo.inspection.application.databinding.UserCenterDataBinding
import com.isuo.inspection.application.ui.user.forget_pass.ForgetPassActivity
import com.isuo.inspection.application.ui.user.user_info.UserInfoActivity
import com.isuo.inspection.application.utils.EventObserver
import java.io.File

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
        val user = ISUOApplication.instance.getCurrentUser()
        user?.let {
            viewModel.userImageUrl.value = it.portraitUrl
        }
        viewModel.toShowUserPhoto.observe(this, EventObserver {
            if (user != null) {
                showChoosePhotoDialog(200, viewModel.userImageUrl.value)
            }
        })
        viewModel.toExitApp.observe(this, EventObserver {
            MaterialDialog(this)
                .show {
                    this.message(null, "退出当前账号?")
                    this.positiveButton(R.string.sure)
                    this.negativeButton(R.string.cancel)
                    this.positiveButton {
                        ISUOApplication.instance.needLogin()
                    }
                    lifecycleOwner(this@UserCenterActivity)
                }
        })
    }

    override fun dealFile(requestCode: Int, file: File) {
        if (requestCode == 200) {
            viewModel.userImageUrl.value = file.absolutePath
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        dataBinding.viewModel = viewModel
    }

    override fun getContentView(): Int {
        return R.layout.activity_user_center
    }

}