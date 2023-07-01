package com.yewapp.ui.modules.settings.setting

import com.yewapp.ui.base.BaseNavigator

interface SettingsNavigator : BaseNavigator {
    fun navigateToChatWithParent()
    fun navigateToContactUs()
    fun onLogoutSuccess()
    fun navigateToAbout()
    fun navigateToShareFeedback()
    fun navigateToFaqs()
    fun navigateToRefer()
    fun navigateToTermsAndCondition()
    fun navigateToPrivacyPolicy()
    fun navigateToManageNotification()
    fun navigateToManageUser()
    fun navigateToManageFeeds()
    fun navigateToManageShort()
    fun navigateToSubscription()
    fun navigateToManageDevices()

    //For testing only
}