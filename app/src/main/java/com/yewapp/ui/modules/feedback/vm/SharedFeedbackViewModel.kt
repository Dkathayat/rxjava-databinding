package com.yewapp.ui.modules.feedback.vm

import android.os.Bundle
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.feedback.navigator.ShareFeedbackNavigator
import com.yewapp.ui.modules.manageshortvideo.navigator.ManageShortVideoNavigator
import com.yewapp.utils.rx.SchedulerProvider

class SharedFeedbackViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ShareFeedbackNavigator>(dataManager, schedulerProvider) {
    override fun setData(extras: Bundle?) {
    }
}