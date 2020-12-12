package com.isuo.inspection.application.ui.user.user_center

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.isuo.inspection.application.R
import com.isuo.inspection.application.app.ISUOApplication
import com.isuo.inspection.application.base.AbsBaseActivity
import com.isuo.inspection.application.base.ext.getViewModelFactory
import com.isuo.inspection.application.base.ext.showChoosePhotoDialog
import com.isuo.inspection.application.databinding.UserCenterDataBinding
import com.isuo.inspection.application.model.bean.AppVersion
import com.isuo.inspection.application.ui.user.forget_pass.ForgetPassActivity
import com.isuo.inspection.application.ui.user.suggest.SuggestActivity
import com.isuo.inspection.application.ui.user.user_info.UserInfoActivity
import com.isuo.inspection.application.utils.DownloadAppUtils
import com.isuo.inspection.application.utils.EventObserver
import com.qw.soul.permission.SoulPermission
import com.qw.soul.permission.bean.Permission
import com.qw.soul.permission.bean.Permissions
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener
import java.io.File

class UserCenterActivity : AbsBaseActivity<UserCenterDataBinding>() {

    private val viewModel by viewModels<UserCenterViewModel> { getViewModelFactory() }

    override fun getToolBarTitle(): String? {
        return "个人中心"
    }

    override fun initView(savedInstanceState: Bundle?) {
        viewModel.toShowUserInfo.observe(this, EventObserver {
            startActivityForResult(Intent(this, UserInfoActivity::class.java), 1001)
        })
        viewModel.toChangePass.observe(this, EventObserver {
            startActivity(Intent(this, ForgetPassActivity::class.java))
        })
        viewModel.toSuggest.observe(this, EventObserver {
            startActivity(Intent(this, SuggestActivity::class.java))
        })
        viewModel.toCheckNewEvenVersion.observe(this, EventObserver { appVersion ->
            MaterialDialog(this).show {
                title(text = "版本更新")
                message(text = appVersion.versionDescription)
                if (appVersion.isUpgrade == 0) {
                    negativeButton(text = "忽略", click = {
                        viewModel.ignoreVersion(appVersion)
                        it.dismiss()
                    })
                }
                positiveButton(text = "更新", click = {
                    downloadAppVersion(appVersion)
                })
            }
        })
        val user = ISUOApplication.instance.userRepository.getUser()
        viewModel.userImageUrl.value = user.headPic
        viewModel.toShowUserPhoto.observe(this, EventObserver {
            showChoosePhotoDialog(200, viewModel.userImageUrl.value)
        })
        viewModel.toExitEvent.observe(this, EventObserver {
            ISUOApplication.instance.needLogin()
        })
        viewModel.toExitApp.observe(this, EventObserver {
            MaterialDialog(this)
                .show {
                    viewModel.userExit()
                    this.message(null, "退出当前账号?")
                    this.positiveButton(R.string.sure)
                    this.negativeButton(R.string.cancel)
                    this.positiveButton {
                        viewModel.userExit()
                    }
                    lifecycleOwner(this@UserCenterActivity)
                }
        })
        viewModel.toExitEvent.observe(this, EventObserver {
            ISUOApplication.instance.needLogin()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            val user = ISUOApplication.instance.userRepository.getUser()
            viewModel.userName.value = user.realName
            viewModel.userPhone.value = user.mobile
        }
    }

    override fun dealFile(requestCode: Int, file: File) {
        if (requestCode == 200) {
            viewModel.toUploadUserPhoto(file)
        }
    }

    private fun downloadAppVersion(it: AppVersion) {
        val permissions =
            Permissions.build(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        SoulPermission.getInstance().checkAndRequestPermissions(permissions, object :
            CheckRequestPermissionsListener {
            override fun onPermissionDenied(refusedPermissions: Array<out Permission>) {
                MaterialDialog(this@UserCenterActivity)
                    .show {
                        viewModel.userExit()
                        this.message(null, "请打开相关权限，否则APP无法提供相关功能?")
                        this.positiveButton(R.string.sure)
                        this.negativeButton(R.string.cancel)
                        this.negativeButton {
                            Toast.makeText(
                                this@UserCenterActivity,
                                "请手动进入APP设置界面打开相关权限",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        this.positiveButton {
                            SoulPermission.getInstance().goApplicationSettings()
                        }
                        lifecycleOwner(this@UserCenterActivity)
                    }
            }

            override fun onAllPermissionOk(allPermissions: Array<out Permission>?) {
                DownloadAppUtils.DownLoad(
                    this@UserCenterActivity,
                    it.url,
                    findString(R.string.app_name) + ".apk"
                )
            }
        })
    }

    override fun initData(savedInstanceState: Bundle?) {
        dataBinding.viewModel = viewModel
    }

    override fun getContentView(): Int {
        return R.layout.activity_user_center
    }

}