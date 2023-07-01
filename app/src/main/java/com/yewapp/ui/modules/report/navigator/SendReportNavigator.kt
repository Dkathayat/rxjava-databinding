package com.yewapp.ui.modules.report.navigator

import com.yewapp.ui.base.BaseNavigator

interface SendReportNavigator : BaseNavigator {
    fun launchImageOptions()
    fun onSendReportSuccess(onSendReportSuccess: String)
    fun uploadValidation(message: String)


}