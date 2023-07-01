package com.yewapp.ui.modules.refer.vm

import android.os.Bundle
import com.yewapp.data.network.DataManager
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.refer.navigator.ReferralNavigator
import com.yewapp.utils.rx.SchedulerProvider

class ReferralViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ReferralNavigator>(dataManager, schedulerProvider) {

    override fun setData(extras: Bundle?) {
    }


}