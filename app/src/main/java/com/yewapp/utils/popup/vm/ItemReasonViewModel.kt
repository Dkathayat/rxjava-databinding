package com.yewapp.utils.popup.vm

import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.report.ReportReason

class ItemReasonViewModel(val item: ReportReason, val listener: (ReportReason) -> Unit) :
    ViewModel() {


    fun onClick() {
        listener(item)
    }
}