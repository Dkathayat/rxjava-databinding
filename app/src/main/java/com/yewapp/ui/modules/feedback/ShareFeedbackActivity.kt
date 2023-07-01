package com.yewapp.ui.modules.feedback

import com.google.android.material.tabs.TabLayoutMediator
import com.yewapp.R
import com.yewapp.databinding.ActivitySharefeedbackBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.base.BaseNavigator
import com.yewapp.ui.modules.feedback.adapter.ShareFeedbackAdapter
import com.yewapp.ui.modules.feedback.navigator.ShareFeedbackNavigator
import com.yewapp.ui.modules.feedback.vm.SharedFeedbackViewModel
import com.yewapp.ui.modules.manageuser.adapter.ManageUserFragmentsAdapter

class ShareFeedbackActivity :
    BaseActivity<ShareFeedbackNavigator, SharedFeedbackViewModel, ActivitySharefeedbackBinding>(),
    ShareFeedbackNavigator {
    override fun getLayout(): Int {
        return R.layout.activity_sharefeedback
    }

    override fun init() {
        bind(SharedFeedbackViewModel::class.java)
    }

    override fun onViewBound(viewDataBinding: ActivitySharefeedbackBinding) {
        viewDataBinding.viewPager.adapter =
            ShareFeedbackAdapter(supportFragmentManager, lifecycle)
        viewDataBinding.viewPager.isUserInputEnabled = false
        viewDataBinding.viewPager.isSaveFromParentEnabled = false
        bindViewPager()
    }

    override fun onViewModelCreated(viewModel: SharedFeedbackViewModel) {
        viewModel.setNavigator(this)
    }
    private fun bindViewPager() {
        TabLayoutMediator(
            viewDataBinding.tabLayout,
            viewDataBinding.viewPager
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "Feedback"
                1 -> tab.text = "Bug Fixes"

            }
        }.attach()

    }
}