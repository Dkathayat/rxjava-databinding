package com.yewapp.ui.dialogs.addyear

import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.sports.FrameSize

class ItemAddYearViewModel(val year: String, val listener: (String) -> Unit) :
    ViewModel() {

    fun onOptionClick() {
        listener(year)
    }

}