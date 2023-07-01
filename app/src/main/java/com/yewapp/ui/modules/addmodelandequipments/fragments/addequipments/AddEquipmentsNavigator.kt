package com.yewapp.ui.modules.addmodelandequipments.fragments.addequipments

import com.yewapp.ui.base.BaseNavigator

interface AddEquipmentsNavigator : BaseNavigator {
    fun onAddIconClick()
    fun addModelLayout()
    fun filterDataRequestToSend()
    fun reDirectToProfile()
}