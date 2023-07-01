package com.yewapp.ui.modules.manageuser

import android.os.Bundle
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.utils.rx.SchedulerProvider

class ManageUserViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ManageUserNavigator>(dataManager, schedulerProvider) {
    override fun setData(extras: Bundle?) {
    }
}