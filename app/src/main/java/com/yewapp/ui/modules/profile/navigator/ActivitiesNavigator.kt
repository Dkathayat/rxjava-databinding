package com.yewapp.ui.modules.profile.navigator

import com.yewapp.data.network.api.profile.ActivitiesData
import com.yewapp.ui.base.BaseNavigator

interface ActivitiesNavigator : BaseNavigator {

    fun navigateToDateRangePicker()
    fun navigateToApplyFilter()
    fun configureLineChart()
    fun onFeedLikeSuccess(activitiesData: ActivitiesData)
    fun onSuccessActivityShared(message:String)

}