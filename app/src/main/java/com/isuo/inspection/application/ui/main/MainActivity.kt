package com.isuo.inspection.application.ui.main

import android.os.Bundle
import com.isuo.inspection.application.R
import com.isuo.inspection.application.base.AbsBaseActivity
import com.isuo.inspection.application.databinding.MainDataBinding

class MainActivity : AbsBaseActivity<MainDataBinding>() {

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun getContentView(): Int {
        return R.layout.activity_main
    }
}