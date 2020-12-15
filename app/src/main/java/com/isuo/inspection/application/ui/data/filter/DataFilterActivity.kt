package com.isuo.inspection.application.ui.data.filter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.isuo.inspection.application.BR
import com.isuo.inspection.application.R
import com.isuo.inspection.application.adapter.GenericQuickAdapter
import com.isuo.inspection.application.base.AbsBaseActivity
import com.isuo.inspection.application.base.ext.getViewModelFactory
import com.isuo.inspection.application.common.ConstantStr
import com.isuo.inspection.application.databinding.DataFilterDataBinding
import com.isuo.inspection.application.model.bean.MeasuringPointBean
import com.isuo.inspection.application.utils.ChooseDateDialog
import com.isuo.inspection.application.utils.DataUtil
import com.isuo.inspection.application.utils.EventObserver
import kotlinx.android.synthetic.main.activity_data_filter.*

class DataFilterActivity : AbsBaseActivity<DataFilterDataBinding>() {

    private val viewModel by viewModels<DataFilterViewModel> { getViewModelFactory() }
    private var checkType: Int = 0
    private var measuringList = ArrayList<MeasuringPointBean>()

    override fun getToolBarTitle(): String? {
        return "数据筛选"
    }

    override fun initView(savedInstanceState: Bundle?) {
        viewModel.showDataEvent.observe(this, EventObserver {
            this.measuringList.clear()
            this.measuringList.addAll(it)
            this.recyclerView.adapter?.notifyDataSetChanged()
        })
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        val adapter = GenericQuickAdapter(
            R.layout.item_measuring_point, this.measuringList, BR.measuring
        )
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener { _, _, position ->
            for ((p, item) in measuringList.withIndex()) {
                if (p == position) {
                    item.selectId.set(item.id.get())
                    item.selectId.get()?.let {
                        viewModel.position.value = it
                        viewModel.measuringName = item.name.get()
                    }
                } else {
                    item.selectId.set(-1)
                }
            }
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_finish, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_finish) {
            if (TextUtils.isEmpty(viewModel.startDate.value) || TextUtils.isEmpty(viewModel.endDate.value)) {
                viewModel.toastStr.value = "请选择时间"
                return false
            }
            if (checkType == 2) {
                if (viewModel.position.value == -1L) {
                    viewModel.toastStr.value = "请选择测点"
                    return false
                }
            }
            val intent = Intent()
            intent.putExtra(ConstantStr.KEY_BUNDLE_STR, viewModel.startDate.value)
            intent.putExtra(ConstantStr.KEY_BUNDLE_STR_1, viewModel.endDate.value)
            if (checkType == 2) {
                intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, viewModel.position.value)
                intent.putExtra(ConstantStr.KEY_BUNDLE_STR_2, viewModel.measuringName)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        return true
    }

    override fun initData(savedInstanceState: Bundle?) {
        dataBinding.viewModel = viewModel
        checkType = intent.getIntExtra(ConstantStr.KEY_BUNDLE_INT, -1)
        viewModel.checkType.value = checkType
        viewModel.equipmentTypeCode = intent.getStringExtra(ConstantStr.KEY_BUNDLE_STR)
        if (checkType == 2) {
            viewModel.start()
        }
    }

    override fun getContentView(): Int {
        return R.layout.activity_data_filter
    }

    fun showChooseStart(view: View) {
        ChooseDateDialog(this)
            .pickDay()
            .setResultListener { calendar ->
                calendar?.let {
                    val text: String =
                        DataUtil.timeFormat(calendar.timeInMillis, "yyyy-MM-dd")
                    viewModel.startDate.value = text
                }
            }
            .show()
    }

    fun showChooseEnd(view: View) {
        ChooseDateDialog(this)
            .pickDay()
            .setResultListener { calendar ->
                calendar?.let {
                    val text: String =
                        DataUtil.timeFormat(calendar.timeInMillis, "yyyy-MM-dd")
                    viewModel.endDate.value = text
                }
            }
            .show()
    }
}