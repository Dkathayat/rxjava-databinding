package com.yewapp.ui.modules.videofeeds.vm

import android.os.Bundle
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.videofeeds.navigator.VideoFeedSearchNavigator
import com.yewapp.utils.rx.SchedulerProvider

class VideoFeedSearchViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<VideoFeedSearchNavigator>(dataManager, schedulerProvider) {
    override fun setData(extras: Bundle?) {

    }
}