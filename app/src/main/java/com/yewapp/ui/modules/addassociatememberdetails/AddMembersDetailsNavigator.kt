package com.yewapp.ui.modules.addassociatememberdetails

import com.yewapp.ui.base.BaseNavigator

interface AddMembersDetailsNavigator :BaseNavigator{
    fun updateTabLayout(position: Int, enable: Boolean)
}