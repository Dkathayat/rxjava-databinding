package com.yewapp.ui.modules.dashboard.navigator

import androidx.fragment.app.Fragment
import com.yewapp.ui.base.BaseNavigator

interface DashboardNavigator : BaseNavigator {
    fun scrollToPosition(fragment: Fragment)
    fun navigateToFeed()
    fun navigateToProfileSetting()
    fun navigateToAddChallenge()
    fun navigateToVideoFeeds()
    fun navigateToAddMember()
    fun showSportsCompletionAlert(message: String)
}