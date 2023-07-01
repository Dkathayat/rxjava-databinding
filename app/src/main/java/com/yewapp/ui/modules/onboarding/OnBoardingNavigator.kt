package com.yewapp.ui.modules.onboarding

import com.yewapp.ui.base.BaseNavigator

interface OnBoardingNavigator : BaseNavigator {
    fun scrollPagerToPosition(position: Int)
    fun navigateToSignUpOptionScreen()
}