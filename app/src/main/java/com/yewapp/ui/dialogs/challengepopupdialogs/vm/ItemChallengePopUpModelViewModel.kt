package com.yewapp.ui.dialogs.challengepopupdialogs.vm

import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.sports.Model

class ItemChallengePopUpModelViewModel(val item: Model, val listener: (Model) -> Unit) :
    ViewModel() {

    fun onOptionClick() {
        listener(item)
    }

}