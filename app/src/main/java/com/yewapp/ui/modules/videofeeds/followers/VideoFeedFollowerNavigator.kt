package com.yewapp.ui.modules.videofeeds.followers

import com.yewapp.data.network.api.follower.MyFollowers
import com.yewapp.ui.base.BaseNavigator

interface VideoFeedFollowerNavigator : BaseNavigator {
    fun onFollowSuccess(userList: List<MyFollowers>, position: Int)

}