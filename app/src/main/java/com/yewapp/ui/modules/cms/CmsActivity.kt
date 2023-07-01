package com.yewapp.ui.modules.cms

import com.yewapp.R
import com.yewapp.databinding.ActivityCmsBinding
import com.yewapp.ui.base.BaseActivity

class CmsActivity : BaseActivity<CmsNavigator, CmsViewModel, ActivityCmsBinding>(), CmsNavigator {
    override fun getLayout(): Int = R.layout.activity_cms

    override fun init() {
        bind(CmsViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: CmsViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityCmsBinding) {

    }
}