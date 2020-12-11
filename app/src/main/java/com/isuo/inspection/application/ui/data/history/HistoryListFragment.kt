package com.isuo.inspection.application.ui.data.history

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isuo.inspection.application.R
import com.isuo.inspection.application.adapter.RVAdapter
import com.isuo.inspection.application.app.ISUOApplication
import com.isuo.inspection.application.base.BaseFragment
import com.isuo.inspection.application.base.ext.async
import com.isuo.inspection.application.base.ext.bindLifeCycle
import com.isuo.inspection.application.base.ext.getViewModelFactory
import com.isuo.inspection.application.common.ConstantInt
import com.isuo.inspection.application.common.ConstantStr
import com.isuo.inspection.application.databinding.HistoryListDataBinding
import com.isuo.inspection.application.model.bean.*
import com.isuo.inspection.application.ui.data.history.adapter.Type1Adapter
import com.isuo.inspection.application.ui.data.history.adapter.Type2Adapter
import com.isuo.inspection.application.ui.data.history.adapter.Type3Adapter
import com.isuo.inspection.application.ui.data.history.widget.LayoutType3
import com.isuo.inspection.application.utils.DataUtil
import com.isuo.inspection.application.utils.EventObserver
import kotlinx.android.synthetic.main.fragment_history_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.MessageFormat
import java.util.*

class HistoryListFragment : BaseFragment<HistoryListDataBinding>() {

    var dataList = ArrayList<HistoryData>()

    var dataType1List = ArrayList<Type1Data>()
    var dataType2List = ArrayList<Type2Data>()
    var dataType3List = ArrayList<Type3Data>()

    private val viewModel by viewModels<HistoryListViewModel> { getViewModelFactory() }

    override fun lazyLoad() {

    }

    private fun getData() {
        if (viewModel.showChooseListView.value!!) {
            getChooseData()
        } else {
            getNormalData()
        }
    }

    private fun loadMoreData() {
        if (viewModel.showChooseListView.value!!) {
            getChooseMoreData()
        } else {
            getNormalMoreData()
        }
    }

    override fun getContentView(): Int {
        return R.layout.fragment_history_list
    }

    private var checkType: Int = 0
    private var deviceName: String? = null

    override fun initData() {
        checkType = arguments?.getInt(ConstantStr.KEY_BUNDLE_INT)!!
        deviceName = arguments?.getString(ConstantStr.KEY_BUNDLE_STR)
        viewModel.measuringName.value = arguments?.getString(ConstantStr.KEY_BUNDLE_STR_1)
        checkType = arguments?.getInt(ConstantStr.KEY_BUNDLE_INT)!!
        viewModel.deviceId = arguments?.getLong(ConstantStr.KEY_BUNDLE_LONG, -1L)!!
        if (checkType == 2) {
            viewModel.showMeasuringView.value = false
        }
        viewModel.checkType = checkType + 1
        viewModel.checkName.value = viewModel.nameList[checkType]
    }

    private var adapter1: Type1Adapter? = null
    private var adapter2: Type2Adapter? = null
    private var adapter3: Type3Adapter? = null

    override fun initView() {
        expandableListView.setOnGroupClickListener({ _, _, _, _ -> true }, false)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        when (checkType) {
            0 -> {
                adapter1 = Type1Adapter(
                    activity,
                    expandableListView,
                    R.layout.item_type_group,
                    R.layout.item_type_child_1
                )
                expandableListView.setAdapter(adapter1)
                val adapterList1 =
                    TypeList1Adapter(recyclerView, dataType1List, R.layout.item_type_child_1)
                recyclerView.adapter = adapterList1
            }
            1 -> {
                adapter2 = Type2Adapter(
                    activity,
                    expandableListView,
                    R.layout.item_type_group,
                    R.layout.item_type_child_2
                )
                expandableListView.setAdapter(adapter2)
                val adapterList2 =
                    TypeList2Adapter(recyclerView, dataType2List, R.layout.item_type_child_2)
                recyclerView.adapter = adapterList2
            }
            else -> {
                adapter3 = Type3Adapter(
                    activity,
                    expandableListView,
                    R.layout.item_type_group,
                    R.layout.item_type_child_3
                )
                expandableListView.setAdapter(adapter3)
                val adapterList3 =
                    TypeList3Adapter(recyclerView, dataType3List, R.layout.item_type_child_3)
                recyclerView.adapter = adapterList3
            }
        }
        refreshLayout.setOnRefreshListener {
            getData()
        }
        refreshLayout.setOnLoadMoreListener {
            loadMoreData()
        }
        viewModel.showResult.observe(this, EventObserver {
            adapter1?.setData(it)
            adapter2?.setData(it)
            adapter3?.setData(it)
            for (index in 0 until expandableListView.expandableListAdapter!!.groupCount) {
                expandableListView.expandGroup(index, false)
            }
            if (it.size < ConstantInt.PAGE_SIZE) {
                refreshLayout.finishRefreshWithNoMoreData()
            } else {
                refreshLayout.finishRefresh()
            }
        })
        viewModel.showMoreResult.observe(this, EventObserver {
            adapter1?.addData(it)
            adapter2?.addData(it)
            adapter3?.addData(it)
            for (index in 0 until expandableListView.expandableListAdapter!!.groupCount) {
                expandableListView.expandGroup(index, false)
            }
            if (it.size < ConstantInt.PAGE_SIZE) {
                refreshLayout.finishLoadMoreWithNoMoreData()
            } else {
                refreshLayout.finishLoadMore()
            }
        })
        refreshLayout.autoRefresh()
    }

    override fun setViewModel(dataBinding: HistoryListDataBinding?) {
        dataBinding?.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    fun onEventMainThread(message: MessageEvent) {
        if (message.message == ConstantStr.SEND_DATA) {
            viewModel.showChooseDate.value = message.startTime + "至" + message.endTime
            viewModel.showChooseListView.value = true
            message.positionId?.let {
                viewModel.positionId = it
            }
            dataList.clear()
            dataType1List.clear()
            dataType2List.clear()
            dataType3List.clear()
            refreshLayout.autoRefresh()
        }
    }

    private fun getChooseData() {
        when (checkType) {
            0 -> {
                viewModel.getType1Data().async(1000).bindLifeCycle(this).subscribe { list ->
                    this.dataType1List.clear()
                    this.dataType1List.addAll(list)
                    recyclerView.adapter?.notifyDataSetChanged()
                    if (list.size < ConstantInt.PAGE_SIZE) {
                        refreshLayout.finishRefreshWithNoMoreData()
                    } else {
                        refreshLayout.finishRefresh()
                    }
                }
            }
            1 -> {
                viewModel.getType2Data().async(1000).bindLifeCycle(this).subscribe { list ->
                    this.dataType2List.clear()
                    this.dataType2List.addAll(list)
                    recyclerView.adapter?.notifyDataSetChanged()
                    if (list.size < ConstantInt.PAGE_SIZE) {
                        refreshLayout.finishRefreshWithNoMoreData()
                    } else {
                        refreshLayout.finishRefresh()
                    }
                }
            }
            else -> {
                viewModel.getType3Data().async(1000).bindLifeCycle(this).subscribe { list ->
                    this.dataType3List.clear()
                    this.dataType3List.addAll(list)
                    recyclerView.adapter?.notifyDataSetChanged()
                    if (list.size < ConstantInt.PAGE_SIZE) {
                        refreshLayout.finishRefreshWithNoMoreData()
                    } else {
                        refreshLayout.finishRefresh()
                    }
                }
            }
        }
    }

    private fun getChooseMoreData() {
        when (checkType) {
            0 -> {
                viewModel.getType1Data().async(1000).bindLifeCycle(this).subscribe { list ->
                    this.dataType1List.addAll(list)
                    recyclerView.adapter?.notifyDataSetChanged()
                    if (list.size < ConstantInt.PAGE_SIZE) {
                        refreshLayout.finishLoadMoreWithNoMoreData()
                    } else {
                        refreshLayout.finishLoadMore()
                    }
                }
            }
            1 -> {
                viewModel.getType2Data().async(1000).bindLifeCycle(this).subscribe { list ->
                    this.dataType2List.addAll(list)
                    recyclerView.adapter?.notifyDataSetChanged()
                    if (list.size < ConstantInt.PAGE_SIZE) {
                        refreshLayout.finishLoadMoreWithNoMoreData()
                    } else {
                        refreshLayout.finishLoadMore()
                    }
                }
            }
            else -> {
                viewModel.getType3Data().async(1000).bindLifeCycle(this).subscribe { list ->
                    this.dataType3List.addAll(list)
                    recyclerView.adapter?.notifyDataSetChanged()
                    if (list.size < ConstantInt.PAGE_SIZE) {
                        refreshLayout.finishLoadMoreWithNoMoreData()
                    } else {
                        refreshLayout.finishLoadMore()
                    }
                }
            }
        }

    }

    private fun getNormalData() {
        viewModel.start()
    }

    private fun getNormalMoreData() {

    }

    class TypeList1Adapter(recyclerView: RecyclerView, data: ArrayList<Type1Data>?, layoutId: Int) :
        RVAdapter<Type1Data>(recyclerView, data, layoutId) {

        override fun showData(vHolder: ViewHolder?, data: Type1Data?, position: Int) {
            val text1: TextView? = vHolder?.getView(R.id.text_1) as TextView?
            val text2 = vHolder?.getView(R.id.text_2) as TextView?
            val text3 = vHolder?.getView(R.id.text_3) as TextView?
            val text4 = vHolder?.getView(R.id.text_4) as TextView?
            val time = vHolder?.getView(R.id.text_time) as TextView?
            text1?.text = MessageFormat.format("放电峰值:{0}", data?.value1)
            text2?.text = MessageFormat.format("背景峰值:{0}", data?.value2)
            text3?.text = MessageFormat.format("频率成分1:{0}", data?.value3)
            text4?.text = MessageFormat.format("频率成分2:{0}", data?.value4)
            time?.text = data?.time?.let { DataUtil.timeFormat(it, null) }
        }
    }

    class TypeList2Adapter(recyclerView: RecyclerView, data: ArrayList<Type2Data>?, layoutId: Int) :
        RVAdapter<Type2Data>(recyclerView, data, layoutId) {

        override fun showData(vHolder: ViewHolder?, data: Type2Data?, position: Int) {
            val text1: TextView? = vHolder?.getView(R.id.text_1) as TextView?
            val text2 = vHolder?.getView(R.id.text_2) as TextView?
            val text3 = vHolder?.getView(R.id.text_3) as TextView?
            val time = vHolder?.getView(R.id.text_time) as TextView?
            text1?.text = MessageFormat.format("放电峰值:{0}", data?.value1)
            text2?.text = MessageFormat.format("背景峰值:{0}", data?.value2)
            text3?.text = MessageFormat.format("图谱特征:{0}", data?.value3)
            time?.text = data?.time?.let { DataUtil.timeFormat(it, null) }
        }
    }

    class TypeList3Adapter(
        recyclerView: RecyclerView,
        data: ArrayList<Type3Data>?,
        layoutId: Int
    ) :
        RVAdapter<Type3Data>(recyclerView, data, layoutId) {

        override fun showData(vHolder: ViewHolder?, data: Type3Data?, position: Int) {
            val text1: TextView? = vHolder?.getView(R.id.text_1) as TextView?
            text1?.text = data?.time?.let { DataUtil.timeFormat(it, null) }
            val layout = vHolder?.getView(R.id.layout) as LinearLayout?
            layout?.removeAllViews()
            data?.let {
//                for (item in it.items) {
//                    val view = LayoutType3(ISUOApplication.instance)
//                    view.setData(item.positionText, item.value1, item.value2)
//                    layout?.addView(view)
//                }
            }
        }
    }

}