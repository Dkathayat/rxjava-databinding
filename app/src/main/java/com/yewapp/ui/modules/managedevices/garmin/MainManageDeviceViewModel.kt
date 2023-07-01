package com.yewapp.ui.modules.managedevices.garmin

import android.os.Bundle
import android.util.Log
import androidx.databinding.ObservableField
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.about.vm.OnWebPageError
import com.yewapp.ui.modules.about.vm.OnWebPageLoad
import com.yewapp.utils.rx.SchedulerProvider

class MainManageDeviceViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<MainManageDevicesNavigator>(dataManager, schedulerProvider), OnWebPageLoad, OnWebPageError {
    var url = ObservableField<String>("")

    init {
        loadGarminUrl()
    }

    override fun setData(extras: Bundle?) {


    }

    override fun onWebPageLoaded() {
        isLoading.set(false)

    }

    override fun onWebPageError(message: String?) {
        isLoading.set(false)
    }

    private fun loadGarminUrl() {
        if (isLoading.get()) return
        isLoading.set(true)
        val userID = dataManager.getUser().userId
        Log.d("UserIDGRAmim",userID.toString())
        url.set("https://www.demo2.newmediaguru.co/garminconnect/?user_id=$userID")
    }
}