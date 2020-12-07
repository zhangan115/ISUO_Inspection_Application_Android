package com.isuo.inspection.application.ui.data.history

import android.view.View
import android.widget.ExpandableListView
import androidx.fragment.app.viewModels
import com.isuo.inspection.application.R
import com.isuo.inspection.application.base.BaseFragment
import com.isuo.inspection.application.base.ext.async
import com.isuo.inspection.application.base.ext.bindLifeCycle
import com.isuo.inspection.application.base.ext.getViewModelFactory
import com.isuo.inspection.application.common.ConstantStr
import com.isuo.inspection.application.databinding.HistoryListDataBinding
import com.isuo.inspection.application.model.bean.HistoryData
import com.isuo.inspection.application.ui.data.history.adapter.Type1Adapter
import com.isuo.inspection.application.ui.data.history.adapter.Type2Adapter
import com.isuo.inspection.application.ui.data.history.adapter.Type3Adapter
import kotlinx.android.synthetic.main.fragment_history_list.*
import java.util.ArrayList

class HistoryListFragment : BaseFragment<HistoryListDataBinding>() {

    var dataList = ArrayList<HistoryData>()

    private val viewModel by viewModels<HistoryListViewModel> { getViewModelFactory() }

    override fun lazyLoad() {

    }

    private fun getData() {
        viewModel.start().async(1000).bindLifeCycle(this).subscribe { list ->
            dataList.clear()
            dataList.addAll(list)
            when (inputType) {
                0 -> {
                    adapter1?.setData(dataList)
                }
                1 -> {
                    adapter2?.setData(dataList)
                }
                else -> {
                    adapter3?.setData(dataList)
                }
            }
            for (index in 0 until expandableListView.expandableListAdapter!!.groupCount) {
                expandableListView.expandGroup(index, false)
            }
            refreshLayout.finishRefresh()
        }
    }

    private fun loadMoreData() {
        viewModel.start().async(1000).bindLifeCycle(this).subscribe { list ->
            dataList.addAll(list)
            when (inputType) {
                0 -> {
                    adapter1?.addData(dataList)
                }
                1 -> {
                    adapter2?.addData(dataList)
                }
                else -> {
                    adapter3?.addData(dataList)
                }
            }
            for (index in 0 until expandableListView.expandableListAdapter!!.groupCount) {
                expandableListView.expandGroup(index, false)
            }
            refreshLayout.finishLoadMore()
            refreshLayout.finishLoadMoreWithNoMoreData()
        }
    }

    override fun getContentView(): Int {
        return R.layout.fragment_history_list
    }

    private var deviceName: String? = null
    private var inputType: Int = 0
    var deviceId: Long = 0L
    var checkPosition: String? = null

    override fun initData() {
        inputType = arguments?.getInt(ConstantStr.KEY_BUNDLE_INT)!!
        deviceName = arguments?.getString(ConstantStr.KEY_BUNDLE_STR)
        checkPosition = arguments?.getString(ConstantStr.KEY_BUNDLE_STR_1)
        inputType = arguments?.getInt(ConstantStr.KEY_BUNDLE_INT)!!
        deviceId = arguments?.getLong(ConstantStr.KEY_BUNDLE_LONG, -1L)!!
    }

    private var adapter1: Type1Adapter? = null
    private var adapter2: Type2Adapter? = null
    private var adapter3: Type3Adapter? = null

    override fun initView() {
        expandableListView.setOnGroupClickListener({ _, _, _, _ -> true }, false)
        when (inputType) {
            0 -> {
                adapter1 = Type1Adapter(
                    activity,
                    expandableListView,
                    R.layout.item_type_group,
                    R.layout.item_type_child_1
                )
                expandableListView.setAdapter(adapter1)
            }
            1 -> {
                adapter2 = Type2Adapter(
                    activity,
                    expandableListView,
                    R.layout.item_type_group,
                    R.layout.item_type_child_2
                )
                expandableListView.setAdapter(adapter2)
            }
            else -> {
                adapter3 = Type3Adapter(
                    activity,
                    expandableListView,
                    R.layout.item_type_group,
                    R.layout.item_type_child_3
                )
                expandableListView.setAdapter(adapter3)
            }
        }
        refreshLayout.setOnRefreshListener {
            getData()
        }
        refreshLayout.setOnLoadMoreListener {
            loadMoreData()
        }
        refreshLayout.autoRefresh()
    }

    override fun setViewModel(dataBinding: HistoryListDataBinding?) {
        dataBinding?.viewModel = viewModel
    }
}