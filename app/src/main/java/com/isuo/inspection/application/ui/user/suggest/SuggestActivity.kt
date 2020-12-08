package com.isuo.inspection.application.ui.user.suggest

import android.os.Bundle
import androidx.activity.viewModels
import com.isuo.inspection.application.R
import com.isuo.inspection.application.base.AbsBaseActivity
import com.isuo.inspection.application.base.ext.getViewModelFactory
import com.isuo.inspection.application.databinding.SuggestDataBinding
import com.isuo.inspection.application.utils.EventObserver

class SuggestActivity : AbsBaseActivity<SuggestDataBinding>() {

    private val viewModel by viewModels<SuggestViewModel> { getViewModelFactory() }

    override fun getToolBarTitle(): String? {
        return "意见反馈"
    }

    override fun initView(savedInstanceState: Bundle?) {

        viewModel.toSub.observe(this, EventObserver {
            finish()
        })
    }

    override fun initData(savedInstanceState: Bundle?) {
        dataBinding.viewModel = viewModel
    }

    override fun getContentView(): Int {
        return R.layout.activity_suggest
    }

}