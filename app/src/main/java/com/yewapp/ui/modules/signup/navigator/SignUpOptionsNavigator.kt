package com.yewapp.ui.modules.signup.navigator

import com.yewapp.ui.base.BaseNavigator

interface SignUpOptionsNavigator : BaseNavigator {
    fun navigateToLogin()
    fun navigateToSignUp()
    fun navigateToGoogleSignUp()
    fun navigateToFacebookSignUp()
    fun onSignUpSocialSuccess()
    fun navigateToTermsAndCondition()
    fun navigateToPrivacyPolicy()

}