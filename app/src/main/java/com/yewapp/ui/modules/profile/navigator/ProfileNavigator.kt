package com.yewapp.ui.modules.profile.navigator

import android.view.View
import com.yewapp.ui.base.BaseNavigator

interface ProfileNavigator : BaseNavigator {
    fun navigateToEditProfile()
    fun navigateToFollower(position:Int)
    fun navigateCompareStatistic()
    fun chooseWhoCanSeeStats(view: View)
    fun allowUsersCanSeePoints(view: View)
    fun becameSponsor()
}