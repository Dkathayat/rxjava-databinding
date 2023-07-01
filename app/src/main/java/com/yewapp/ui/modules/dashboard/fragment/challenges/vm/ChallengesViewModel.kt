package com.yewapp.ui.modules.dashboard.fragment.challenges.vm

import android.os.Bundle
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.dashboard.fragment.challenges.navigator.ChallengesNavigator
import com.yewapp.utils.rx.SchedulerProvider

class ChallengesViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ChallengesNavigator>(dataManager, schedulerProvider) {
    override fun setData(extras: Bundle?) {

    }
}