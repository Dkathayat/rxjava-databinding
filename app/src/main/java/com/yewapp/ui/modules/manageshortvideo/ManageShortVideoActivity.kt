package com.yewapp.ui.modules.manageshortvideo

import com.google.android.material.tabs.TabLayoutMediator
import com.yewapp.R
import com.yewapp.databinding.ActivityManageShortVideoBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.manageshortvideo.adapter.ManageReportedShortFragmentAdapter
import com.yewapp.ui.modules.manageshortvideo.navigator.ManageShortVideoNavigator
import com.yewapp.ui.modules.manageshortvideo.vm.ManageShortVideoViewModel

class ManageShortVideoActivity :
    BaseActivity<ManageShortVideoNavigator, ManageShortVideoViewModel, ActivityManageShortVideoBinding>(),
    ManageShortVideoNavigator {
    override fun getLayout(): Int {
        return R.layout.activity_manage_short_video
    }

    override fun init() {
        bind(ManageShortVideoViewModel::class.java)
    }

    override fun onViewBound(viewDataBinding: ActivityManageShortVideoBinding) {
        viewDataBinding.viewPager.adapter =
            ManageReportedShortFragmentAdapter(supportFragmentManager,lifecycle)
        viewDataBinding.viewPager.isUserInputEnabled = false
        bindViewPager()

    }

    override fun onViewModelCreated(viewModel: ManageShortVideoViewModel) {
        viewModel.setNavigator(this)
    }
    private fun bindViewPager(){
        TabLayoutMediator(
            viewDataBinding.tabLayout,
            viewDataBinding.viewPager
        ){tab,postion ->
            when(postion){
                0 -> tab.text = "Reported"
                1 -> tab.text = "Reported Comment"
            }
        }.attach()
    }

    override fun bindAdapter() {
        TODO("Not yet implemented")
    }
}