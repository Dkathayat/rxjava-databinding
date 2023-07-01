package com.yewapp.ui.modules.subscription

import com.google.android.material.tabs.TabLayoutMediator
import com.yewapp.R
import com.yewapp.databinding.ActivitySubscriptionBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.subscription.adapter.SubscriptionFragmentsAdapter
import com.yewapp.ui.modules.subscription.navigator.SubscriptionNavigator
import com.yewapp.ui.modules.subscription.vm.SubscriptionViewModel

class SubscriptionActivity :
    BaseActivity<SubscriptionNavigator, SubscriptionViewModel, ActivitySubscriptionBinding>(),
    SubscriptionNavigator {

    var test = ""
    override fun getLayout(): Int {
        return R.layout.activity_subscription
    }

    override fun init() {
        bind(SubscriptionViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: SubscriptionViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivitySubscriptionBinding) {
        viewDataBinding.viewPager.adapter =
            SubscriptionFragmentsAdapter(supportFragmentManager, lifecycle)
        viewDataBinding.viewPager.isUserInputEnabled = false
        bindViewPager()
    }

    fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        viewDataBinding.viewPager.setCurrentItem(item, smoothScroll)
    }
    private fun bindViewPager() {
        TabLayoutMediator(
            viewDataBinding.tabLayout,
            viewDataBinding.viewPager
        ) { tab, position ->
            when (position) {
                0 -> tab.text =
                    viewModel.dataManager.getResourceProvider().getString(R.string.dashboard)
                1 -> tab.text =
                    viewModel.dataManager.getResourceProvider().getString(R.string.pricing_plans)

            }
        }.attach()

    }


    override fun bindAdapter() {
        viewDataBinding.viewPager.adapter =
            SubscriptionFragmentsAdapter(supportFragmentManager, lifecycle)
        viewDataBinding.viewPager.isUserInputEnabled = false
        bindViewPager()
    }
}