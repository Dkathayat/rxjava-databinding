package com.yewapp.ui.modules.settings.contactus

import com.yewapp.ui.base.BaseNavigator

interface ContactUsNavigator : BaseNavigator {
    fun launchImageOptions()
    fun onContactSuccess(message: String)
}