package com.yewapp.ui.modules.addmodelandequipments.fragments.addmodel

import com.yewapp.ui.base.BaseNavigator

interface AddModelNavigator : BaseNavigator {
    fun onAddIconClick()
    fun addModelLayout()
    fun filterDataRequestToSend()
    fun reDirectToProfile()
}