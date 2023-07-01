package com.yewapp.ui.base

interface BaseNavigator {
    fun onBackPress()
    fun onError(t: Throwable, showAction: Boolean = false, function: () -> Unit = {})
    fun onSuccess(message: String, showAction: Boolean = true, function: () -> Unit = {})
    fun navigateToSetting()
    fun showProfileCompletionAlert(message: String)

//    fun unableDisableScreenTouch(isScreenDisable:Boolean)

}