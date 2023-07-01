package com.yewapp.utils.popup.vm

import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.report.ReportReason
import com.yewapp.data.network.api.sports.Sport

class ItemReasonFeebackViewModel(val item: Sport, val listener: (Sport) -> Unit) :
    ViewModel() {

    fun onClick() {
        listener(item)
    }
}