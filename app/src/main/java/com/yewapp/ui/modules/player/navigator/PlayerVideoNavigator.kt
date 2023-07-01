package com.yewapp.ui.modules.player.navigator

import android.view.View
import com.yewapp.ui.base.BaseNavigator

interface PlayerVideoNavigator : BaseNavigator {
    fun onChallengeAreaClick(view: View)
    fun play()
    fun pause()
    fun navigateToUplish()
}