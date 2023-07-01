package com.yewapp.ui.modules.managedevices

import com.yewapp.R
import com.yewapp.databinding.ActivityManageDevicesBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.about.CmsExtras
import com.yewapp.ui.modules.managedevices.extra.DeviceConnectInfoActivity
import com.yewapp.ui.modules.managedevices.navigator.ManageDevicesNavigator
import com.yewapp.ui.modules.managedevices.vm.ManageDevicesViewModel

class ManageDevicesActivity :
    BaseActivity<ManageDevicesNavigator, ManageDevicesViewModel, ActivityManageDevicesBinding>(),
    ManageDevicesNavigator {
    override fun getLayout(): Int {
        return R.layout.activity_manage_devices
    }

    override fun init() {
        bind(ManageDevicesViewModel::class.java)
    }

    override fun onViewBound(viewDataBinding: ActivityManageDevicesBinding) {

    }

    override fun onViewModelCreated(viewModel: ManageDevicesViewModel) {
        viewModel.setNavigator(this)
    }


    override fun navigateToGarmin() {
        startActivity(
            intentProviderFactory.create(
                DeviceConnectInfoActivity::class.java,
                CmsExtras.cmsExtras {
                    link = "GarminDevice"
                },
                0
            )
        )
    }

    override fun navigateToWearOs() {
        startActivity(
            intentProviderFactory.create(
                DeviceConnectInfoActivity::class.java,
                CmsExtras.cmsExtras {
                    link = "GoogleDevice"
                },
                0
            )
        )
    }
}