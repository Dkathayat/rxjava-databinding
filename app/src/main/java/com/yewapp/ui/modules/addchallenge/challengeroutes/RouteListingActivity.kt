package com.yewapp.ui.modules.addchallenge.challengeroutes

import com.google.android.material.tabs.TabLayoutMediator
import com.yewapp.R
import com.yewapp.databinding.RouteListingActivityBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.addchallenge.challengeroutes.adapter.RouteListingTabsAdapter

class RouteListingActivity :
    BaseActivity<RoutesListingNavigator, RoutesListingViewModel, RouteListingActivityBinding>(),
    RoutesListingNavigator {

    override fun getLayout(): Int {
        return R.layout.route_listing_activity
    }

    override fun init() {
        bind(RoutesListingViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: RoutesListingViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: RouteListingActivityBinding) {

    }

    override fun navigateToRouteList(position: Int) {
        viewDataBinding.viewPagerRouteListing.adapter =
            RouteListingTabsAdapter(supportFragmentManager, lifecycle,viewModel.challengeModel)
        viewDataBinding.viewPagerRouteListing.isUserInputEnabled = false
        bindViewPager()
        viewDataBinding.viewPagerRouteListing.currentItem = position
    }

    private fun bindViewPager() {
        TabLayoutMediator(
            viewDataBinding.tabLayoutRouteListing,
            viewDataBinding.viewPagerRouteListing
        ) { tab, position ->
            when (position) {
                0 -> tab.text =
                    viewModel.dataManager.getResourceProvider().getString(R.string.popular)
                1 -> tab.text =
                    viewModel.dataManager.getResourceProvider().getString(R.string.latest)
                2 -> tab.text =
                    viewModel.dataManager.getResourceProvider().getString(R.string.favroites)
            }
        }.attach()
    }
}