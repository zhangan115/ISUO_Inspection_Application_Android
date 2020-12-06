package com.isuo.inspection.application.ui.data

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import com.isuo.inspection.application.R
import com.isuo.inspection.application.base.AbsBaseActivity
import com.isuo.inspection.application.base.ext.getViewModelFactory
import com.isuo.inspection.application.common.ConstantStr
import com.isuo.inspection.application.databinding.DataBaseDataBinding

class DataBaseActivity : AbsBaseActivity<DataBaseDataBinding>() {

    private val viewModel by viewModels<DataBaseViewModel> { getViewModelFactory() }

    private var deviceName: String? = null
    private var inputType: Int = 0
    var deviceId: Long = 0L
    var checkPosition: String? = null

    override fun getToolBarTitle(): String? {
        return deviceName
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initData(savedInstanceState: Bundle?) {
        dataBinding.viewModel = viewModel
        deviceName = intent.getStringExtra(ConstantStr.KEY_BUNDLE_STR)
        checkPosition = intent.getStringExtra(ConstantStr.KEY_BUNDLE_STR_1)
        inputType = intent.getIntExtra(ConstantStr.KEY_BUNDLE_INT, -1)
        deviceId = intent.getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1L)
    }

    override fun getContentView(): Int {
        return R.layout.activity_data_base
    }
}