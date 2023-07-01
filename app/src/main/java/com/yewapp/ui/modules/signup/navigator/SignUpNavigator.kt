package com.yewapp.ui.modules.signup.navigator

import com.yewapp.ui.base.BaseNavigator

interface SignUpNavigator : BaseNavigator {
    fun onSignUpSuccess()
    fun onDobClicked()
    fun navigateToTermsAndCondition()
    fun navigateToPrivacyPolicy()

}