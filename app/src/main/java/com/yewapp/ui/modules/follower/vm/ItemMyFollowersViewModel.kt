package com.yewapp.ui.modules.follower.vm

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.follower.MyFollowers
import com.yewapp.utils.popup.PopUpDialog

class ItemMyFollowersViewModel(
    val myfollowers: MyFollowers,
    val position: Int,
    val onItemClickListener: (String, MyFollowers, Int) -> Unit

) : ViewModel() {


    val profileImageUrl = ObservableField<String>("")
    var address = ObservableField<String>("")
    // var popMenu = ObservableField<String>("")

    init {
        profileImageUrl.set(myfollowers.image ?: "")
        address.set(myfollowers.city + " " + myfollowers.state + " " + myfollowers.country)

    }

    fun onOptionClick(view: View) {
        PopUpDialog.showFollowerOptionsPopUp(view, myfollowers) {
            onItemClickListener(it, myfollowers, position)
        }
    }
}