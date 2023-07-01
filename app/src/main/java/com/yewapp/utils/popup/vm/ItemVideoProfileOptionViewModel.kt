package com.yewapp.utils.popup.vm

import android.view.View

class ItemVideoProfileOptionViewModel(
    val option: String,
    val position: Int,
    val id: Int,
    val listener: (String) -> Unit
) {
    var optionLabel = option


    fun onOptionClick(view: View) {
        listener(option)
    }
}