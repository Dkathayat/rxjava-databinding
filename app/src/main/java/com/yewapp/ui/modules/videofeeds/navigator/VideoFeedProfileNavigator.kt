package com.yewapp.ui.modules.videofeeds.navigator

import com.yewapp.ui.base.BaseNavigator

interface VideoFeedProfileNavigator : BaseNavigator {
    fun onOptionMenuClick(option: String, id: Int)
    fun onOptionSelectedClick(option: String, userId: Int)
    fun onFollowerClick()
    fun onFollowingClick()
}