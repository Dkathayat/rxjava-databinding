package com.yewapp.ui.modules.brandmodel

import com.yewapp.R
import com.yewapp.databinding.BrandActivityBinding
import com.yewapp.ui.base.BaseActivity

class BrandActivity : BaseActivity<BrandNavigator, BrandViewModel, BrandActivityBinding>(),
    BrandNavigator {
    override fun getLayout(): Int {
        return R.layout.brand_activity
    }

    override fun init() {
        bind(BrandViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: BrandViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: BrandActivityBinding) {
    }
}