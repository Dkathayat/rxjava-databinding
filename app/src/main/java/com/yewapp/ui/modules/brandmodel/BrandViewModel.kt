package com.yewapp.ui.modules.brandmodel

import android.os.Bundle
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.utils.rx.SchedulerProvider

class BrandViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<BrandNavigator>(dataManager, schedulerProvider) {

    override fun setData(extras: Bundle?) {

    }
}