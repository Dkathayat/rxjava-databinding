package com.yewapp.ui.modules.follower.vm

import android.os.Bundle
import androidx.databinding.ObservableField
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.follower.FollowerExtras
import com.yewapp.ui.modules.follower.navigator.FollowerNavigator
import com.yewapp.utils.rx.SchedulerProvider

class FollowerViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<FollowerNavigator>(dataManager, schedulerProvider) {


    var userName = ObservableField<String>()
    var stateAndCountry = ObservableField<String>()
    var position = ObservableField<Int>(0)
    override fun setData(extras: Bundle?) {
        position.set(extras?.getInt(FollowerExtras.NAVIGATE_POSITION, 0))
    }

    init {
        userName.set(dataManager.getUser().firstName + " " + dataManager.getUser().lastName)
        stateAndCountry.set(dataManager.getUser().state + "•" + dataManager.getUser().country)
    }


}