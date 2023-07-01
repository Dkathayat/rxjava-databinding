package com.yewapp.ui.modules.addchallenge.addchallengesportstype

import com.yewapp.ui.base.BaseNavigator

interface AddChallengeNavigator : BaseNavigator {
    fun navigateToSelectDateRange()
    fun navigateToastMessage()
    fun saveAsDraft()
}