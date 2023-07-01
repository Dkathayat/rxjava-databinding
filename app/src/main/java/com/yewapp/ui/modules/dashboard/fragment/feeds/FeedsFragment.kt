package com.yewapp.ui.modules.dashboard.fragment.feeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.yewapp.R
import com.yewapp.databinding.FragmentFeedsBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.dashboard.fragment.feeds.adapter.FeedsPagerAdapter
import com.yewapp.ui.modules.dashboard.fragment.feeds.navigator.FeedsNavigator
import com.yewapp.ui.modules.dashboard.fragment.feeds.vm.FeedsViewModel

class FeedsFragment(override val layoutId: Int = R.layout.fragment_feeds) :
    BaseFragment<FeedsNavigator, FeedsViewModel, FragmentFeedsBinding>(), FeedsNavigator {

    override fun onViewModelCreated(viewModel: FeedsViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: FragmentFeedsBinding) {
        viewDataBinding.layoutFeeds.viewPager.adapter =
            FeedsPagerAdapter(childFragmentManager, lifecycle)
        viewDataBinding.layoutFeeds.viewPager.isUserInputEnabled = false
        bindViewPager()
    }

    private fun bindViewPager() {
        TabLayoutMediator(
            viewDataBinding.layoutFeeds.tabLayout,
            viewDataBinding.layoutFeeds.viewPager
        ) { tab, position ->

            when (position) {
                0 -> tab.text = activity?.getString(R.string.all_feeds)
                1 -> tab.text = activity?.getString(R.string.my_feeds)
            }
        }.attach()
    }

    override fun onBackPress() {

    }

    //
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return bind(FeedsViewModel::class.java, inflater, container)
    }
}