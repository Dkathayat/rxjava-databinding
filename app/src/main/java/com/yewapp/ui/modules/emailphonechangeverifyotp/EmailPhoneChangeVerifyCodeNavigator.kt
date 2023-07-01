package com.yewapp.ui.modules.emailphonechangeverifyotp

import com.yewapp.ui.base.BaseNavigator

interface EmailPhoneChangeVerifyCodeNavigator : BaseNavigator {
    fun onVerificationSuccess()
    fun onResendClick()
    fun resendEmailSuccess(message: String)
    fun onLogoutSuccess()
    fun setMessageForEmail(b: Boolean)

}