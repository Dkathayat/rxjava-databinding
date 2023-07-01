package com.yewapp.ui.modules.dashboard.fragment.feeds.vm

import android.os.Bundle
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.dashboard.fragment.feeds.navigator.FeedsNavigator
import com.yewapp.utils.rx.SchedulerProvider

class FeedsViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<FeedsNavigator>(dataManager, schedulerProvider) {

    override fun setData(extras: Bundle?) {

    }
}