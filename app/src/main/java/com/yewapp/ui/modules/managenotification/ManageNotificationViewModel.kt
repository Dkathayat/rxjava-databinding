package com.yewapp.ui.modules.managenotification

import android.os.Bundle
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.utils.rx.SchedulerProvider

class ManageNotificationViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ManageNotificationNavigator>(dataManager, schedulerProvider) {
    override fun setData(extras: Bundle?) {
    }
}