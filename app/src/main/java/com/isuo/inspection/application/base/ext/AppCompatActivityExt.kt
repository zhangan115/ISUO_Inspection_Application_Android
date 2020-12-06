package com.isuo.inspection.application.base.ext

import android.Manifest
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.isuo.inspection.application.R
import com.isuo.inspection.application.app.ISUOApplication
import com.isuo.inspection.application.base.factory.ViewModelFactory
import com.qw.soul.permission.SoulPermission
import com.qw.soul.permission.bean.Permission
import com.qw.soul.permission.bean.Permissions
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener
import com.sito.tool.library.activity.ShowPhotoListActivity
import com.sito.tool.library.utils.ActivityUtilsV4
import java.io.File
import java.util.ArrayList

const val ACTION_TAKE_PHOTO = 1000
const val ACTION_CHOOSE_FILE = 2000

fun AppCompatActivity.getViewModelFactory(): ViewModelFactory {
    val userRepository = (applicationContext as ISUOApplication).userRepository
    val workRepository = (applicationContext as ISUOApplication).taskRepository
    val dataRepository = (applicationContext as ISUOApplication).dataRepository
    return ViewModelFactory(
        userRepository,
        workRepository,
        dataRepository,
        this
    )
}

fun AppCompatActivity.showChoosePhotoDialog(
    takePhotoRequestCode: Int,
    filePhoto: File,
    photo: String? = null,
    photos: ArrayList<String>? = null,
    position: Int = -1
) {
    val permissions =
        Permissions.build(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    SoulPermission.getInstance().checkAndRequestPermissions(permissions, object :
        CheckRequestPermissionsListener {
        override fun onPermissionDenied(refusedPermissions: Array<out Permission>) {
            MaterialDialog(this@showChoosePhotoDialog).show {
                title(null, "提示")
                message(null, "请打开相关权限，否则APP无法提供相关功能")
                positiveButton(R.string.sure)
                negativeButton(R.string.cancel)
                positiveButton {
                    it.dismiss()
                    SoulPermission.getInstance().goApplicationSettings()
                }
            }
        }

        override fun onAllPermissionOk(allPermissions: Array<out Permission>?) {
            MaterialDialog(this@showChoosePhotoDialog, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
                customView(
                    R.layout.dialog_choose_photo,
                    noVerticalPadding = true,
                    horizontalPadding = true,
                    dialogWrapContent = true
                )
                findViewById<TextView>(R.id.takePhoto).setOnClickListener {
                    ActivityUtilsV4.startCameraToPhoto(
                        this@showChoosePhotoDialog,
                        filePhoto,
                        takePhotoRequestCode
                    )
                    dismiss()
                }
                if (!TextUtils.isEmpty(photo) || photos != null) {
                    findViewById<TextView>(R.id.checkPhotoLayout).visibility = View.VISIBLE
                    findViewById<TextView>(R.id.checkPhotoLayout).setOnClickListener {
                        ShowPhotoListActivity.startActivity(
                            this@showChoosePhotoDialog,
                            ISUOApplication.appHost() + photo,
                            0,
                            R.mipmap.emptyimg
                        )
                        dismiss()
                    }
                }
                findViewById<TextView>(R.id.choosePhoto).setOnClickListener {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "image/*"
                    startActivityForResult(intent, takePhotoRequestCode)
                    dismiss()
                }
                lifecycleOwner(this@showChoosePhotoDialog)
            }
        }
    })
}

fun AppCompatActivity.findColor(id: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(id, null)
    } else {
        resources.getColor(id)
    }
}

fun AppCompatActivity.findStrById(id: Int): String {
    return resources.getString(id)
}
