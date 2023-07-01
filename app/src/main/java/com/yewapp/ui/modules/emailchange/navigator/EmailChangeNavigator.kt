package com.yewapp.ui.modules.emailchange.navigator

import com.yewapp.ui.base.BaseNavigator

interface EmailChangeNavigator : BaseNavigator {
    fun selectCountryCodeClick()
    fun changeEmailSuccess(message: String)
}