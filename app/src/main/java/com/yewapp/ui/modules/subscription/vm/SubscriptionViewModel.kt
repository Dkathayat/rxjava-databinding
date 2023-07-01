package com.yewapp.ui.modules.subscription.vm

import android.os.Bundle
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.subscription.navigator.SubscriptionNavigator
import com.yewapp.utils.rx.SchedulerProvider

class SubscriptionViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<SubscriptionNavigator>(dataManager, schedulerProvider) {
    override fun setData(extras: Bundle?) {
    }
}