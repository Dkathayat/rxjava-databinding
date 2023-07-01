package com.yewapp.ui.modules.viewMedia.vm

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.feed.Files

class ItemViewMediaViewModel(val item: Files, val listener: OnItemClickListener, var index: Int) :
    ViewModel() {

    var image = ObservableField<String>()
    var videoIconVisibility = ObservableField<Boolean>(false)

    init {
        image.set(item.url)
        if (item.type != "image") {
            videoIconVisibility.set(true)
        } else {
            videoIconVisibility.set(false)
        }
    }

    interface OnItemClickListener {
        fun onClickItem(item: Files)
    }

    fun onItemClick() {
        listener.onClickItem(item)
    }

}