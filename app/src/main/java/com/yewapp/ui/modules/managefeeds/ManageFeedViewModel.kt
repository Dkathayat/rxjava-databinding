package com.yewapp.ui.modules.managefeeds

import android.os.Bundle
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.utils.rx.SchedulerProvider

class ManageFeedViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ManageFeedsNavigator>(dataManager, schedulerProvider) {

    override fun setData(extras: Bundle?) {
    }
}
