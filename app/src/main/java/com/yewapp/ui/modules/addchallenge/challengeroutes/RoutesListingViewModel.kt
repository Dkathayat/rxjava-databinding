package com.yewapp.ui.modules.addchallenge.challengeroutes

import android.os.Bundle
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.challenges.ChallengeModel
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.listner.ChallengeExtras
import com.yewapp.utils.rx.SchedulerProvider

class RoutesListingViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<RoutesListingNavigator>(dataManager, schedulerProvider) {


    lateinit var challengeModel: ChallengeModel

    override fun setData(extras: Bundle?) {
        challengeModel =
            extras?.getParcelable(ChallengeExtras.CHALLENGE_DATA) ?: return

        getNavigator()?.navigateToRouteList(challengeModel.navigateToRouteFragment ?: return)
    }
}