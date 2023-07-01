package com.yewapp.ui.modules.refer.vm

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.refer.ReferResponse

class ItemReferredFriendViewModel(
    val referResponse: ReferResponse
) : ViewModel() {
    var name = ObservableField<String>("")
    var mobileNo = ObservableField<String>("")
    var image = ObservableField<String>("")
    var url = ObservableField<String>("")
    val bitmap = ObservableField<String>("")
    val refferedStatus = ObservableField<Boolean>()

    init {
        if (referResponse.status == "0") {
            // set visibility
            refferedStatus.set(false)

        } else {
            // visibilty
            refferedStatus.set(true)

        }

        //name.set(referResponse.name)
        //profileImageUrl.set(referResponse.image ?: "")
    }
}