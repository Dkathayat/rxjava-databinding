package com.yewapp.ui.modules.manageuser

import com.google.android.material.tabs.TabLayoutMediator
import com.yewapp.R
import com.yewapp.databinding.ManageUserActivityBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.manageuser.adapter.ManageUserFragmentsAdapter

class ManageUserActivity :
    BaseActivity<ManageUserNavigator, ManageUserViewModel, ManageUserActivityBinding>(),
    ManageUserNavigator {
    override fun getLayout(): Int {
        return R.layout.manage_user_activity
    }

    override fun init() {
        bind(ManageUserViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: ManageUserViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ManageUserActivityBinding) {
        viewDataBinding.viewPager.adapter =
            ManageUserFragmentsAdapter(supportFragmentManager, lifecycle)
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
                    viewModel.dataManager.getResourceProvider().getString(R.string.block)
                1 -> tab.text =
                    viewModel.dataManager.getResourceProvider().getString(R.string.favorite_manage_user)
                2 -> tab.text =
                    viewModel.dataManager.getResourceProvider().getString(R.string.reported)
                3 -> tab.text =
                    viewModel.dataManager.getResourceProvider().getString(R.string.muted)
            }
        }.attach()

    }

    override fun bindAdapter() {
        viewDataBinding.viewPager.adapter =
            ManageUserFragmentsAdapter(supportFragmentManager, lifecycle)
        viewDataBinding.viewPager.isUserInputEnabled = false
        bindViewPager()
    }
}