package com.isuo.inspection.application.ui.data

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.viewpager.widget.ViewPager
import com.isuo.inspection.application.R
import com.isuo.inspection.application.base.AbsBaseActivity
import com.isuo.inspection.application.base.ext.getViewModelFactory
import com.isuo.inspection.application.common.ConstantStr
import com.isuo.inspection.application.databinding.DataBaseDataBinding
import com.isuo.inspection.application.model.bean.MessageChartEvent
import com.isuo.inspection.application.model.bean.MessageEvent
import com.isuo.inspection.application.ui.data.chart.ChartFragment
import com.isuo.inspection.application.ui.data.filter.DataFilterActivity
import com.isuo.inspection.application.ui.data.history.HistoryListFragment
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_data_base.*
import org.greenrobot.eventbus.EventBus

class DataBaseActivity : AbsBaseActivity<DataBaseDataBinding>() {

    private val viewModel by viewModels<DataBaseViewModel> { getViewModelFactory() }

    private var deviceName: String? = null
    private var inputType: Int = 0
    var deviceId: Long = 0L
    var checkPosition: String? = null

    override fun getToolBarTitle(): String? {
        return deviceName
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_filter) {
            val intent = Intent(this, DataFilterActivity::class.java)
            intent.putExtra(ConstantStr.KEY_BUNDLE_INT, inputType)
            startActivityForResult(intent, 10001)
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 10001) {
            val msg = MessageEvent()
            val startTime = data?.getStringExtra(ConstantStr.KEY_BUNDLE_STR)
            val endTime = data?.getStringExtra(ConstantStr.KEY_BUNDLE_STR_1)
            val positionId = data?.getIntExtra(ConstantStr.KEY_BUNDLE_INT, -1)
            if (positionId == -1) {
                msg.MessageEvent(ConstantStr.SEND_DATA, startTime, endTime, null)
            } else {
                msg.MessageEvent(ConstantStr.SEND_DATA, startTime, endTime, positionId)
            }
            EventBus.getDefault().postSticky(msg)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        val bundle1 = Bundle()
        bundle1.putString(ConstantStr.KEY_BUNDLE_STR, deviceName)
        bundle1.putString(ConstantStr.KEY_BUNDLE_STR_1, "xxxxx")
        bundle1.putInt(ConstantStr.KEY_BUNDLE_INT, inputType)
        bundle1.putLong(ConstantStr.KEY_BUNDLE_LONG, deviceId)

        val adapter = FragmentPagerItemAdapter(
            supportFragmentManager
            , FragmentPagerItems.with(this)
                .add(
                    getString(R.string.history_tabs_name),
                    HistoryListFragment::class.java,
                    bundle1
                )
                .add(
                    getString(R.string.chart_tabs_name),
                    ChartFragment::class.java,
                    bundle1
                )
                .create()
        )
        viewpager.adapter = adapter
        tabs.setViewPager(viewpager)
        val textView1 = tabs.getTabAt(0).findViewById<TextView>(R.id.id_tv_title)
        val textView2 = tabs.getTabAt(1).findViewById<TextView>(R.id.id_tv_title)
        textView1.setTextColor(findColor(R.color.text_title))
        textView2.setTextColor(findColor(R.color.text_grey))
        tabs.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    textView1.setTextColor(findColor(R.color.text_title))
                    textView2.setTextColor(findColor(R.color.text_grey))
                } else {
                    textView1.setTextColor(findColor(R.color.text_grey))
                    textView2.setTextColor(findColor(R.color.text_title))
                }
            }
        })
    }

    override fun initData(savedInstanceState: Bundle?) {
        dataBinding.viewModel = viewModel
        deviceName = intent.getStringExtra(ConstantStr.KEY_BUNDLE_STR)
        checkPosition = intent.getStringExtra(ConstantStr.KEY_BUNDLE_STR_1)
        inputType = intent.getIntExtra(ConstantStr.KEY_BUNDLE_INT, -1)
        deviceId = intent.getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1L)
    }

    override fun getContentView(): Int {
        return R.layout.activity_data_base
    }
}