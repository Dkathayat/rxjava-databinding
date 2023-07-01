package com.yewapp.ui.modules.feed.vm

import android.os.Bundle
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.feed.navigator.ChooseLocationNavigator
import com.yewapp.utils.rx.SchedulerProvider

class ChooseLocationViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ChooseLocationNavigator>(dataManager, schedulerProvider) {

    override fun setData(extras: Bundle?) {

    }
}