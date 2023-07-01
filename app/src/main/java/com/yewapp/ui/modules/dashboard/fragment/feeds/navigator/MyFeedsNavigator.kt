package com.yewapp.ui.modules.dashboard.fragment.feeds.navigator

import com.yewapp.data.network.api.feed.Feed
import com.yewapp.ui.base.BaseNavigator

interface MyFeedsNavigator : BaseNavigator {
    fun onFilterClick()
    fun showCalendar()
    fun onFeedLikeSuccess(feed: Feed)
    fun onCreateFeedClick()

    fun onEditFeedClick(feed: Feed)

}