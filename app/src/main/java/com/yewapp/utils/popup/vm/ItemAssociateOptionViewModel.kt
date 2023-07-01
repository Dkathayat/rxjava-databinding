package com.yewapp.utils.popup.vm

import android.view.View
import com.yewapp.data.network.api.associate.Associate
import com.yewapp.data.network.api.feed.Feed

class ItemAssociateOptionViewModel(
    val option: String,
    val position: Int,
    val associate: Associate,
    val listener: (String) -> Unit
) {

    fun onOptionClick(view: View) {
        listener(option)
    }
}