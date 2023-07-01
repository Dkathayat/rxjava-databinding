package com.yewapp.ui.modules.about

import com.yewapp.R
import com.yewapp.databinding.ActivityAboutBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.about.navigator.AboutNavigator
import com.yewapp.ui.modules.about.vm.AboutViewModel

class AboutActivity : BaseActivity<AboutNavigator, AboutViewModel, ActivityAboutBinding>(),
    AboutNavigator {
    override fun getLayout(): Int = R.layout.activity_about
    override fun init() {
        bind(AboutViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: AboutViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityAboutBinding) {

    }


}