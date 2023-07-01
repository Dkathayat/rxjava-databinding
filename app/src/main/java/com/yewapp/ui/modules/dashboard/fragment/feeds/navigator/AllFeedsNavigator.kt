package com.yewapp.ui.modules.dashboard.fragment.feeds.navigator

import com.yewapp.data.network.api.feed.Feed
import com.yewapp.ui.base.BaseNavigator

interface AllFeedsNavigator : BaseNavigator {
    fun onFilterClick()
    fun onCalendarSelected()
    fun userBlockedSuccess()
    fun userFavouriteSuccess()
    fun userMutedSuccess()
    fun userFollowedSuccess()
    fun launchReportActivity(id: Int)
    fun onFeedLikeSuccess(feed: Feed)
    fun followClick(option: String, feed: Feed)
    fun muteUnmuteClick(option: String, feed: Feed)
    fun addToFavouriteClick(option: String, feed: Feed)
    fun blockClick(option: String, feed: Feed)
    fun onFollowButtonClick()

    fun onEditFeedClick(feed: Feed)
}