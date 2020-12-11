package com.isuo.inspection.application.ui.main.search

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.isuo.inspection.application.BR
import com.isuo.inspection.application.R
import com.isuo.inspection.application.adapter.GenericQuickAdapter
import com.isuo.inspection.application.base.AbsBaseActivity
import com.isuo.inspection.application.base.ext.getViewModelFactory
import com.isuo.inspection.application.common.ConstantStr
import com.isuo.inspection.application.databinding.SearchSubDataBinding
import com.isuo.inspection.application.model.api.OkHttpManager
import com.isuo.inspection.application.model.bean.SubstationBean
import com.isuo.inspection.application.model.bean.SubstationNetBean
import com.isuo.inspection.application.ui.main.check_item.SubCheckItemActivity
import com.isuo.inspection.application.utils.EventObserver
import kotlinx.android.synthetic.main.activity_search_sub.*
import java.util.ArrayList

class SearchSubActivity : AbsBaseActivity<SearchSubDataBinding>() {

    private val viewModel by viewModels<SearchSubViewModel> { getViewModelFactory() }

    override fun getToolBarTitle(): String? {
        return "选择变电站"
    }

    var dataList = ArrayList<SubstationBean>()

    override fun initView(savedInstanceState: Bundle?) {
        viewModel.showResult.observe(this, EventObserver {
            dataList.clear()
            dataList.addAll(it)
            recyclerView.adapter?.notifyDataSetChanged()
        })
        val adapter = GenericQuickAdapter(
            R.layout.item_search_sub, this.dataList, BR.subBean
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
    }

    override fun initData(savedInstanceState: Bundle?) {
        dataBinding.viewModel = viewModel
    }

    override fun getContentView(): Int {
        return R.layout.activity_search_sub
    }
}