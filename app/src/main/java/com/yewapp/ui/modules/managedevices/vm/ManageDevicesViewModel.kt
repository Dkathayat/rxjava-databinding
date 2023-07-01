package com.yewapp.ui.modules.managedevices.vm

import android.os.Bundle
import android.view.View
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.managedevices.navigator.ManageDevicesNavigator
import com.yewapp.utils.rx.SchedulerProvider

class ManageDevicesViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ManageDevicesNavigator>(dataManager, schedulerProvider) {
    override fun setData(extras: Bundle?) {

    }

    fun deviceClicked(view:View) {
        when(view.id){
            R.id.text_garmin -> {
                getNavigator()?.navigateToGarmin()
            }
            R.id.text_google -> {
                getNavigator()?.navigateToWearOs()
            }
        }
    }
}