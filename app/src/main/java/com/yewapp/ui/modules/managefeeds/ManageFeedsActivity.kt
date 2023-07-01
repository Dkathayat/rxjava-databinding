package com.yewapp.ui.modules.managefeeds

import com.google.android.material.tabs.TabLayoutMediator
import com.yewapp.R
import com.yewapp.databinding.ManageFeedsActivityBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.managefeeds.adapter.ManageFeedsFragmentsAdapter

class ManageFeedsActivity :
    BaseActivity<ManageFeedsNavigator, ManageFeedViewModel, ManageFeedsActivityBinding>(),
    ManageFeedsNavigator {
    override fun getLayout(): Int {
        return R.layout.manage_feeds_activity
    }

    override fun init() {
        bind(ManageFeedViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: ManageFeedViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ManageFeedsActivityBinding) {
        viewDataBinding.viewPager.adapter =
            ManageFeedsFragmentsAdapter(supportFragmentManager, lifecycle)
        viewDataBinding.viewPager.isUserInputEnabled = false
        bindViewPager()

    }

    private fun bindViewPager() {
        TabLayoutMediator(
            viewDataBinding.tabLayout,
            viewDataBinding.viewPager
        ) { tab, postion ->
            when (postion) {
                0 -> tab.text = "Reported"
                1 -> tab.text = "Reported Comment"
            }
        }.attach()
    }


}