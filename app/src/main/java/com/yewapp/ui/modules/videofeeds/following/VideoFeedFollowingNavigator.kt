package com.yewapp.ui.modules.videofeeds.following

import com.yewapp.data.network.api.follower.MyFollowers
import com.yewapp.ui.base.BaseNavigator

interface VideoFeedFollowingNavigator : BaseNavigator {
    fun onFollowSuccess(userList: List<MyFollowers>, position: Int)

}