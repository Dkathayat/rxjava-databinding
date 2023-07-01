package com.yewapp.ui.dialogs.framesize

import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.sports.FrameSize

class ItemFrameSizePopUpViewModel(val item: FrameSize, val listener: (FrameSize) -> Unit) :
    ViewModel() {

    fun onOptionClick() {
        listener(item)
    }

}