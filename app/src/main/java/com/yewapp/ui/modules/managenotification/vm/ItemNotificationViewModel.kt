package com.yewapp.ui.modules.managenotification.vm

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.notification.Notification

class ItemNotificationViewModel(
    val item: Notification,
    val listener: OnItemClickListener,
    var index: Int
) :
    ViewModel() {

    var title = ObservableField<String>()
    var dateTime = ObservableField<String>()

    init {

    }

    interface OnItemClickListener {
        fun onClickItem(item: Notification)
    }

    fun onItemClick(view: View) {
        listener.onClickItem(item)
    }

}