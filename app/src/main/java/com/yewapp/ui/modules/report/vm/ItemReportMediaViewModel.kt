package com.yewapp.ui.modules.report.vm

import android.view.View
import androidx.lifecycle.ViewModel

class ItemReportMediaViewModel(
    val item: String,
    val listener: OnItemClickListener,
    var index: Int
) :
    ViewModel() {

    init {

    }

    interface OnItemClickListener {
        fun onClickItem(item: String, index: Int)
        fun onCrossClick(item: String)
    }

    fun onItemClick(view: View) {
        listener.onClickItem(item, index)
    }

    fun onCrossImageClick() {
        listener.onCrossClick(item)
    }

}