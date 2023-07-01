package com.yewapp.ui.modules.profile.vm

import android.os.Bundle
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.profile.navigator.MainProfileNavigator
import com.yewapp.utils.rx.SchedulerProvider

class MainProfileViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<MainProfileNavigator>(dataManager, schedulerProvider) {

    override fun setData(extras: Bundle?) {

    }


}