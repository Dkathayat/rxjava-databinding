package com.yewapp.ui.modules.addchallenge.additionalform

import android.view.View
import com.yewapp.ui.base.BaseNavigator

interface AdditionalFormNavigator : BaseNavigator {
    fun navigateToBannerScreen()
    fun navigateEditToBannerScreen()
    fun onWinnerPickedUpClick(view: View)
    fun onWinnerPrizeAwarded(view: View)
    fun onSaveAsDraftClick()
}