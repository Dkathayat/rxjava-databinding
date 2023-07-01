package com.yewapp.ui.modules.challengeDetails.vm

import android.os.Bundle
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.challengeDetails.navigator.ChallengeDescriptionDetailNavigator
import com.yewapp.utils.rx.SchedulerProvider

class ChallengeDescriptionDetailViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
) :
    BaseViewModel<ChallengeDescriptionDetailNavigator>(dataManager, schedulerProvider) {
    override fun setData(extras: Bundle?) {
    }
}