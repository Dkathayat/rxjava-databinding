package com.yewapp.ui.modules.managenotification.vm

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.notification.Notification
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.managenotification.navigator.NotificationFragmentNavigator
import com.yewapp.utils.rx.SchedulerProvider

class NotificationFragmentViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<NotificationFragmentNavigator>(dataManager, schedulerProvider) {
    val _notificationList = MutableLiveData<List<Notification>>()
    val notificationListLive: LiveData<List<Notification>> get() = _notificationList
    val notificationList = mutableListOf<Notification>()

    override fun setData(extras: Bundle?) {

    }

    init {

    }
}