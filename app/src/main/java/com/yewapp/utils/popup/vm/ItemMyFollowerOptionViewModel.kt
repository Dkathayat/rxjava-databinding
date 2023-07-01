package com.yewapp.utils.popup.vm

import android.view.View
import com.yewapp.data.network.api.follower.MyFollowers

class ItemMyFollowerOptionViewModel(
    val option: String,
    val position: Int,
    val myFollowers: MyFollowers,
    val listener: (String) -> Unit
) {

    var optionLabel = option

    init {
        handleLabel(myFollowers)
    }

    fun handleLabel(myFollowers: MyFollowers) {
        optionLabel = when {
            myFollowers.blocked && position == 5 -> "Unblock"
            myFollowers.favourite && position == 4 -> "Remove as favourite"
            myFollowers.muted && position == 2 -> "Un-mute"
            myFollowers.following && position == 0 -> "Unfollow"
            else -> option
        }

    }

    fun onOptionClick(view: View) {
        listener(option)
    }
}