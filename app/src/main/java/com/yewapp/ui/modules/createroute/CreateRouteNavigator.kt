package com.yewapp.ui.modules.createroute

import com.yewapp.ui.base.BaseNavigator

interface CreateRouteNavigator : BaseNavigator {
//    fun navigateToPopularRouteScreen()
//    fun navigateToLatestRouteScreen()
//    fun navigateToFavoriteRouteScreen()
    fun resetMapBox()
    fun navigateToMapBoxSearch()
    fun setDataToCreateRoute()
    fun getCurrentLocation()
//    fun optimizeEditRoute()
//    fun getElevationFromMapBoxApi()
}