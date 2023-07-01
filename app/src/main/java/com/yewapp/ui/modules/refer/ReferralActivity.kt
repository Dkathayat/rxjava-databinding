package com.yewapp.ui.modules.refer

import com.google.android.material.tabs.TabLayoutMediator
import com.yewapp.R
import com.yewapp.databinding.ActivityReferralBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.refer.adapter.ReferFragmentPagerAdapter
import com.yewapp.ui.modules.refer.navigator.ReferralNavigator
import com.yewapp.ui.modules.refer.vm.ReferralViewModel

class ReferralActivity :
    BaseActivity<ReferralNavigator, ReferralViewModel, ActivityReferralBinding>(),
    ReferralNavigator {

    override fun getLayout(): Int = R.layout.activity_referral

    override fun init() {
        bind(ReferralViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: ReferralViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityReferralBinding) {
        viewDataBinding.viewPagerRefer.adapter =
            ReferFragmentPagerAdapter(supportFragmentManager, lifecycle)
        viewDataBinding.viewPagerRefer.offscreenPageLimit = 1
        viewDataBinding.viewPagerRefer.isUserInputEnabled = false// stop scrolling
        bindViewPager()
    }

    private fun bindViewPager() {
        TabLayoutMediator(
            viewDataBinding.tabLayoutReferFriend,
            viewDataBinding.viewPagerRefer
        ) { tab, position ->

            when (position) {
                0 -> tab.text = getString(R.string.choose_contacts)
                1 -> tab.text = getString(R.string.referred_friends)
                2 -> tab.text = getString(R.string.history)
            }
        }.attach()
    }


    override fun onSuccess() {
    }
}