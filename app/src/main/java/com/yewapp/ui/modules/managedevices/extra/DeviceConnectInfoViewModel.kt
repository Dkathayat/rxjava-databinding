package com.yewapp.ui.modules.managedevices.extra

import android.os.Bundle
import android.view.View
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.about.CmsExtras
import com.yewapp.utils.rx.SchedulerProvider

class DeviceConnectInfoViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<DeviceConnectInfoNavigator>(dataManager, schedulerProvider) {
    var deviceType: String? = null
    override fun setData(extras: Bundle?) {
        if (extras != null) {
            deviceType = extras.getString(CmsExtras.LINK)
        }

    }

    fun onButtonClick(view: View) {
        when (deviceType) {
            "GarminDevice" -> {
                getNavigator()?.navigateToGarmin()
            }
            "GoogleDevice" -> {
                getNavigator()?.navigateToGoogleWearOs()
            }
        }
    }

    private fun setDataView() {

    }
}