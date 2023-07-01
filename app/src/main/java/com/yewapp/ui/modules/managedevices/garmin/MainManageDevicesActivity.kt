package com.yewapp.ui.modules.managedevices.garmin

import com.yewapp.R
import com.yewapp.databinding.ActivityGarminBinding
import com.yewapp.ui.base.BaseActivity

class MainManageDevicesActivity : BaseActivity<MainManageDevicesNavigator, MainManageDeviceViewModel, ActivityGarminBinding>(),
    MainManageDevicesNavigator {
    override fun getLayout(): Int = R.layout.activity_garmin

    override fun init() {
        bind(MainManageDeviceViewModel::class.java)
    }

    override fun onViewBound(viewDataBinding: ActivityGarminBinding) {
       val webview = viewDataBinding.aboutContent.settings
        webview.domStorageEnabled = true
        webview.javaScriptEnabled = true
        webview.allowFileAccess = true
        webview.javaScriptCanOpenWindowsAutomatically = true

    }

    override fun onViewModelCreated(viewModel: MainManageDeviceViewModel) {
        viewModel.setNavigator(this)
    }
}