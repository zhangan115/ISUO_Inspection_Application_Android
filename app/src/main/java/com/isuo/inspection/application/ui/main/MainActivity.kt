package com.isuo.inspection.application.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.GeneratedAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.isuo.inspection.application.BR
import com.isuo.inspection.application.R
import com.isuo.inspection.application.adapter.GenericQuickAdapter
import com.isuo.inspection.application.base.AbsBaseActivity
import com.isuo.inspection.application.base.ext.getViewModelFactory
import com.isuo.inspection.application.common.ConstantInt
import com.isuo.inspection.application.common.ConstantStr
import com.isuo.inspection.application.databinding.MainDataBinding
import com.isuo.inspection.application.model.bean.SubstationBean
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AbsBaseActivity<MainDataBinding>() {


    private val viewModel by viewModels<MainViewModel> { getViewModelFactory() }

    var dataList = ArrayList<SubstationBean>()

    override fun getToolBarTitle(): String? {
        return "运维"
    }

    override fun initView(savedInstanceState: Bundle?) {
        val adapter = GenericQuickAdapter(
            R.layout.item_main_sub, this.dataList, BR.subBean
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun initData(savedInstanceState: Bundle?) {
        dataBinding.viewModel = viewModel
        for (index in 0..10) {
            dataList.add(SubstationBean(index.toLong(), "变电站$index"))
        }
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