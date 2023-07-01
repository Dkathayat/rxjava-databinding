package com.yewapp.ui.modules.addchallenge.challengepreview

import com.yewapp.ui.base.BaseNavigator

interface ChallengePreviewNavigator : BaseNavigator {
    fun onAthleteClicked()
    fun createChallengeSuccess()
    fun showAlertForInactiveChallenge()
}