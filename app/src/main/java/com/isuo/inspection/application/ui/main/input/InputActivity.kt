package com.isuo.inspection.application.ui.main.input

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItems
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
import com.isuo.inspection.application.model.bean.Type3StateBean
import com.isuo.inspection.application.ui.data.DataBaseActivity
import com.isuo.inspection.application.utils.EventObserver
import kotlinx.android.synthetic.main.activity_input.*

class InputActivity : AbsBaseActivity<InputDataBinding>() {

    private val viewModel by viewModels<InputViewModel> { getViewModelFactory() }

    private var submitDialog: MaterialDialog? = null

    private var valueType3State = ArrayList<Boolean>()

    override fun initView(savedInstanceState: Bundle?) {
        viewModel.checkType.value = this.checkName
        viewModel.checkPosition.value = this.checkPosition
        viewModel.showLoadingDialog.observe(this, EventObserver {
            if (it) {
                submitDialog = MaterialDialog(this).show {
                    customView(R.layout.dialog_loading_submit)
                }.noAutoDismiss()
            } else {
                submitDialog?.dismiss()
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(this)
        viewModel.showInputViews.observe(this, EventObserver {
            showInputView()
        })
        viewModel.uploadSuccess.observe(this, EventObserver {
            finish()
        })
    }

    private fun showInputView() {
        if (viewModel.measuringList.isEmpty()) {
            return
        }
        when (this.checkType) {
            0 -> {
                val inputType1 = InputType1()
                inputType1.positionId = viewModel.measuringList[0].measuringPointId
                inputType1.positionName = viewModel.measuringList[0].measuringPointName
                inputType1.isFinish.observe(this, Observer {
                    viewModel.canClick.value = it
                })
                viewModel.dataList1.add(inputType1)
                val adapter = GenericQuickAdapter(
                    R.layout.item_input_type_1, viewModel.dataList1, BR.inputType1
                )
                recyclerView.adapter = adapter
            }
            1 -> {
                val inputType2 = InputType2()
                inputType2.positionId = viewModel.measuringList[0].measuringPointId
                inputType2.positionName = viewModel.measuringList[0].measuringPointName
                inputType2.isFinish.observe(this, Observer {
                    viewModel.canClick.value = it
                })
                viewModel.dataList2.add(inputType2)
                val adapter = GenericQuickAdapter(
                    R.layout.item_input_type_2, viewModel.dataList2, BR.inputType2
                )
                recyclerView.adapter = adapter
                adapter.addChildClickViewIds(R.id.choosePhotoType)
                adapter.setOnItemChildClickListener { _, view, position ->
                    if (view.id == R.id.choosePhotoType) {
                        MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT))
                            .show {
                                listItems(R.array.choose_photo_list) { _, index, text ->
                                    viewModel.dataList2[position].value3.set(text.toString())
                                    viewModel.dataList2[position].chooseId.set(index)
                                }
                                lifecycleOwner(this@InputActivity)
                            }
                    }
                }
            }
            else -> {
                for ((index, item) in viewModel.measuringList.withIndex()) {
                    val inputType3 = InputType3()
                    inputType3.positionText.set(item.measuringPointName)
                    inputType3.positionId = item.measuringPointId
                    inputType3.positionName = item.measuringPointName
                    val bean = Type3StateBean()
                    bean.chooseId.set(index)
                    bean.isFinish.value = false
                    inputType3.isFinish.value = bean
                    valueType3State.add(false)
                    inputType3.isFinish.observe(this, Observer { bean1 ->
                        if (bean1.chooseId.get() != null && bean1.isFinish.value != null) {
                            valueType3State[bean1.chooseId.get()!!] = bean1.isFinish.value!!
                            val state = valueType3State.filter { it }
                            viewModel.canClick.value = (state.size == valueType3State.size)
                        }
                    })
                    viewModel.dataList3.add(inputType3)
                }
                val adapter = GenericQuickAdapter(
                    R.layout.item_input_type_3, viewModel.dataList3, BR.inputType3
                )
                recyclerView.adapter = adapter
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_history, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_history) {
            val intent = Intent(this, DataBaseActivity::class.java)
            intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, deviceId)
            intent.putExtra(ConstantStr.KEY_BUNDLE_STR, deviceName)
            intent.putExtra(ConstantStr.KEY_BUNDLE_INT, checkType)
            intent.putExtra(ConstantStr.KEY_BUNDLE_STR_1, viewModel.equipmentTypeCode)
            intent.putExtra(ConstantStr.KEY_BUNDLE_STR_2, viewModel.checkPosition.value)
            startActivity(intent)
        }
        return true
    }

    private var deviceName: String? = null
    private var checkType: Int = 0
    var deviceId: Long? = 0L
    var checkName: String? = null
    var checkPosition: String? = null
    private val nameList = listOf("开关柜超声波局放检测", "开关柜特高频局放检测", "开关柜暂态地电压检测")

    override fun initData(savedInstanceState: Bundle?) {
        dataBinding.viewModel = viewModel
        checkType = intent.getIntExtra(ConstantStr.KEY_BUNDLE_INT, -1)
        checkName = nameList[checkType]
        viewModel.checkTypeId = checkType + 1
        deviceId = intent.getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1L)
        deviceName = intent.getStringExtra(ConstantStr.KEY_BUNDLE_STR)
        viewModel.equipmentTypeCode = intent.getStringExtra(ConstantStr.KEY_BUNDLE_STR_1)
        if (checkType == 2) {
            viewModel.showPositionView.value = false
        }
        val subId = intent.getLongExtra(ConstantStr.KEY_BUNDLE_LONG_2, -1)
        val subName = intent.getStringExtra(ConstantStr.KEY_BUNDLE_STR_2)
        if (deviceId != null && deviceName != null) {
            viewModel.setData(subId, subName, deviceId!!, deviceName!!)
        }
        viewModel.start()
    }

    override fun getToolBarTitle(): String? {
        return deviceName
    }

    override fun getContentView(): Int {
        return R.layout.activity_input
    }

}