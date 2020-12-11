package com.isuo.inspection.application.ui.main.device_list

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.isuo.inspection.application.BR
import com.isuo.inspection.application.R
import com.isuo.inspection.application.adapter.GenericQuickAdapter
import com.isuo.inspection.application.base.AbsBaseActivity
import com.isuo.inspection.application.base.ext.getViewModelFactory
import com.isuo.inspection.application.common.ConstantInt
import com.isuo.inspection.application.common.ConstantStr
import com.isuo.inspection.application.databinding.DeviceListDataBinding
import com.isuo.inspection.application.model.bean.DeviceBean
import com.isuo.inspection.application.ui.main.input.InputActivity
import com.isuo.inspection.application.utils.EventObserver
import kotlinx.android.synthetic.main.activity_device_list.*

class DeviceListActivity : AbsBaseActivity<DeviceListDataBinding>() {

    private val viewModel by viewModels<DeviceListViewModel> { getViewModelFactory() }
    var dataList = ArrayList<DeviceBean>()

    override fun initView(savedInstanceState: Bundle?) {
        val adapter = GenericQuickAdapter(
            R.layout.item_device_list, this.dataList, BR.deviceBean
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        refreshLayout.setOnRefreshListener {
            requestData()
        }
        refreshLayout.setOnLoadMoreListener {
            requestMoreData()
        }
        adapter.setOnItemClickListener { _, _, position ->
            val intent = Intent(this, InputActivity::class.java)
            intent.putExtra(ConstantStr.KEY_BUNDLE_STR, dataList[position].name.get())
            intent.putExtra(ConstantStr.KEY_BUNDLE_STR_1, dataList[position].equipmentTypeCode)
            intent.putExtra(ConstantStr.KEY_BUNDLE_INT, checkId?.toInt())
            intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, dataList[position].id.get())
            val id = this.intent.getLongExtra(ConstantStr.KEY_BUNDLE_LONG_2, -1)
            val name = this.intent.getStringExtra(ConstantStr.KEY_BUNDLE_STR_2)
            intent.putExtra(ConstantStr.KEY_BUNDLE_LONG_2, id)
            intent.putExtra(ConstantStr.KEY_BUNDLE_STR_2, name)
            startActivity(intent)
        }
        viewModel.showResult.observe(this, EventObserver {
            dataList.clear()
            dataList.addAll(it)
            recyclerView.adapter?.notifyDataSetChanged()
            if (it.size < ConstantInt.PAGE_SIZE) {
                refreshLayout.finishRefreshWithNoMoreData()
            } else {
                refreshLayout.finishRefresh()
            }
        })
        viewModel.showMoreResult.observe(this, EventObserver {
            dataList.addAll(it)
            recyclerView.adapter?.notifyDataSetChanged()
            if (it.size < ConstantInt.PAGE_SIZE) {
                refreshLayout.finishRefreshWithNoMoreData()
            } else {
                refreshLayout.finishLoadMore()
            }
        })
        viewModel.requestState.value = ConstantInt.REQUEST_STATE_LOADING
    }

    override fun initData(savedInstanceState: Bundle?) {
        dataBinding.viewModel = viewModel
        checkId = intent.getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1)
        viewModel.subId = intent.getLongExtra(ConstantStr.KEY_BUNDLE_LONG_1, -1)
        checkName = intent.getStringExtra(ConstantStr.KEY_BUNDLE_STR)
    }

    override fun requestData() {
        viewModel.start()
    }

    private fun requestMoreData() {
        viewModel.loadMore(dataList[dataList.size - 1].id.get())
    }

    var checkName: String? = null
    var checkId: Long? = null

    override fun getToolBarTitle(): String? {
        return checkName
    }

    override fun getContentView(): Int {
        return R.layout.activity_device_list
    }
}