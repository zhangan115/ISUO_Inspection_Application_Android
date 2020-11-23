package com.isuo.inspection.application.base.ext

import android.content.Intent
import android.os.Build
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.isuo.inspection.application.app.ISUOApplication
import com.isuo.inspection.application.R
import com.isuo.inspection.application.base.factory.ViewModelFactory
import com.sito.tool.library.utils.ActivityUtilsV4
import java.io.File

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
    filePhoto: File
) {
    MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
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
        findViewById<TextView>(R.id.choosePhoto).setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, takePhotoRequestCode)
            dismiss()
        }
        lifecycleOwner(this@showChoosePhotoDialog)
    }
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
