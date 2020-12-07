package com.isuo.inspection.application.ui.data.filter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import com.isuo.inspection.application.R
import com.isuo.inspection.application.base.AbsBaseActivity
import com.isuo.inspection.application.base.ext.getViewModelFactory
import com.isuo.inspection.application.common.ConstantStr
import com.isuo.inspection.application.databinding.DataFilterDataBinding
import com.isuo.inspection.application.utils.ChooseDateDialog
import com.isuo.inspection.application.utils.DataUtil

class DataFilterActivity : AbsBaseActivity<DataFilterDataBinding>() {

    private val viewModel by viewModels<DataFilterViewModel> { getViewModelFactory() }
    private var checkType: Int = 0
    private var positionText = -1

    override fun getToolBarTitle(): String? {
        return "数据筛选"
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_finish, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_finish) {
            val intent = Intent()
            intent.putExtra(ConstantStr.KEY_BUNDLE_STR, viewModel.startDate.value)
            intent.putExtra(ConstantStr.KEY_BUNDLE_STR_1, viewModel.endDate.value)
            if (checkType == 2) {
                intent.putExtra(ConstantStr.KEY_BUNDLE_INT, positionText)
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
    }

    override fun getContentView(): Int {
        return R.layout.activity_data_filter
    }

    fun buttonSelect(view: View) {
        val tag: String = view.tag as String
        viewModel.position.value = tag.toInt()
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