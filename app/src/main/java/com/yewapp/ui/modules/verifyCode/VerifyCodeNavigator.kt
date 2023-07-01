package com.yewapp.ui.modules.verifyCode

import com.yewapp.ui.base.BaseNavigator

interface VerifyCodeNavigator : BaseNavigator {
    fun onVerificationSuccess()
    fun onResendClick()

}