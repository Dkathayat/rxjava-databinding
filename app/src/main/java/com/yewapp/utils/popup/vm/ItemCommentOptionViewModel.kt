package com.yewapp.utils.popup.vm

import android.view.View
import com.yewapp.data.network.api.video.Comment

class ItemCommentOptionViewModel(
    val option: String,
    val position: Int,
    val comment: Comment,
    val listener: (String) -> Unit
) {
    var optionLabel = option

    init {
        //  handleLabel(comment)
    }

//    fun handleLabel(comment: Comment) {
//        optionLabel = when {
//            feed.createdBy.blocked && position == 5 -> "Unblock"
//            feed.createdBy.favourite && position == 4  -> "Remove as favourite"
//            feed.createdBy.muted && position == 2 -> "Un-mute"
//            feed.createdBy.following && position == 0 -> "Unfollow"
//            else -> option
//        }
//    }

    fun onOptionClick(view: View) {
        listener(option)
    }
}