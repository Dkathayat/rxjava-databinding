package com.yewapp.ui.modules.addchallenge.selectorcreateroute

import com.yewapp.ui.base.BaseNavigator

interface SelectORCreateRouteNavigator : BaseNavigator {

    //    fun getCurrentLocation()
    fun skipCreateRoute()
    fun navigateToPopularRouteScreen()
    fun navigateToLatestRouteScreen()
    fun navigateToFavoriteRouteScreen()

    fun navigateToLocationAdditionalDetailActivity(isRouteCreated: Boolean)

    //    fun navigateToSearchLocationActivity()
    fun navigateToCreateRouteActivity()
}