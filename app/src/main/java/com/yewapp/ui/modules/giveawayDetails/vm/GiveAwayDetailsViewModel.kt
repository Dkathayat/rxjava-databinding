package com.yewapp.ui.modules.giveawayDetails.vm

import android.os.Bundle
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.giveawayDetails.navigator.GiveAwayDetailsNavigator
import com.yewapp.utils.rx.SchedulerProvider

class GiveAwayDetailsViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<GiveAwayDetailsNavigator>(dataManager, schedulerProvider) {

    override fun setData(extras: Bundle?) {
    }
}