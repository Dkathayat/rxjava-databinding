package com.yewapp.utils.popup.vm

import android.view.View
import com.yewapp.data.network.api.spectator.SpectatorMember

class ItemSpectatorOptionViewModel(
    val option: String,
    val position: Int,
    val spectator: SpectatorMember,
    val listener: (String) -> Unit
) {

    fun onOptionClick(view: View) {
        listener(option)
    }
}
