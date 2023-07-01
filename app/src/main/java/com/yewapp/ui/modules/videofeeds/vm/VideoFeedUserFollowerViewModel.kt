package com.yewapp.ui.modules.videofeeds.vm

import android.os.Bundle
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.videofeeds.extras.UserIdExtras
import com.yewapp.ui.modules.videofeeds.navigator.VideoFeedUserFollowerNavigator
import com.yewapp.utils.rx.SchedulerProvider

class VideoFeedUserFollowerViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<VideoFeedUserFollowerNavigator>(dataManager, schedulerProvider) {

    var userId = 0
    var userName = ""
    override fun setData(extras: Bundle?) {
        userId = extras?.getInt(UserIdExtras.USER_ID)!!
        userName = extras.getString(UserIdExtras.USER_Name)!!
        getNavigator()?.bindPager(userId, userName)
    }

}