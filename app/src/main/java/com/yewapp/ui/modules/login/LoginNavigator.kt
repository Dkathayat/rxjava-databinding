package com.yewapp.ui.modules.login

import com.yewapp.ui.base.BaseNavigator

interface LoginNavigator : BaseNavigator {
    fun navigateToForgotPassword()
    fun navigateToLogin()
    fun navigateToVerifyCode()

}