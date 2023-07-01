package com.yewapp.ui.dialogs.addmodel

import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.sports.Brand
import com.yewapp.data.network.api.sports.Model

class ItemAddModelPopUpViewModel(val item: Model, val listener: (Model) -> Unit) : ViewModel() {

    fun onOptionClick() {
        listener(item)
    }

}