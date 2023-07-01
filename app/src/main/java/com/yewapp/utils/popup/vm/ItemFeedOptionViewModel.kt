package com.yewapp.utils.popup.vm

import android.view.View
import com.yewapp.data.network.api.feed.Feed

class ItemFeedOptionViewModel(
    val option: String,
    val position: Int,
    val feed: Feed,
    val listener: (String) -> Unit
) {
    var optionLabel = option

    init {
        handleLabel(feed)
    }

    private fun handleLabel(feed: Feed) {
        optionLabel = when {
            feed.createdBy!!.blocked && position == 5 -> "Unblock"
            feed.createdBy.favourite && position == 4 -> "Remove as favourite"
            !feed.createdBy.muted && position == 2 -> "Un-Mute"
            feed.createdBy.following && position == 0 -> "Unfollow"
            else -> option
        }
    }

    fun onOptionClick(view: View) {
        listener(option)
    }
}