package com.yewapp.ui.modules.videofeeds.navigator

import com.yewapp.data.network.api.UserList
import com.yewapp.data.network.api.video.VideoData
import com.yewapp.ui.base.BaseNavigator

interface VideoFeedsNavigator : BaseNavigator {
    fun onFeedLikeSuccess(feed: VideoData, position: Int)
    fun onOptionSelectedClick(videoFeed: VideoData, option: String, position: Int)
    fun onFollowSuccess(userList: List<UserList>, position: Int)
}