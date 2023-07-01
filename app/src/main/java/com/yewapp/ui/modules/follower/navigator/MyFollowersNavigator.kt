package com.yewapp.ui.modules.follower.navigator

import com.yewapp.ui.base.BaseNavigator

interface MyFollowersNavigator : BaseNavigator {
    fun userBlockedSuccess()
    fun userFavouriteSuccess()
    fun userMutedSuccess()
    fun userFollowedSuccess()
    fun launchReportActivity()
}