package com.yewapp.ui.modules.videofeeds.vm

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.video.VideoData


class ItemVideoFeedsProfileViewModel(val data: VideoData, val listener: OnItemClickListener) :
    ViewModel() {


    var date = ObservableField("")
    var thumbnil = ObservableField("")

    init {
    }


    interface OnItemClickListener {
        fun onItemClick(item: VideoData)
    }

    fun onItemClick() {
        listener.onItemClick(data)
    }

}




