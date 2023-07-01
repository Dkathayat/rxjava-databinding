package com.yewapp.ui.modules.feed.navigator

import com.yewapp.ui.base.BaseNavigator

interface CreateFeedNavigator : BaseNavigator {
    fun closeActivity()
    fun chooseActivity()
    fun launchImageOptions()
    fun launchVideoPicker()
    fun uploadValidation(message: String)
}