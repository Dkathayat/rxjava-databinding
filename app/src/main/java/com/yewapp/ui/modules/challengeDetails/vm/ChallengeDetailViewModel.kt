package com.yewapp.ui.modules.challengeDetails.vm

import android.os.Bundle
import android.view.View
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.challenges.ChallengeModel
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.challengeDetails.navigator.ChallengeDetailNavigator
import com.yewapp.ui.modules.listner.ChallengeExtras
import com.yewapp.utils.rx.SchedulerProvider

class ChallengeDetailViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ChallengeDetailNavigator>(dataManager, schedulerProvider) {

    lateinit var challengeModel: ChallengeModel

    override fun setData(extras: Bundle?) {
        challengeModel =
            extras?.getParcelable(ChallengeExtras.CHALLENGE_DATA) ?: return

    }

    fun onWinnerClick(view: View) {
        when (view.id) {
            R.id.winner_text -> {
                getNavigator()?.navigateToGiveAwayDetails()
            }
            R.id.challenge_detail_description -> {
                getNavigator()?.navigateDescriptionDetails()
            }
        }
    }
}