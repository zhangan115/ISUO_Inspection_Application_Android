package com.isuo.inspection.application.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.isuo.inspection.application.BR
import com.isuo.inspection.application.R
import com.isuo.inspection.application.adapter.GenericQuickAdapter
import com.isuo.inspection.application.base.AbsBaseActivity
import com.isuo.inspection.application.base.ext.async
import com.isuo.inspection.application.base.ext.bindLifeCycle
import com.isuo.inspection.application.base.ext.getViewModelFactory
import com.isuo.inspection.application.common.ConstantInt
import com.isuo.inspection.application.common.ConstantStr
import com.isuo.inspection.application.databinding.MainDataBinding
import com.isuo.inspection.application.model.bean.SubstationBean
import com.isuo.inspection.application.ui.main.check_item.SubCheckItemActivity
import com.isuo.inspection.application.ui.main.search.SearchSubActivity
import com.isuo.inspection.application.ui.user.user_center.UserCenterActivity
import com.isuo.inspection.application.utils.EventObserver
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AbsBaseActivity<MainDataBinding>() {

    private val viewModel by viewModels<MainViewModel> { getViewModelFactory() }

    var dataList = ArrayList<SubstationBean>()

    override val showToolbar: Boolean
        get() = false

    override fun initView(savedInstanceState: Bundle?) {
        val adapter = GenericQuickAdapter(
            R.layout.item_main_sub, this.dataList, BR.subBean
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener { _, _, position ->
            val bean = dataList[position]
            val intent = Intent(this, SubCheckItemActivity::class.java)
            intent.putExtra(ConstantStr.KEY_BUNDLE_LONG, bean.id.get())
            intent.putExtra(ConstantStr.KEY_BUNDLE_STR, bean.name.get())
            startActivity(intent)
        }
        viewModel.showSearchSub.observe(this, EventObserver {
            startActivity(Intent(this, SearchSubActivity::class.java))
        })
        viewModel.showUserCenter.observe(this, EventObserver {
            startActivity(Intent(this, UserCenterActivity::class.java))
        })
        refreshLayout.setOnRefreshListener {
            getData()
        }
        refreshLayout.setOnLoadMoreListener {
            loadMoreData()
        }
    }

    private fun getData(){
        viewModel.start().async(1000).bindLifeCycle(this).subscribe { list ->
            dataList.clear()
            dataList.addAll(list)
            recyclerView.adapter?.notifyDataSetChanged()
            refreshLayout.finishRefresh()
        }
    }

    override fun requestData() {
        refreshLayout.autoRefresh()
    }

    private fun loadMoreData() {
        viewModel.loadMore().async(1000).bindLifeCycle(this).subscribe { list ->
            dataList.addAll(list)
            recyclerView.adapter?.notifyDataSetChanged()
            if (dataList.size > ConstantInt.PAGE_SIZE) {
                refreshLayout.finishLoadMoreWithNoMoreData()
            } else {
                refreshLayout.finishLoadMore(true)
            }

        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        dataBinding.viewModel = viewModel
        viewModel.requestState.value = ConstantInt.REQUEST_STATE_DATA
    }

    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    private var currentTime: Long = 0

    override fun onBackPressed() {
        if (currentTime == 0L) {
            Toast.makeText(this, "再次点击退出App", Toast.LENGTH_SHORT).show()
            currentTime = System.currentTimeMillis()
        } else {
            if (System.currentTimeMillis() - currentTime >= 2000) {
                currentTime = 0L
                Toast.makeText(this, "再次点击退出App", Toast.LENGTH_SHORT).show()
            } else {
                super.onBackPressed()
            }
        }
    }
}