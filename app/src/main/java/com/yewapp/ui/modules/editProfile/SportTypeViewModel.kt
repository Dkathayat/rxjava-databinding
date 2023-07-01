package com.yewapp.ui.modules.editProfile

import android.os.Bundle
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.utils.rx.SchedulerProvider

class SportTypeViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<SportTypeNavigator>(dataManager, schedulerProvider) {

    override fun setData(extras: Bundle?) {

    }
}