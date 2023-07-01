package com.yewapp.ui.dialogs.challengepopupdialogs.vm

import androidx.lifecycle.ViewModel

class VisibilityViewModel(val item: String, val listener: (String) -> Unit) : ViewModel() {

    fun onOptionClick() {
        listener(item)
    }

}