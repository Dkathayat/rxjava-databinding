package com.yewapp.ui.dialogs.profile

import androidx.lifecycle.ViewModel

class ItemProfileSingleSelectionViewModel(val item: String, val listener: (String) -> Unit) :
    ViewModel() {

    fun onOptionClick() {
        listener(item)
    }

}