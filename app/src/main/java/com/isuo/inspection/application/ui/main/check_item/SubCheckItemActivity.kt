package com.isuo.inspection.application.ui.main.check_item

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.isuo.inspection.application.BR
import com.isuo.inspection.application.R
import com.isuo.inspection.application.adapter.GenericQuickAdapter
import com.isuo.inspection.application.base.AbsBaseActivity
import com.isuo.inspection.application.base.ext.bindLifeCycle
import com.isuo.inspection.application.base.ext.getViewModelFactory
import com.isuo.inspection.application.common.ConstantStr
import com.isuo.inspection.application.databinding.CheckItemDataBinding
import com.isuo.inspection.application.model.bean.SubCheckItemBean
import com.isuo.inspection.application.model.bean.SubstationBean
import com.isuo.inspection.application.ui.main.device_list.DeviceListActivity
import kotlinx.android.synthetic.main.activity_check_item.*
import java.util.*

class SubCheckItemActivity : AbsBaseActivity<CheckItemDataBinding>() {

    private val viewModel by viewModels<SubCheckItemViewMode> { getViewModelFactory() }

    var dataList = ArrayList<SubCheckItemBean>()

    override fun initView(savedInstanceState: Bundle?) {
        val adapter = GenericQuickAdapter(
            R.layout.item_check_sub_item, this.dataList, BR.checkItem
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener { _, _, position ->
            val intent = Intent(this, DeviceListActivity::class.java)
            intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, dataList[position].id.get())
            intent.putExtra(ConstantStr.KEY_BUNDLE_STR, dataList[position].name.get())
            startActivity(intent)
        }
    }

    override fun requestData() {
        viewModel.start().bindLifeCycle(this).subscribe { list ->
            dataList.clear()
            dataList.addAll(list)
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    var subBeanId: Long? = null
    var subBeanName: String? = null

    override fun initData(savedInstanceState: Bundle?) {
        dataBinding.viewModel = viewModel
        subBeanId = intent.getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1)
        subBeanName = intent.getStringExtra(ConstantStr.KEY_BUNDLE_STR)
    }

    override fun getToolBarTitle(): String? {
        return subBeanName
    }

    override fun getContentView(): Int {
        return R.layout.activity_check_item
    }
}