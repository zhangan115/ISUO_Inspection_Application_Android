package com.isuo.inspection.application.base.ext

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.isuo.inspection.application.app.ISUOApplication
import com.isuo.inspection.application.base.factory.ViewModelFactory

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
