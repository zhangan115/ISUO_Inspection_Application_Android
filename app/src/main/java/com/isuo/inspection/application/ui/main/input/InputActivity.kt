package com.isuo.inspection.application.ui.main.input

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BasicGridItem
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.GridItem
import com.afollestad.materialdialogs.bottomsheets.gridItems
import com.afollestad.materialdialogs.list.listItems
import com.afollestad.materialdialogs.list.listItemsSingleChoice
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
import com.orhanobut.logger.Logger
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
                val inputType2 = InputType2()
                inputType2.isFinish.observe(this, Observer {
                    viewModel.canClick.value = it
                })
                dataList2.add(inputType2)
                val adapter = GenericQuickAdapter(
                    R.layout.item_input_type_2, this.dataList2, BR.inputType2
                )
                recyclerView.adapter = adapter
                adapter.addChildClickViewIds(R.id.choosePhotoType)
                adapter.setOnItemChildClickListener { _, view, _ ->
                    if (view.id == R.id.choosePhotoType) {
                        MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT))
                            .show {
                                listItems(R.array.choose_photo_list){_, index, text ->
                                    dataList2[0].value3.set(text.toString())
                                    dataList2[0].chooseId.set(index)
                                    dataList2[0].chooseValue()
                                }
                            }
                    }
                }
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