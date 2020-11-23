package com.isuo.inspection.application.base

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.isuo.inspection.application.app.ISUOApplication
import java.io.File

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    open var photo: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ISUOApplication.instance.openActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ISUOApplication.instance.closeActivity(this)
    }
}