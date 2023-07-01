package com.yewapp.ui.modules.forgotPassword.navigators

import com.yewapp.ui.base.BaseNavigator

interface ResetPasswordNavigator : BaseNavigator {
    fun onSuccess()
    fun onResendClick()
}