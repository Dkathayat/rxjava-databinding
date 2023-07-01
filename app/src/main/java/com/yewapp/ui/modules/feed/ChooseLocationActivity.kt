package com.yewapp.ui.modules.feed

import com.yewapp.R
import com.yewapp.databinding.ActivityChooseLocationBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.feed.navigator.ChooseLocationNavigator
import com.yewapp.ui.modules.feed.vm.ChooseLocationViewModel

class ChooseLocationActivity :
    BaseActivity<ChooseLocationNavigator, ChooseLocationViewModel, ActivityChooseLocationBinding>(),
    ChooseLocationNavigator {
    override fun getLayout(): Int = R.layout.activity_choose_location

    override fun init() {
        bind(ChooseLocationViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: ChooseLocationViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityChooseLocationBinding) {

    }
}