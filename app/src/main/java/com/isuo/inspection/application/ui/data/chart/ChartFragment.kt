package com.isuo.inspection.application.ui.data.chart

import android.content.Context
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.isuo.inspection.application.R
import com.isuo.inspection.application.base.BaseFragment
import com.isuo.inspection.application.base.ext.async
import com.isuo.inspection.application.base.ext.bindLifeCycle
import com.isuo.inspection.application.base.ext.getViewModelFactory
import com.isuo.inspection.application.common.ConstantInt
import com.isuo.inspection.application.common.ConstantStr
import com.isuo.inspection.application.databinding.ChartDataBinding
import com.isuo.inspection.application.model.bean.ChartBean
import com.isuo.inspection.application.model.bean.MessageEvent
import com.isuo.inspection.application.ui.data.DataBaseActivity
import com.isuo.inspection.application.ui.data.chart.widget.ChartXFormatter
import com.isuo.inspection.application.utils.ChartLabelUtils
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_chart.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ChartFragment : BaseFragment<ChartDataBinding>() {

    private val viewModel by viewModels<ChartViewModel> { getViewModelFactory() }

    override fun lazyLoad() {

    }

    override fun getContentView(): Int {
        return R.layout.fragment_chart
    }

    private var deviceName: String? = null
    private var inputType: Int = 0
    var deviceId: Long = 0L
    var checkPosition: String? = null

    override fun initData() {
        viewModel.requestState.value = ConstantInt.REQUEST_STATE_EMPTY
        inputType = arguments?.getInt(ConstantStr.KEY_BUNDLE_INT)!!
        deviceName = arguments?.getString(ConstantStr.KEY_BUNDLE_STR)
        checkPosition = arguments?.getString(ConstantStr.KEY_BUNDLE_STR_1)
        inputType = arguments?.getInt(ConstantStr.KEY_BUNDLE_INT)!!
        deviceId = arguments?.getLong(ConstantStr.KEY_BUNDLE_LONG, -1L)!!
        if (inputType == 2) {
            viewModel.showPositionView.value = false
        }
        viewModel.checkPosition.value = checkPosition
        viewModel.checkType.value = viewModel.nameList[inputType]
    }

    override fun initView() {

    }

    override fun setViewModel(dataBinding: ChartDataBinding?) {
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
    fun onEventChartMainThread(message: MessageEvent) {
        if (message.message == ConstantStr.SEND_DATA) {
            viewModel.showChooseDate.value = message.startTime + "至" + message.endTime
            message.positionId?.let {
                viewModel.positionId = it
            }
            viewModel.start(inputType).async(1000).bindLifeCycle(this).subscribe { list ->
                val chartListData = ArrayList<ChartBean>()
                when (inputType) {
                    0 -> {
                        val chart1 = ChartBean()
                        val chart2 = ChartBean()
                        val chart3 = ChartBean()
                        val chart4 = ChartBean()

                        val list1 = ArrayList<ChartBean.Data>()
                        val list2 = ArrayList<ChartBean.Data>()
                        val list3 = ArrayList<ChartBean.Data>()
                        val list4 = ArrayList<ChartBean.Data>()

                        if (list.size == 1) {
                            for (item in list[0].type1DataList!!) {
                                val data1 = ChartBean.Data()
                                data1.value = item.time
                                data1.dataValue = item.value1.toFloat()
                                list1.add(data1)
                                val data2 = ChartBean.Data()
                                data2.value = item.time
                                data2.dataValue = item.value2.toFloat()
                                list2.add(data2)
                                val data3 = ChartBean.Data()
                                data3.value = item.time
                                data3.dataValue = item.value3.toFloat()
                                list3.add(data3)
                                val data4 = ChartBean.Data()
                                data4.value = item.time
                                data4.dataValue = item.value4.toFloat()
                                list4.add(data4)
                            }

                            chart1.data = list1
                            chart2.data = list2
                            chart3.data = list3
                            chart4.data = list4

                            chartListData.add(chart1)
                            chartListData.add(chart2)
                            chartListData.add(chart3)
                            chartListData.add(chart4)
                        }
                    }
                    1 -> {
                        val chart1 = ChartBean()
                        val chart2 = ChartBean()

                        val list1 = ArrayList<ChartBean.Data>()
                        val list2 = ArrayList<ChartBean.Data>()

                        if (list.size == 1) {
                            for (item in list[0].type2DataList!!) {
                                val data1 = ChartBean.Data()
                                data1.value = item.time
                                data1.dataValue = item.value1.toFloat()
                                list1.add(data1)
                                val data2 = ChartBean.Data()
                                data2.value = item.time
                                data2.dataValue = item.value2.toFloat()
                                list2.add(data2)
                            }

                            chart1.data = list1
                            chart2.data = list2

                            chartListData.add(chart1)
                            chartListData.add(chart2)
                        }
                    }
                    else -> {
                        val chart1 = ChartBean()
                        val chart2 = ChartBean()

                        val list1 = ArrayList<ChartBean.Data>()
                        val list2 = ArrayList<ChartBean.Data>()

                        if (list.size == 1) {
                            for (item in list[0].type3DataList!!) {
                                val data1 = ChartBean.Data()
                                data1.dataValue = item.items[0].value1.toFloat()
                                data1.value = item.time
                                list1.add(data1)
                                val data2 = ChartBean.Data()
                                data2.dataValue = item.items[0].value2.toFloat()
                                data2.value = item.time
                                list2.add(data2)
                            }

                            chart1.data = list1
                            chart2.data = list2

                            chartListData.add(chart1)
                            chartListData.add(chart2)
                        }
                    }
                }
                showData(chartListData)
            }
        }
    }

    private var colors = intArrayOf(
        R.color.line_chart_color_1, R.color.line_chart_color_2
        , R.color.line_chart_color_3, R.color.line_chart_color_4
    )

    private fun showData(chartDataList: List<ChartBean>) {
        initLineChart(lineChart, chartDataList)
        lineChart.data = getLineData(chartDataList, colors)
        viewModel.requestState.value = ConstantInt.REQUEST_STATE_DATA
        viewModel.showChartView.value = true
    }


    private fun initLineChart(
        lineChart: LineChart, dataList: List<ChartBean>
    ) {
        lineChart.clear()
        lineChart.description = null
        lineChart.setNoDataText("")
        lineChart.alpha = 1f
        lineChart.setTouchEnabled(true)
        lineChart.isDragEnabled = false
        lineChart.setScaleEnabled(false)
        lineChart.setPinchZoom(false)
        lineChart.legend.isEnabled = true
        val legen = lineChart.legend
        legen.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legen.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        //x
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.xAxis.textSize = 10f
        lineChart.xAxis.setLabelCount(5, false)
        lineChart.xAxis.textColor = findColor(R.color.text_content)
        lineChart.xAxis.setDrawAxisLine(true)
        lineChart.xAxis.setDrawGridLines(true)
        lineChart.xAxis.axisLineColor = findColor(R.color.chart_line)
        lineChart.xAxis.valueFormatter = ChartXFormatter(dataList)
        //左边Y
        lineChart.axisLeft.setDrawGridLines(true)
        lineChart.axisLeft.setDrawAxisLine(true)
        lineChart.axisLeft.setDrawLabels(true)
        lineChart.axisLeft.textSize = 12f
        lineChart.axisLeft.setDrawZeroLine(false)
        lineChart.axisLeft.textColor = findColor(R.color.text_content)
        lineChart.axisLeft.axisLineColor = findColor(R.color.chart_line)
        //右边Y
        lineChart.axisRight.isEnabled = false
    }

    private fun getLineData(
        chartDataList: List<ChartBean>,
        color: IntArray
    ): LineData? {
        val dataSets: MutableList<ILineDataSet> =
            ArrayList()
        val utils = ChartLabelUtils()
        for (i in chartDataList.indices) {
            val entries: MutableList<Entry> =
                ArrayList()
            for (j in 0 until chartDataList[i].data.size) {
                entries.add(
                    Entry(
                        j.toFloat(),
                        chartDataList[i].data[j].dataValue
                    )
                )
            }
            val dataSet = LineDataSet(entries, utils.getLabel(inputType, i))
            initDataSet(dataSet, color[i])
            dataSets.add(dataSet)
        }
        return LineData(dataSets)
    }


    private fun initDataSet(dataSet: LineDataSet, @ColorInt color: Int) {
        dataSet.lineWidth = 2.0f
        dataSet.color = findColor(color)
        dataSet.setCircleColor(findColor(color))
        dataSet.circleRadius = 2.5f
        dataSet.setDrawCircleHole(false)
        dataSet.setDrawValues(false)
        dataSet.setDrawCircles(true)
        dataSet.mode = LineDataSet.Mode.LINEAR
        dataSet.highLightColor = findColor(R.color.colorTransparent)
    }
}