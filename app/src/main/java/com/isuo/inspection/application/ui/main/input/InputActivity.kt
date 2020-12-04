package com.isuo.inspection.application.ui.main.input

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.isuo.inspection.application.BR
import com.isuo.inspection.application.R
import com.isuo.inspection.application.adapter.GenericQuickAdapter
import com.isuo.inspection.application.base.AbsBaseActivity
import com.isuo.inspection.application.base.ext.getViewModelFactory
import com.isuo.inspection.application.common.ConstantStr
import com.isuo.inspection.application.databinding.InputDataBinding
import com.isuo.inspection.application.model.bean.InputType1
import com.isuo.inspection.application.model.bean.InputType2
import com.isuo.inspection.application.model.bean.InputType3
import kotlinx.android.synthetic.main.activity_input.*

class InputActivity : AbsBaseActivity<InputDataBinding>() {

    private val viewModel by viewModels<InputViewModel> { getViewModelFactory() }

    var dataList1 = ArrayList<InputType1>()
    var dataList2 = ArrayList<InputType2>()
    var dataList3 = ArrayList<InputType3>()

    override fun initView(savedInstanceState: Bundle?) {
        viewModel.checkType.value = this.checkName
        viewModel.checkPosition.value = this.checkPosition
        recyclerView.layoutManager = LinearLayoutManager(this)
        when (this.inputType) {
            0 -> {
                val inputType1 = InputType1()
                inputType1.isFinish.observe(this, Observer {
                    viewModel.canClick.value = it
                })
                dataList1.add(inputType1)
                val adapter = GenericQuickAdapter(
                    R.layout.item_input_type_1, this.dataList1, BR.inputType1
                )
                recyclerView.adapter = adapter
            }
            1 -> {

            }
            else -> {

            }
        }
    }

    private var deviceName: String? = null
    private var inputType: Int = 0
    var deviceId: Long = 0L
    var checkName: String? = null
    var checkPosition: String? = null
    private val nameList = listOf("开关柜超声波局放检测", "开关柜特高频局放检测", "开关柜暂态地电压检测")

    override fun initData(savedInstanceState: Bundle?) {
        dataBinding.viewModel = viewModel
        deviceName = intent.getStringExtra(ConstantStr.KEY_BUNDLE_STR)
        checkPosition = intent.getStringExtra(ConstantStr.KEY_BUNDLE_STR_1)
        inputType = intent.getIntExtra(ConstantStr.KEY_BUNDLE_INT, -1)
        deviceId = intent.getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1L)
        checkName = nameList[inputType]
    }

    override fun getToolBarTitle(): String? {
        return deviceName
    }

    override fun getContentView(): Int {
        return R.layout.activity_input
    }

}