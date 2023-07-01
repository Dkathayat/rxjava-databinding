package com.yewapp.ui.modules.addassociatememberdetails

import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.LinearLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jakewharton.rxbinding2.view.clickable
import com.yewapp.R
import com.yewapp.databinding.ActivityAddMemberDetailsBinding
import com.yewapp.ui.base.BaseActivity

class AddMembersDetailsActivity :
    BaseActivity<AddMembersDetailsNavigator, AddMembersDetailsViewModel, ActivityAddMemberDetailsBinding>(),
    AddMembersDetailsNavigator {

    override fun getLayout() = R.layout.activity_add_member_details

    override fun init() {
        bind(AddMembersDetailsViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: AddMembersDetailsViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityAddMemberDetailsBinding) {
        adapterInitialize()
    }


    private fun adapterInitialize() {
        viewDataBinding.associateViewPager.adapter =
            AddMembersDetailsAdapter(supportFragmentManager, lifecycle, viewModel.extras)
        viewDataBinding.associateViewPager.isUserInputEnabled = false// stop scrolling
        bindViewPager()

    }

    private fun bindViewPager() {
        TabLayoutMediator(
            viewDataBinding.tabLayout, viewDataBinding.associateViewPager
        ) { tab, position ->

            when (position) {
                0 -> tab.text = getString(R.string.profile_details)
                1 -> tab.text = getString(R.string.sport_images)
            }
        }.attach()
    }

    override fun updateTabLayout(position: Int, enable: Boolean) {
        viewDataBinding.tabLayout.getTabAt(position)?.view?.isEnabled = enable
    }
}