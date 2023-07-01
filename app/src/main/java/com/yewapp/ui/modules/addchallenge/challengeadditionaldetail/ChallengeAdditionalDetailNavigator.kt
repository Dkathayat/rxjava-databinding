package com.yewapp.ui.modules.addchallenge.challengeadditionaldetail

import android.view.View
import com.yewapp.ui.base.BaseNavigator

interface ChallengeAdditionalDetailNavigator : BaseNavigator {
    fun onCountryClicked()
    fun onStateClicked()
    fun onCityClicked()
    fun navigateToMinGoalRequirementActivity()
    fun onChallengeAreaClick(view: View)
    fun onLocationClick()
    fun onRadiusClick(view: View)
    fun skipScreen()
}