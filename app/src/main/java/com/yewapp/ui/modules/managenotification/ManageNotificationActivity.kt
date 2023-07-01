package com.yewapp.ui.modules.managenotification

import com.google.android.material.tabs.TabLayoutMediator
import com.yewapp.R
import com.yewapp.databinding.ManageNotificationsActivityBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.managenotification.adapter.ManageNotificationFragmentsAdapter

class ManageNotificationActivity :
    BaseActivity<ManageNotificationNavigator, ManageNotificationViewModel, ManageNotificationsActivityBinding>(),
    ManageNotificationNavigator {
    override fun getLayout(): Int {
        return R.layout.manage_notifications_activity
    }

    override fun init() {
        bind(ManageNotificationViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: ManageNotificationViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ManageNotificationsActivityBinding) {
        viewDataBinding.viewPager.adapter =
            ManageNotificationFragmentsAdapter(supportFragmentManager, lifecycle)
        viewDataBinding.viewPager.isUserInputEnabled = false
        bindViewPager()
    }

    private fun bindViewPager() {
        TabLayoutMediator(
            viewDataBinding.tabLayout,
            viewDataBinding.viewPager
        ) { tab, position ->
            when (position) {
                0 -> tab.text =
                    viewModel.dataManager.getResourceProvider().getString(R.string.notifications)
                1 -> tab.text =
                    viewModel.dataManager.getResourceProvider().getString(R.string.settings)

            }
        }.attach()

    }

    override fun bindAdapter() {
        viewDataBinding.viewPager.adapter =
            ManageNotificationFragmentsAdapter(supportFragmentManager, lifecycle)
        viewDataBinding.viewPager.isUserInputEnabled = false
        bindViewPager()
    }
}