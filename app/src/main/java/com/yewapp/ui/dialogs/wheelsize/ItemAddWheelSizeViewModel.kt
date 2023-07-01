package com.yewapp.ui.dialogs.wheelsize

import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.sports.Model
import com.yewapp.data.network.api.sports.WheelSize

class ItemAddWheelSizeViewModel(val item: WheelSize, val listener: (WheelSize) -> Unit) : ViewModel() {

    fun onOptionClick() {
        listener(item)
    }

}