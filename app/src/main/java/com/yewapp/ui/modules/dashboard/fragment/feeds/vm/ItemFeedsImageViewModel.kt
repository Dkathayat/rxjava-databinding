package com.yewapp.ui.modules.dashboard.fragment.feeds.vm

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.feed.Feed
import com.yewapp.data.network.api.feed.Files

class ItemFeedsImageViewModel(
    val item: Files,
    val feeds: Feed,
    val listener: OnItemClickListener,
    var index: Int
) :
    ViewModel() {

    var image = ObservableField<String>()
    val feedsImageVisibility = ObservableBoolean(true)


    init {
        image.set(item.url)
        if (item.type == "image") {
            feedsImageVisibility.set(true)
        } else {
            feedsImageVisibility.set(false)
        }
    }

    interface OnItemClickListener {
        fun onClickItem(item: Feed)
    }

    fun onItemClick(view: View) {
        listener.onClickItem(feeds)
    }

}