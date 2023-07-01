package com.yewapp.ui.dialogs.addbrandmake

import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.sports.Brand

class ItemAddBrandPopUpViewModel(val item: Brand, val listener: (Brand) -> Unit) : ViewModel() {

    fun onOptionClick() {
        listener(item)
    }

}