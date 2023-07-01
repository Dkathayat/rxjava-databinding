package com.yewapp.ui.dialogs.type

import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.sports.Type
import com.yewapp.data.network.api.sports.WheelSize

class ItemTypeViewModel(val item: Type, val listener: (Type) -> Unit) : ViewModel() {

    fun onOptionClick() {
        listener(item)
    }

}