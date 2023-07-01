package com.yewapp.ui.modules.manageshortvideo.vm

import android.os.Bundle
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.manageshortvideo.navigator.ManageShortVideoNavigator
import com.yewapp.utils.rx.SchedulerProvider

class ManageShortVideoViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
BaseViewModel<ManageShortVideoNavigator>(dataManager, schedulerProvider) {
    override fun setData(extras: Bundle?) {
    }
}
