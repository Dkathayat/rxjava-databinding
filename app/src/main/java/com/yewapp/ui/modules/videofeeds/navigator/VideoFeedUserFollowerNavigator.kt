package com.yewapp.ui.modules.videofeeds.navigator

import com.yewapp.ui.base.BaseNavigator

interface VideoFeedUserFollowerNavigator : BaseNavigator {
    fun bindPager(userId: Int, userName: String)
}