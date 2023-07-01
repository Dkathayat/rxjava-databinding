package com.yewapp.ui.modules.editProfile

import com.yewapp.R
import com.yewapp.databinding.ActivitySportTypeBinding
import com.yewapp.ui.base.BaseActivity

class SportTypeActivity :
    BaseActivity<SportTypeNavigator, SportTypeViewModel, ActivitySportTypeBinding>(),
    SportTypeNavigator {
    override fun getLayout(): Int = R.layout.activity_sport_type

    override fun init() {
        bind(SportTypeViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: SportTypeViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivitySportTypeBinding) {

    }
}