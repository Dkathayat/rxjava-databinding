package com.yewapp.ui.modules.feedback.navigator

import com.yewapp.ui.base.BaseNavigator

interface ShareFeedbackFragmentNavigator: BaseNavigator {
    fun launchImageOptions()
    fun onSendReportSuccess(onSendReportSuccess: String)
    fun uploadValidation(message: String)
}