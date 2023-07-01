package com.yewapp.ui.modules.feed.vm

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class CreateFeedImageItemViewModel(
    val item: String,
    val listener: OnItemClickListener,
    var index: Int
) ://
    ViewModel() {

    var image = ObservableField<String>()


    init {
        image.set(item)
    }

    interface OnItemClickListener {
        fun onClickItem(item: String)
        fun onCrossClick(item: String)
    }

    fun onItemClick() {
        listener.onClickItem(item)
    }

    fun onCrossImageClick() {
        listener.onCrossClick(item)
    }

}