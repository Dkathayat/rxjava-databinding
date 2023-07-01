package com.yewapp.ui.dialogs.challengepopupdialogs.vm

import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.sports.Sport

class ItemChallengePopUpViewModel(val item: Sport, val listener: (Sport) -> Unit) : ViewModel() {

    fun onOptionClick() {
        listener(item)
    }

}