package com.yewapp.ui.modules.viewMedia

import com.yewapp.ui.base.BaseNavigator

interface ViewImageVideoNavigator : BaseNavigator {

    fun onVideoClick(type: Boolean)
    fun onStopPlayer()
    fun crossClick()
}