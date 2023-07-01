package com.yewapp.ui.modules.addspectator

import android.view.View
import com.yewapp.ui.base.BaseNavigator

interface AddSpectatorNavigator : BaseNavigator {
    fun chooseUserType(view: View)
    abstract fun navigateToSpectatorListing(message: String)
}