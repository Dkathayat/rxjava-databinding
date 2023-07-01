package com.yewapp.ui.modules.follower

import com.google.android.material.tabs.TabLayoutMediator
import com.yewapp.R
import com.yewapp.databinding.ActivityFollowerBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.follower.adapter.FollowerFragmentPagerAdapter
import com.yewapp.ui.modules.follower.navigator.FollowerNavigator
import com.yewapp.ui.modules.follower.vm.FollowerViewModel

class FollowerActivity :
    BaseActivity<FollowerNavigator, FollowerViewModel, ActivityFollowerBinding>(),
    FollowerNavigator {

    override fun getLayout(): Int = R.layout.activity_follower

    override fun init() {
        bind(FollowerViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: FollowerViewModel) {
        viewModel.setNavigator(this)

    }

    override fun onViewBound(viewDataBinding: ActivityFollowerBinding) {
        viewDataBinding.layoutfollower.viewPagerFollower.adapter =
            FollowerFragmentPagerAdapter(supportFragmentManager, lifecycle)
        bindViewPager()
    }

    override fun onResume() {
        super.onResume()
        viewDataBinding.layoutfollower.viewPagerFollower.currentItem =
            viewModel.position.get() ?: return
    }


    private fun bindViewPager() {
        TabLayoutMediator(
            viewDataBinding.layoutfollower.tabLayoutFollower,
            viewDataBinding.layoutfollower.viewPagerFollower
        ) { tab, position ->

            when (position) {
                0 -> tab.text = getString(R.string.my_followers)
                1 -> tab.text = getString(R.string.my_following)
            }
        }.attach()
    }


}